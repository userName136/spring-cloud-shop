package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/5 15:41
 */
@Component
public interface BrandMapper extends Mapper<Brand>, SelectByIdListMapper<Brand,Long> {

    /**
     * 新增商品分类和品牌中间表数据
     * @param cid 商品分类id
     * @param bid 品牌id
     * @return
     */
    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    /**
     * 根据bId删除商品分类和品牌中间表数据
     * @param bId
     * @return
     */
    @Delete("delete from tb_category_brand where brand_id=#{bId}")
    int deleteCategoryBrand(@Param("bId") Long bId);

    /**
     * 删除品牌
     * @param bId
     * @return
     */
    @Delete("delete from tb_brand where id=#{bId}")
    int deleteBrandById(@Param("bId")Long bId);

    /**
     * 根据分类查询品牌
     * @param cid
     * @return
     */
    @Select("SELECT b.* FROM tb_brand b LEFT JOIN tb_category_brand cb ON b.id = cb.brand_id WHERE cb.category_id = #{cid}")
    List<Brand> queryByCategoryId(Long cid);
}
