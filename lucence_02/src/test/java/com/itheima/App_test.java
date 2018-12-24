package com.itheima;

import com.itheima.dao.imp.BookDaoImpl;
import com.itheima.entity.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App_test {

    private String path = "D:\\lucence\\library\\index";

    @Test
    public void createIndex() throws IOException {

        //1. 查询数据库，采集数据
        List<Book> bookList = new BookDaoImpl().findAllBooks();

        //2. 把数据转换为文档对象
        List<Document> documentList = new ArrayList<>();
        bookList.forEach(book -> {
            // 创建文档对象
            Document document = new Document();
            /**
             * 给文档对象添加域
             * TextField 文本域
             * 参数1：域名称，可以随便写。
             * 参数2：域名称对应的值
             * 参数3：是否存储域的值到索引域。
             */
            document.add(new TextField("id", book.getId() + "", Field.Store.YES));
            document.add(new TextField("bookname", book.getBookname(), Field.Store.YES));
            document.add(new TextField("price", book.getPrice() + "", Field.Store.YES));
            document.add(new TextField("pic", book.getPic(), Field.Store.YES));
            document.add(new TextField("bookdesc", book.getBookdesc(), Field.Store.YES));
            // 添加到集合
            documentList.add(document);
        });
        //3.创建分析器,主要用于分词
        Analyzer analyzer = new StandardAnalyzer();

        //4.  创建索引库配置对象,生成索引文件
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);

        //5.创建索引库目录对象,储存生成的索引文件
        Directory d = FSDirectory.open(new File(path));

        // 6.创建索引库操作对象,生成索引文件
        IndexWriter writer = new IndexWriter(d, conf);

        //7.把文档对象写入索引库
        documentList.forEach(document -> {
            try {
                writer.addDocument(document);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //8.关闭释放资源
        writer.commit();
        writer.close();

    }
}
