package com.leyou.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/13 9:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "tb_spu")
public class Spu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long brandId;
    /*1级类目*/
    private Long cid1;
    /*2级类目*/
    private Long cid2;
    /*3级类目*/
    private Long cid3;
    /*标题*/
    private String title;
    /*子标题*/
    private String subTitle;
    /*是否上架*/
    @Column(name = "saleable")
    private Boolean saleable;
    /*是否有效，逻辑删除用*/
    private Boolean valid;
    /*创建时间*/
    private Date createTime;
    /*最后修改时间*/
    private Date lastUpdateTime;

}
