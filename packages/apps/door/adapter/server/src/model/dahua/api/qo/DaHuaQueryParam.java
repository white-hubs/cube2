package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.qo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DaHuaQueryParam {
    /**
     * url:http+ip+端口
     * 例子：http://192.168.1.1:8080
     */
    private String host;

}
