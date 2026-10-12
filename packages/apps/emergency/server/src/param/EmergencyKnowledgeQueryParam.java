package com.guhecloud.cube.biz.emergency.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 应急知识（技术）管理
 * @date 2024-01-26 10:08:36
 */
@Data
public class EmergencyKnowledgeQueryParam extends Paging {

    //知识名称
    private String name;
    //知识备注
    private String remark;
    //文件地址(冗余：存放三方的文件地址)
    private String filePath;
    //
    private String fileIds;

}