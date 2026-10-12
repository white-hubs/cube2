package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.constants.XunYiApiDocConstant.UPDATE_DEVICE_URI;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveXunYiAccountRequest extends AbstractXunYiRequest {

    /**
     * 账号
     */
    private XunYiAccount account;

    @Override
    public String getUrl() {
        return getUrl(UPDATE_DEVICE_URI).toString();
    }
}
