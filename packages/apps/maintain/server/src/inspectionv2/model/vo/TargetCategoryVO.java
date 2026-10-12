package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-14  11:00
 * @Description:
 * @Version: 1.0
 */
@Data
@Builder
public class TargetCategoryVO {
    /**
     * 对象类型
     */
    private String targetCategory;
    /**
     * 对象类型
     */
    private List<TargetCategoryDetailsVO> details;
}
