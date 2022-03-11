package com.leyou.item.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leyou.item.pojo.*;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/13 10:05
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 分页查询SPU
     * @param page
     * @param rows
     * @param key
     * @return
     */
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable",required=false,defaultValue = "null")Boolean saleable,
            @RequestParam(value = "desc",defaultValue = "false")Boolean desc,
            @RequestParam(value = "sortBy",required = false)String sortBy
            ) {
        // 分页查询spu信息
        PageResult<SpuBo> result = this.goodsService.querySpuByPageAndSort(page,rows,saleable,key,desc,sortBy);
        if (result == null || result.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 新增商品
     * @param str
     * @return
     */
    @PostMapping("/save")
    public ResponseEntity<Void> saveGoods(@RequestBody String str) {
        JSONObject json = JSONObject.parseObject(str);
        SpuBo spuBo = new SpuBo();
        spuBo.setCid1(Long.valueOf((Integer) json.get("cid1")));
        spuBo.setCid2(Long.valueOf((Integer) json.get("cid2")));
        spuBo.setCid3(Long.valueOf((Integer) json.get("cid3")));
        spuBo.setBrandId(Long.valueOf((Integer) json.get("brandId")));
        spuBo.setSkus(JSON.parseArray(json.getString("skus"),Sku.class));
        spuBo.setSpuDetail(JSONObject.parseObject(json.getString("spuDetail"),SpuDetail.class));
        spuBo.setSubTitle((String)json.get("subTitle"));
        spuBo.setTitle((String)json.get("title"));
        try {
            this.goodsService.save(spuBo);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 编辑商品
     * @param str
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<Void> updateGoods(@RequestBody String str) {
        JSONObject json = JSONObject.parseObject(str);
        SpuBo spuBo = new SpuBo();
        spuBo.setId(Long.valueOf((Integer) json.get("id")));
        spuBo.setCid1(Long.valueOf((Integer) json.get("cid1")));
        spuBo.setCid2(Long.valueOf((Integer) json.get("cid2")));
        spuBo.setCid3(Long.valueOf((Integer) json.get("cid3")));
        spuBo.setBrandId(Long.valueOf((Integer) json.get("brandId")));
        spuBo.setSkus(JSON.parseArray(json.getString("skus"),Sku.class));
        spuBo.setSpuDetail(JSONObject.parseObject(json.getString("spuDetail"),SpuDetail.class));
        spuBo.setSubTitle((String)json.get("subTitle"));
        spuBo.setTitle((String)json.get("title"));
        try {
            this.goodsService.updateGoods(spuBo);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGoods(@PathVariable("id")Long id){
        if(id == null || id < 1){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            this.goodsService.deleteGoods(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id") Long id) {
        SpuDetail detail = this.goodsService.querySpuDetailById(id);
        if (detail == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long id) {
        List<Sku> skus = this.goodsService.querySkuBySpuId(id);
        if (skus == null || skus.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(skus);
    }

    @PostMapping("/changeSaleable/{id}/{option}")
    public ResponseEntity<Void> downGoods(@PathVariable("id") Long id,@PathVariable("option")Integer option) {
        if(id == null || id < 1){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            this.goodsService.changeSaleableById(id,option);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
        Spu spu = this.goodsService.querySpuById(id);
        if(spu == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(spu);
    }

}
