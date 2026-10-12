package com.guhecloud.cube.srv.gen.loose.works.dto.request.param;

import com.guhecloud.cube.srv.gen.loose.works.enumeration.ProjectInspectResultEnum;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/11 17:31
 * @Description:
 */
@Data
public class ProjectInspectRecordParam {

    /**
     * 工程id
     */
    private Long projectScheduleId;

    /**
     * 巡查结果
     */
    private ProjectInspectResultEnum result;
    /**
     * 备注
     */
    private String remarks;

    /**
     * 图片
     */
    private List<Long> image;
}
