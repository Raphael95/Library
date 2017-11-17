package com.example.myapp.controller;

import com.example.myapp.domain.ZYK;
import com.example.myapp.mapper.ZYKMapper;
import com.example.myapp.util.ZYKLucene;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ZYKController {

    ZYKLucene zykLucene = new ZYKLucene();

    @Autowired
    private ZYKMapper zykMapper;

    @RequestMapping(value = "/getAllBooks",method = RequestMethod.GET)
    public List<ZYK> getAllBooks(){
        List<ZYK> lists = zykMapper.getAllBooks();
        return lists;
    }

    @RequestMapping(value = "/searchBook/{title}",method = RequestMethod.GET)
    public List<ZYK> searchBook(@PathVariable String title){
        List<ZYK> lists =zykMapper.getAllBooks();
        List<ZYK> results = null;
        try{
            zykLucene.createIndex(lists);
            TopDocs topDocs = zykLucene.search(title);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            results = zykLucene.addHitsToList(scoreDocs);

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("查询数据库时出错！");
        }
        return results;
    }

    @RequestMapping(value = "/search/{title}",method = RequestMethod.GET)
    public List<ZYK> searchBooks(@PathVariable String title){
        List<ZYK> lists =zykMapper.getAllBooks();
        List<ZYK> results = null;
        try{
            TopDocs topDocs = zykLucene.search(title);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            results = zykLucene.addHitsToList(scoreDocs);

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("查询数据库时出错！");
        }
        return results;
    }



}
