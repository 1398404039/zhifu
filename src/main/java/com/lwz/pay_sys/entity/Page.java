package com.lwz.pay_sys.entity;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Long totalSize;
    private Integer pages;
    private List<T> list;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getPages() {
        pages = (int) (totalSize / pageSize);
        int mod = (int) (totalSize % pageSize);
        if (mod > 0) {
            pages += 1;
        }
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Page() {
    }

    /**
     * 构建分页数据
     *
     * @param pageNum
     * @param pageSize
     * @param totalSize
     * @param list
     * @param <T>
     * @return
     */
    public static <T> Page<T> build(int pageNum, int pageSize, long totalSize, List<T> list) {
        Page<T> page = new Page<>();
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setTotalSize(totalSize);
        page.setList(list);
        return page;
    }
}
