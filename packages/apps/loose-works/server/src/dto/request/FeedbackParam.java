package com.guhecloud.cube.srv.gen.loose.works.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @author wql
 * @describe 工程进度反馈
 * @date 2023/9/15 14:15
 */
@Data
public class FeedbackParam {
    /**
     * 工程进度id
     */
    @NotBlank
    private String scheduleId;
    /**
     * 工程进度
     */
    @NotBlank
    private String schedule;
    /**
     * 进度描述
     */
    @Size(max = 500, message = "描述不能超过500字")
    private String description;
    /**
     * 说明文件
     */
    private List<String> fileIds;
    /**
     * 工程是否存在问题
     */
    private Boolean hasProblem;
    /**
     * 问题描述
     */
    private String problemDes;
    /**
     * 问题文件
     */
    private List<String> problemFileIds;
}
