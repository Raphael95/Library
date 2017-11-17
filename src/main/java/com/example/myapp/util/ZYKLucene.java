package com.example.myapp.util;

import com.example.myapp.domain.ZYK;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ZYKLucene {
    private static final  String searchDir = "H:\\luceneTestDir";
    private static File indexFile = null;
    private static Analyzer analyzer = null;
    private static IndexSearcher indexSearcher = null;

    public void createIndex(List<ZYK> lists) throws Exception{
        Directory directory = null;
        IndexWriter indexWriter = null;
        try{
            indexFile = new File(searchDir);
            if(!indexFile.exists()){
                indexFile.mkdir();
            }
            directory = FSDirectory.open(indexFile.toPath());
            //analyzer = new StandardAnalyzer();
            analyzer = new MyIkAnalyzer();
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            indexWriter = new IndexWriter(directory,indexWriterConfig);
            //indexWriter.deleteAll();
            Document doc = null;
            for(ZYK zyk : lists){
                doc = new Document();
                doc.add(new Field("KZH",zyk.getKZH(), TextField.TYPE_STORED));
                doc.add(new Field("title",zyk.getTitle()==null?"":zyk.getTitle(),TextField.TYPE_STORED));
                doc.add(new Field("authors",zyk.getAuthors()==null?"":zyk.getAuthors(),TextField.TYPE_STORED));
                doc.add(new Field("index",zyk.getIndex()==null?"":zyk.getIndex(), TextField.TYPE_STORED));
                doc.add(new Field("isbn",zyk.getIsbn()==null?"":zyk.getIsbn(),TextField.TYPE_STORED));
                doc.add(new Field("publisher",zyk.getPublisher()==null?"":zyk.getPublisher(),TextField.TYPE_STORED));
                doc.add(new Field("publishPlace",zyk.getPublishPlace()==null?"":zyk.getPublishPlace(),TextField.TYPE_STORED));
                doc.add(new Field("publishTime",zyk.getPublishTime()==null?"":zyk.getPublishTime(),TextField.TYPE_STORED));
                doc.add(new Field("price",String.valueOf(zyk.getPrice()==null?"":zyk.getPrice()),TextField.TYPE_STORED));
                doc.add(new Field("entryTime",String.valueOf(zyk.getEntryTime()==null?"":zyk.getEntryTime()),TextField.TYPE_STORED));
                doc.add(new Field("context",zyk.getContext()==null?"":zyk.getContext(),TextField.TYPE_STORED));
                doc.add(new Field("pages",zyk.getPages()==null?"":zyk.getPages(),TextField.TYPE_STORED));
                doc.add(new Field("format",zyk.getFormat()==null?"":zyk.getFormat(),TextField.TYPE_STORED));
                indexWriter.addDocument(doc);
            }
            //System.out.println("建立索引得到的doc是："+doc);

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            indexWriter.close();
        }
    }

    public TopDocs search(String searchStr) throws Exception{
        if(indexSearcher == null){
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(indexFile.toPath()));
            indexSearcher = new IndexSearcher(indexReader);
        }
        QueryParser parser = new QueryParser("title",analyzer);
        Query query = parser.parse(searchStr);
        TopDocs topDocs = indexSearcher.search(query,100);
        return topDocs;
    }

    public List<ZYK> addHitsToList(ScoreDoc[] scoreDocs) throws Exception{
        List<ZYK> lists = new ArrayList<>();
        ZYK zyk = null;
        for(int i=0;i<scoreDocs.length;i++){
            Document doc = indexSearcher.doc(scoreDocs[i].doc);
            zyk = new ZYK();
            zyk.setKZH(doc.get("KZH"));
            zyk.setTitle(doc.get("title"));
            zyk.setAuthors(doc.get("authors"));
            zyk.setIndex(doc.get("index"));
            zyk.setIsbn(doc.get("isbn"));
            zyk.setPublisher(doc.get("publisher"));
            zyk.setPublishPlace(doc.get("publishPlace"));
            zyk.setPublishTime(doc.get("publishTime"));
            zyk.setPrice(Float.parseFloat(doc.get("price")));
            zyk.setEntryTime(new Date(doc.get("entryTime")));
            zyk.setContext(doc.get("context"));
            zyk.setPages(doc.get("pages"));
            zyk.setFormat(doc.get("format"));
            lists.add(zyk);
        }
        return lists;
    }
}
