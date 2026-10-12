package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片查询-返回URL 请求
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudPicSearchRequest {
    private String pno;
    private String tn;
    private String ts;
    private String sn;
    private String ve;
    /** 业务参数 JSON 字符串，格式如 [{"filePath":"/path/to/image"}] */
    private String dataItems;
}
