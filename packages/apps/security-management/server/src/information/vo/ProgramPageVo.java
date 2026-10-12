package com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class ProgramPageVo {

    @JSONField(serializeFeatures = JSONWriter.Feature.WriteLongAsString)
    private Long id;
    /**
     * 第三方节目id
     */
    private String thirdProgramId;
    /**
     * 节目名称
     */
    private String programName;
    /**
     * 节目分类名称
     */
    private String categoryName;
}
