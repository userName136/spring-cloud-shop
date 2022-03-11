package com.leyou.item.controller;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.PageResult;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/5 16:13
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService bService;

    /**
     * 查询商品信息列表，分页
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key) {
        PageResult<Brand> result = this.bService.queryBrandByPageAndSort(page,rows,sortBy,desc, key);
        if (result == null || result.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 新增&修改 商品信息
     * @param brand
     * @param cIds
     * @return
     */
    @PostMapping("/update")
    public ResponseEntity<Void> saveBrand(Brand brand,@RequestParam(value = "cIds",required = false)List<Long> cIds){
        /*System.out.println(brand+"---"+cIds);*/
        if(brand.getId()==0){
            /* System.out.println("添加"); */
            this.bService.saveBrand(brand, cIds);
        }else{
            /* System.out.println("修改"); */
            this.bService.editBrand(brand,cIds);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/delete/{bId}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("bId")Long bId){
        this.bService.deleteBrand(bId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 根据分类查询品牌
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCategory(@PathVariable("cid") Long cid) {
        List<Brand> list = this.bService.queryBrandByCategory(cid);
        if(list == null){
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 根据多个id查询品牌
     * @param ids
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids") List<Long> ids){
        System.out.println("进入方法--"+ids);
        List<Brand> list = this.bService.queryBrandByIds(ids);
        if(list == null){
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

}
