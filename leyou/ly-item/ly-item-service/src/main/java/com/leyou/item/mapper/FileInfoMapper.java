package com.leyou.item.mapper;

import com.leyou.item.pojo.FileInfo;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/14 14:06
 */
@Component
@Repository
public interface FileInfoMapper extends Mapper<FileInfo> {

    /**
     * 保存上传文件的信息
     * @param fileInfo
     * @return
     */
    @Insert("insert tb_fileInfo values(#{id},#{groupName},#{fileUrl},#{fileType},#{uploadDate},0)")
    int saveFileInfo(FileInfo fileInfo);

}
