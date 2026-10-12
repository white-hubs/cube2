package com.guhecloud.cube.srv.gen.loose.works.dto;

import annotation.FormField;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/12 10:50
 * @Description:
 */

@Data
public class ProjectReformDTO {
    @FormField(label = "整改工程")
    private List<String> projectScheduleId;

}
