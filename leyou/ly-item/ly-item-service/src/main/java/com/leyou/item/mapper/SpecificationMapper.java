package com.leyou.item.mapper;

import com.leyou.item.pojo.Specification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/12 16:25
 */
@Component
public interface SpecificationMapper extends Mapper<Specification> {

    /**
     * 查询id的数据是否存在
     * @param id
     * @return
     */
    @Select("select category_id from tb_specification where category_id=#{id}")
    Long queryCIdBySpecId(@Param("id")Long id);

    /**
     * 添加商品列表信息
     * @param cId
     * @param specifications
     * @return
     */
    @Insert("insert tb_specification values(#{cId},#{spec})")
    int insertSpecification(@Param("cId")Long cId, @Param("spec")String specifications);

    /**
     * 修改商品列表信息
     * @param cId
     * @param specifications
     * @return
     */
    @Update("update tb_specification set specifications=#{spec} where category_id=#{cId}")
    int updateSpecification(@Param("cId")Long cId, @Param("spec")String specifications);
}
