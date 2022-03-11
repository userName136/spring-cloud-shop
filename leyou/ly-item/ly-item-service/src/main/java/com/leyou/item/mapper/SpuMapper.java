package com.leyou.item.mapper;

import com.leyou.item.pojo.Spu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/13 10:09
 */
@Component
public interface SpuMapper extends Mapper<Spu> {

    /**
     * 修改商品上/下架的状态
     * @param id
     * @param option
     */
    @Update("update tb_spu set saleable=#{option} where id=#{id}")
    void updateSaleableById(@Param("id") Long id,@Param("option") Integer option);
}
