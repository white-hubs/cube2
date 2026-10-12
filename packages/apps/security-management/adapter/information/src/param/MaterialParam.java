package com.guhecloud.cube.srv.gen.adapter.information.server.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 获取节目单列表 请求入参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialParam {

    //节目Id集合
    private List<String> programIds;
}
