package com.guhecloud.cube.srv.gen.securitymanagement.server.information.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DownloadMaterialsParam {

    /**
     * 节目Id集合
     */
    private List<String> thirdProgramIds;
}
