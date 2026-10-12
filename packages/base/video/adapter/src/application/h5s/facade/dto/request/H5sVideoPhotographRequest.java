package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class H5sVideoPhotographRequest extends BasicRequest {

    /**
     * token不能为空
     */
    private String token;

    /**
     * 快照文件名，如果空，自动生成文件名，文件名不用带文件扩展
     */
    private String filename;

    /**
     * 根路径
     */
    private String basePath;

    /**
     * 开始时间年月日
     */
    private String start1;

    /**
     * 开始时间时分秒
     */
    private String start2;

    @Override
    public String getUrl() {
        return getUrl("/api/v1/PathSnapshot")
                .append("?token={token}")
                .append("&filename={filename}")
                .append("&basePath={basePath}")
                .append("&start1={start1}")
                .append("&start2={start2}")
                .append("&session={session}")
                .build();
    }
}
