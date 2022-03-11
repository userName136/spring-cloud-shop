package com.leyou.item.controller;

import com.leyou.item.pojo.FileInfo;
import com.leyou.item.pojo.PageResult;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.service.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/14 17:05
 */
@Controller
@RequestMapping("/fileInfo")
public class FileInfoController {

    @Autowired
    private FileInfoService fService;

    @GetMapping("/queryFileInfo")
    public ResponseEntity<PageResult<FileInfo>> queryFileInfo(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc
    ){
        PageResult<FileInfo> result = fService.queryFileInfo(page,rows,sortBy,desc);
        if (result == null || result.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

}
