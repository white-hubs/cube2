package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-11-11  13:58
 * @Description:
 * @Version: 1.0
 */
@Data
public class ProjectFeedbackVO {
    private Long id;
    /**
     * 进度id
     */
    private Long scheduleId;
    /**
     * 进度
     */
    private Integer schedule;
    /**
     * 操作类型
     */
    private String operationType;
    /**
     * 验收结果
     */
    private String acceptanceResult;
    /**
     * 描述
     */
    private String description;
    /**
     * 文件
     */
    private String files;
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
    private String problemFiles;
    /**
     * 进度文件
     */
    private List<FileVO> problemFileList;
}
