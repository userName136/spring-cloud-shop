package com.leyou.search.pojo;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Yu, Zou
 * @version 1.0
 * @date 2022/2/10 15:56
 */
public class SearchResult implements Serializable {

    private String key;// 搜索条件

    private Integer page;// 当前页

    private String sortBy;//排序规则

    private Boolean descending;//是否降序

    private Map<String, String> filter;//过滤条件

    private static final Integer DEFAULT_SIZE = 20;// 每页大小，不从页面接收，而是固定大小

    private static final Integer DEFAULT_PAGE = 1;// 默认页

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if(page == null){
            return DEFAULT_PAGE;
        }
        // 获取页码时做一些校验，不能小于1
        return Math.max(DEFAULT_PAGE, page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return DEFAULT_SIZE;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getDescending() {
        return descending;
    }

    public void setDescending(Boolean descending) {
        this.descending = descending;
    }

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filters) {
        this.filter = filters;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "key='" + key + '\'' +
                ", page=" + page +
                ", sortBy='" + sortBy + '\'' +
                ", descending=" + descending +
                ", filter=" + filter +
                '}';
    }
}
