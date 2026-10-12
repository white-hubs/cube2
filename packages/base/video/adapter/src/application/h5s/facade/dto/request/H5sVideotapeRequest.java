package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class H5sVideotapeRequest extends BasicRequest {

    /**
     * token
     */
    private String token;

    /**
     * 开始时间年月日
     */
    private String start1;

    /**
     * 开始时间时分秒
     */
    private String start2;

    /**
     * 录像时长
     */
    private String duration;

    /**
     * 8 或者-8 如果没有此参数默认使用服务器所在的时区
     */
    private String tz;

    /**
     * 录像文件名，文件名不用带文件扩展
     */
    private String filename;

    /**
     * true 或者 false,默认 flase，是否永远创建新文件，如果没有该选项，如果录像正在进行，则会在原来的基础上延长对应的时长
     */
    private String alwayscreate;

    /**
     * 根路径
     */
    private String basePath;

    @Override
    public String getUrl() {
        return getUrl("/api/v1/PathRecord")
                .append("?token={token}")
                .append("&start1={start1}")
                .append("&start2={start2}")
                .append("&duration={duration}")
                .append("&tz={tz}")
                .append("&filename={filename}")
                .append("&alwayscreate={alwayscreate}")
                .append("&basePath={basePath}")
                .append("&session={session}")
                .build();
    }
}
