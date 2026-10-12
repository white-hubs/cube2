package com.guhecloud.cube.srv.gen.loose.works.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @author wql
 * @describe 工程验收
 * @date 2023/9/15 14:25
 */
@Data
public class CheckAcceptParam {
    /**
     * 工程进度id
     */
    @NotBlank
    private String scheduleId;
    /**
     * 验收结果
     */
    @NotBlank
    private String acceptanceResult;
    /**
     * 验收说明
     */
    @Size(max = 200, message = "描述不能超过200字")
    private String description;
    /**
     * 说明文件
     */
    private List<String> fileIds;
}
