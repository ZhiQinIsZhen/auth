package com.liyz.auth.common.base.result;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.collect.Lists;
import com.liyz.auth.common.remote.exception.CommonExceptionCodeEnum;
import com.liyz.auth.common.remote.exception.IExceptionCodeService;
import com.liyz.auth.common.remote.page.Page;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 注释:分页消息体
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2019/8/30 10:45
 */
@Getter
@Setter
@JsonPropertyOrder({"code", "message", "total", "pages", "pageNum", "pageSize", "hasNextPage", "data"})
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 491504189274405094L;

    private String code;

    private String message;

    private Long total;

    private Integer pages;

    private Integer pageNum;

    private Integer pageSize;

    private Boolean hasNextPage;

    private List<T> data;

    public static <T> PageResult<T> success(org.springframework.data.domain.Page<T> data) {
        return new PageResult<>(data);
    }

    public static <T> PageResult<T> success(Page<T> data) {
        return new PageResult<>(data);
    }

    public static <T> PageResult<T> error(String code, String message) {
        return new PageResult<T>(code, message);
    }

    public static <T> PageResult<T> error(IExceptionCodeService codeEnum) {
        return new PageResult<T>(codeEnum.getCode(), codeEnum.getMessage());
    }

    public PageResult() {}

    public PageResult(org.springframework.data.domain.Page<T> data) {
        boolean isNull = data == null;
        this.setData(isNull ? Lists.newArrayList() : data.getContent());
        this.total = isNull ? 0L : data.getTotalElements();
        this.pages = isNull ? 0 : data.getTotalPages();
        this.hasNextPage = isNull ? false : data.hasNext();
        this.pageNum = isNull ? 0 : data.getNumber();
        this.pageSize = isNull ? 0 : data.getSize();
        this.code = CommonExceptionCodeEnum.success.getCode();
        this.message = CommonExceptionCodeEnum.success.getMessage();
    }

    public PageResult(Page<T> data) {
        boolean isNull = data == null;
        this.setData(isNull ? Lists.newArrayList() : data.getList());
        this.total = isNull ? 0L : data.getTotal();
        this.pages = isNull ? 0 : data.getPages();
        this.hasNextPage = isNull ? false : data.getHasNextPage();
        this.pageNum = isNull ? 0 : data.getPageNum();
        this.pageSize = isNull ? 0 : data.getPageSize();
        this.code = CommonExceptionCodeEnum.success.getCode();
        this.message = CommonExceptionCodeEnum.success.getMessage();
    }

    public PageResult(String code, String message) {
        this.code = code;
        this.message = message;
        this.total = 0L;
        this.pages = 0;
        this.pageNum = 0;
        this.pageSize = 0;
        this.hasNextPage = false;
    }
}
