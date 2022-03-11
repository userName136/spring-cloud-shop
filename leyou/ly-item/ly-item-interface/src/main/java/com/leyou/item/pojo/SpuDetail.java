package com.leyou.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/13 10:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="tb_spu_detail")
public class SpuDetail implements Serializable {
    @Id
    private Long spuId;// 对应的SPU的id
    private String description;// 商品描述
    private String specTemplate;// 商品特殊规格的名称及可选值模板
    private String specifications;// 商品的全局规格属性
    private String packingList;// 包装清单
    private String afterService;// 售后服务

}
