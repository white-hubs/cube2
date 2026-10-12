package com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 节目单详情 实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("program_schedule")
public class ProgramScheduleEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 终端id
     */
    private String clientId;
    /**
     * 终端名称
     */
    private String clientName;
    /**
     * 第三方节目单id，和发送id是同一个概念
     */
    private String thirdPartyId;
    /**
     * 第三方节目id
     */
    private String thirdProgramId;
    /**
     * 节目名称
     */
    private String name;
    /**
     * 开始日期
     */
    private LocalDate beginDate;
    /**
     * 结束日期
     */
    private LocalDate endDate;
    /**
     * 开始时间
     */
    private String beginTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 有效星期 1-6分别表示星期一至星期六 ，0:表示星期天
     */
    private String weeks;
    /**
     * 优先级 0普通 2插播
     */
    private String priority;
    /**
     * 发送状态0表示发送不存在,1:等待下载,2:下载中,3：下载失败,4：播放中,5:播放完成,6：等待播放,7:已取消
     */
    private String state;
    /**
     * 节目单类型normal插播节目、text插播文字，reserved预留节目
     */
    private String type;
}
