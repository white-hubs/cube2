package com.guhecloud.cube.srv.gen.conferencemanage.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.ParticipantEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("conference")
public class Conference extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 会议室名称
     */
    private String name;

    /**
     * 所属区域ID
     */
    private Long areaId;

    /**
     * 所属区域名
     */
    private String areaName;

    /**
     * 可容纳人数
     */
    private Integer capacity;

    /**
     * 可预约时间段
     */
    private String period;

    /**
     * 使用状态(默认0，可用；1，不可用)
     */
    private Integer status;
    /**
     * 管理员
     */
    private String administrators;

    /**
     * 会议室类型id
     */
    private Long conferenceRoomTypeId;

    /**
     * 可预约人员类型
     */
    private ParticipantEnum bookPersonType;

    /**
     * 可预约人员
     */
    private String bookPerson;
}
