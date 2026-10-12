package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author wql
 * @describe 反馈详情
 * @date 2023/9/14 20:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FeedbackDetailVO {
    /**
     * 工程进度
     */
    private Integer schedule;
    /**
     * 验收结果
     */
    private String checkAcceptResult;
    /**
     * 操作类型
     */
    private String operationType;
    /**
     * 工程是否存在问题
     */
    private Boolean hasProblem;
    /**
     * 描述
     */
    private String describe;
    /**
     * 文件信息
     */
    private List<FileVO> files;
}
