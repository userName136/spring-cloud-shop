package com.leyou.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/6 15:07
 */
@Component
public interface UploadService {

    /**
     * 图片上传
     * @param file
     * @return
     */
    String upload(MultipartFile file);

}
