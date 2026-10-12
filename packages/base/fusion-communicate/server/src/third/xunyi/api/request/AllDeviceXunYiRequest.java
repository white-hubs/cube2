package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.constants.XunYiApiDocConstant.ALL_DEVICE_URI;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllDeviceXunYiRequest extends AbstractXunYiRequest {

    /**
     * 页码
     */
    private Integer curPage;

    /**
     * 页大小
     */
    private Integer pageRows;

    @Override
    public String getUrl() {
        return getUrl(ALL_DEVICE_URI)
                .append("?curPage={curPage}")
                .append("&pageRows={pageRows}").toString();
    }
}
