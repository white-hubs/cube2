package com.guhecloud.cube.biz.meattrade.server.third.request.statistics.base;

import com.guhecloud.cube.biz.meattrade.server.third.request.AbstractThirdRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class StatQueryRequest extends AbstractThirdRequest {

    /**
     * 时间类别
     */
    private String type;

    /**
     * 页码
     */
    private Integer pageNumber = 1;

    /**
     * 页大小
     */
    private Integer pageSize = 10;

    protected StatQueryRequest() {}

    public String getUrl() {
        return getApiUrl() + "?type={type}&pageNumber={pageNumber}&pageSize={pageSize}";
    }

    protected abstract String getApiUrl();
}
