package com.itheima;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class App2_seach {

    private String path = "D:\\lucence\\library\\index";

    @Test
    public void search() throws ParseException, IOException {
    //1.创建分析器,用于分词
        Analyzer analyzer=new StandardAnalyzer();

        //2.创建查询解析器,解析查询语句
        QueryParser queryParser=new QueryParser("bookname",analyzer);

        //3.创建查询对象,指定查询语法
        Query query=queryParser.parse("bookname:java");

        //4.创建目录对象,指定索引库目录路径
        Directory directory= FSDirectory.open(new File(path));

        //5.创建索引库读取对象,读取索引文件到内存
        IndexReader reader= DirectoryReader.open(directory);

        //6.创建索引库搜索对象,执行查询
        IndexSearcher indexSearcher=new IndexSearcher(reader);

        //7.搜索结果
        TopDocs topDocs=indexSearcher.search(query,10);
        // 处理结果1：获取结果总数
        System.out.println("搜索结果总数：" + topDocs.totalHits);
        // 处理结果2：获取搜索结果内容
        // 返回ScoreDoc数组，ScoreDoc里面只包含2个内容： 文档的id和分值
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        Arrays.asList(scoreDocs).forEach(scoreDoc -> {
            System.out.println("-------华丽分割-------");
            System.out.println("文档ID：" + scoreDoc.doc);
            System.out.println("文档分值：" + scoreDoc.score);
            try {
                // 根据文档的id，获取文档
                Document doc = indexSearcher.doc(scoreDoc.doc);
                // 获取文档域
                System.out.println("图书编号：" + doc.get("id"));
                System.out.println("图书名称：" + doc.get("bookname"));
                System.out.println("图书价格：" + doc.get("price"));
                System.out.println("图书图片：" + doc.get("pic"));
                System.out.println("图书描述：" + doc.get("bookdesc"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // 关闭
        reader.close();
    }
}