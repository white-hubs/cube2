package com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 下载素材的vo类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DownloadMaterialVo {
    /**
     * 第三方节目id
     */
    private String thirdProgramId;

    /**
     * 素材
     */
    private List<MaterialVo> materialVos;
}
