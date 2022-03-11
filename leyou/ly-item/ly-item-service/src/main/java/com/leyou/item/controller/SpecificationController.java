package com.leyou.item.controller;

import com.leyou.item.pojo.Specification;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/12 16:26
 */
@RestController
@RequestMapping("/spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specService;

    @GetMapping("/query/{id}")
    public ResponseEntity<String> querySpecificationByCategoryId(@PathVariable("id") Long id){
        Specification spec = this.specService.queryById(id);
        if (spec == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(spec.getSpecifications());
    }

    @RequestMapping("/save")
    public ResponseEntity<Void> insertSpecificationByCategoryId(Long cId,String specifications){
        Long oldCId = specService.queryCIdBySpecId(cId);
        if(oldCId == null){
            /*System.out.println("添加");*/
            this.specService.insertSpecification(cId,specifications);
        }else{
            /*System.out.println("修改");*/
            this.specService.updateSpecification(cId,specifications);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
