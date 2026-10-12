package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wql
 * @describe 工程详情
 * @date 2023/9/14 19:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProjectDetailVO {
    private Long scheduleId;
    /**
     * 工程名称
     */
    private String projectName;
    /**
     * 请示
     */
    private ApplyDetail apply;
    /**
     * 签约
     */
    private ApplyDetail signing;
    /**
     * 反馈
     */
    private List<FeedbackDetail> feedbackDetailList;
    /**
     * 验收
     */
    private FeedbackDetail checkAccept;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class ApplyDetail {
        /**
         * 发起人
         */
        private String username;
        /**
         * 发起部门
         */
        private String depName;
        /**
         * 通过时间
         */
        private LocalDateTime passTime;
        /**
         * 工单编号
         */
        private String orderNumber;
        /**
         * 流程实例id
         */
        private String processInsId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FeedbackDetail {
        private Long id;
        /**
         * 反馈人
         */
        private String username;
        /**
         * 反馈时间
         */
        private LocalDateTime feedbackTime;
        /**
         * 进度
         */
        private Integer schedule;
        /**
         * 验收结果
         */
        private String result;
        /**
         * 反馈部门
         */
        private Long FeedbackDepId;
        private String FeedbackDepName;
        /**
         * 进度文件
         */
        private List<FileVO> problemFileList;
        /**
         * 描述
         */
        private String description;
        private String problemDes;
    }
}
