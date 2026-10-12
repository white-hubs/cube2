package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户培训签到记录
 *
 * @author lanchun
 * @date 2024/4/12 9:27
 */
@Data
public class CultivateUserSignRecordVO {

    private Long id;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 市场信息
     */
    private CultivateMarketInfoVO market;

    /**
     * 签到信息
     */
    private CultivateSignStatusInfoVO signInfo;

    /**
     * 培训任务信息
     */
    private CultivateTaskInfoVO cultivateTask;

    /**
     * 考试市场信息
     *
     * @author lanchun
     * @date 2024/4/12 9:30
     */
    @Data
    public static class CultivateMarketInfoVO {

        /**
         * 租户编号
         */
        private Integer marketTenant;

        /**
         * 租户名称
         */
        private String marketName;
    }

    /**
     * 用户打卡信息
     *
     * @author lanchun
     * @date 2024/4/12 9:31
     */
    @Data
    public static class CultivateSignStatusInfoVO {

        /**
         * 签到时间
         */
        private LocalDateTime signDate;

        /**
         * 签到状态
         */
        private String status;

        /**
         * 是否迟到
         */
        private Boolean belated;
    }

    /**
     * 培训任务信息
     *
     * @author lanchun
     * @date 2024/4/12 9:33
     */
    @Data
    public static class CultivateTaskInfoVO {

        /**
         * 任务名称
         */
        private String name;

        /**
         * 培训形式(线上、线下)
         */
        private String cultivateMode;

        /**
         * 来源主键
         */
        private String sourceKey;

        /**
         * 培训开始时间
         */
        private LocalDateTime startTime;

        /**
         * 培训结束时间
         */
        private LocalDateTime endTime;

        /**
         * 主题名称
         */
        private String themeName;

        /**
         * 负责人名称
         */
        private String chargeUserName;
    }
}
