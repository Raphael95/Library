package com.example.myapp.util;

import com.example.myapp.domain.DangerouCargo;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.*;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LuceneUtil {
    private static final  String searchDir = "H:\\luceneTestDir";
    private static File indexFile = null;
    private static Analyzer analyzer = null;
    private static  IndexSearcher indexSearcher = null;


    public void createIndex(List<DangerouCargo> lists) throws Exception{
        Directory directory = null;
        IndexWriter indexWriter = null;
        try{
            indexFile = new File(searchDir);
            if(!indexFile.exists()){
                indexFile.mkdir();
            }
            directory = FSDirectory.open(indexFile.toPath());
            analyzer = new StandardAnalyzer();
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            indexWriter = new IndexWriter(directory,indexWriterConfig);
            indexWriter.deleteAll();
            Document doc = null;
            for(DangerouCargo dangerouCargo : lists){
                doc = new Document();
                doc.add(new Field("dangerousId",String.valueOf(dangerouCargo.getDangerousId()), TextField.TYPE_STORED));
                doc.add(new Field("cargoName",dangerouCargo.getCargoName(),TextField.TYPE_STORED));
                doc.add(new Field("safeCardPath",dangerouCargo.getSafeCardPath()==null?"":dangerouCargo.getSafeCardPath(),TextField.TYPE_STORED));
                indexWriter.addDocument(doc);
            }
            System.out.println("建立索引得到的doc是："+doc);

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
        QueryParser parser = new QueryParser("cargoName",analyzer);
        Query query = parser.parse(searchStr);
        TopDocs topDocs = indexSearcher.search(query,100);
        return topDocs;
    }

    public List<DangerouCargo> addHitsToList(ScoreDoc[] scoreDocs) throws Exception{
        List<DangerouCargo> lists = new ArrayList<>();
        DangerouCargo dangerouCargo = null;
        System.out.println("Score的长度为："+scoreDocs.length);
        for(int i=0;i<scoreDocs.length;i++){
            Document doc = indexSearcher.doc(scoreDocs[i].doc);
            System.out.println("检索出来的doc是："+doc);
            dangerouCargo = new DangerouCargo();
            System.out.println("检索出来的dangerousId是："+doc.get("dangerousId"));
            System.out.println("检索出来的cargoName是："+doc.get("cargoName"));
            System.out.println("检索出来的safeCardPath是："+doc.get("safeCardPath"));
            dangerouCargo.setDangerousId(Integer.parseInt(doc.get("dangerousId")));
            dangerouCargo.setCargoName(doc.get("cargoName"));
            dangerouCargo.setSafeCardPath(doc.get("safeCardPath"));
            lists.add(dangerouCargo);
        }
        return lists;
    }

}
