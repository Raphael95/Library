package com.example.myapp.controller;

import com.example.myapp.domain.DangerouCargo;
import com.example.myapp.mapper.DangerousCargoMapper;
import com.example.myapp.util.LuceneUtil;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
public class DangerousCargoController {
    LuceneUtil luceneUtil = new LuceneUtil();

    @Autowired
    private DangerousCargoMapper dangerousCargoMapper;

    @RequestMapping(value = "/sayHello",method = RequestMethod.GET)
    public String getString(){
        System.out.println("hello ,qdefddsacv");
        return "hello ,myApp";
    }

    @RequestMapping(value = "/getAllCargo",method = RequestMethod.GET)
    public List<DangerouCargo> getAllCargo(){
        List<DangerouCargo> allCargos = dangerousCargoMapper.getAll();
        return allCargos;
    }

    @RequestMapping(value = "/searchCargo/{cargoName}",method = RequestMethod.GET)
    public List<DangerouCargo> searchCargo(@PathVariable String cargoName){
        List<DangerouCargo> allCargos = dangerousCargoMapper.getAll();
        List<DangerouCargo> reults = null;
        System.out.println("获得的参数是："+cargoName);
        try{
            luceneUtil.createIndex(allCargos);
            TopDocs topDocs = luceneUtil.search(cargoName);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            reults = luceneUtil.addHitsToList(scoreDocs);

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("查询数据库的时候出错！");
        }
        return reults;
    }


    @RequestMapping(value = "/getCargo/{id}",method = RequestMethod.GET)
    public DangerouCargo getCargo(@PathVariable Integer id){
        DangerouCargo dangerousCargo = dangerousCargoMapper.getOne(id);
        return dangerousCargo;
    }

    @RequestMapping(value = "/addCargo",method = RequestMethod.POST)
    public String insertCargo(DangerouCargo cargo){
        dangerousCargoMapper.insertCargo(cargo);
        return "success";
    }

    @RequestMapping(value = "/updateCargo",method = RequestMethod.POST)
    public String updateCargo(DangerouCargo cargo){
        dangerousCargoMapper.updateCargo(cargo);
        return "success";
    }

    @RequestMapping(value = "/deleteCargo/{id}",method = RequestMethod.DELETE)
    public String deleteCargo(@PathVariable Integer id){
        dangerousCargoMapper.deleteCargo(id);
        return "success";
    }
}
