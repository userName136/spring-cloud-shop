package com.leyou.goods.service.impl;

import com.leyou.goods.service.FileService;
import com.leyou.goods.service.GoodsPageService;
import com.leyou.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/17 16:34
 */
@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(GoodsPageServiceImpl.class);

    @Autowired
    private GoodsPageService goodsPageService;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("D:/Nginx/nginx-1.12.2/html/")
    private String destPath;

    @Override
    public void createHtml(Long id) throws Exception {
        PrintWriter writer = null;
        try {
            // 获取页面数据
            Map<String, Object> spuMap = this.goodsPageService.loadModel(id);

            // 创建thymeleaf上下文对象
            Context context = new Context();
            // 把数据放入上下文对象
            context.setVariables(spuMap);

            // 创建输出流
            File file = new File("D:\\Nginx\\nginx-1.12.2\\html\\item\\" + id + ".html");
            writer = new PrintWriter(file);

            // 执行页面静态化方法
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            logger.error("页面静态化出错：{}，"+ e, id);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private File createPath(Long id) {
        if (id == null) {
            return null;
        }
        File dest = new File(this.destPath);
        if (!dest.exists()) {
            dest.mkdirs();
        }
        return new File(dest, id + ".html");
    }


    @Override
    public boolean exists(Long id){
        return this.createPath(id).exists();
    }

    @Override
    public void syncCreateHtml(Long id){
        ThreadUtils.execute(() -> {
            try {
                createHtml(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
