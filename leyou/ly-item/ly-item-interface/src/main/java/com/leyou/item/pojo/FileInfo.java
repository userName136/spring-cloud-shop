package com.leyou.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/14 13:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "tb_fileInfo")
public class FileInfo {

    private Integer id;

    private String groupName;

    private String fileUrl;

    private String fileType;

    private Timestamp uploadDate;

}
