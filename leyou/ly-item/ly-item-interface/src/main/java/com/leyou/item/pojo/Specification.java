package com.leyou.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/1/12 16:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "tb_specification")
public class Specification {

    @Id
    private Long categoryId;

    private String specifications;

}
