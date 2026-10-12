package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.config;

import com.guhecloud.cube.biz.fusion.communicate.server.third.base.config.model.ProviderConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XunYiConfig extends ProviderConfig {

    /**
     * 接口密钥
     */
    private String apiKey;

    /**
     * 服务地址
     */
    private String serverApiAddr;

    /**
     * 是否录音：0-否 1-是 (默认 0)
     */
    private Boolean isRecord;

    /**
     * 是否录像：0-否 1-是 (默认 0)
     */
    private Boolean isVideo;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        XunYiConfig that = (XunYiConfig) o;
        return apiKey.equals(that.apiKey) &&
               serverApiAddr.equals(that.serverApiAddr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), apiKey, serverApiAddr);
    }

}
