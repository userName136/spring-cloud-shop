package com.leyou.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/13 10:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SpuBo extends Spu {

    List<Sku> skus;// sku列表

    SpuDetail spuDetail;// 商品详情

    String cname;

    String bname;

}
