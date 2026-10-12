package com.guhecloud.cube.base.flow.flowable.task.interfaces.dto;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-06-06  09:39
 * @Description:
 * @Version: 1.0
 */
@Data
public class FormPermsDTO {
    private String id;
    /**
     * 是否必填：E=编辑
     */
    private String perm;
    /**
     * 是否必填
     */
    private Boolean required;
    private String title;
}