package com.guhecloud.cube.srv.gen.conferencemanage.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("conference_device")
public class ConferenceDevice extends BasicEnhancedTenantEntity<Long> {

    //主键id
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    //会议室id
    private Long conferenceId;
    //设备id
    private Long deviceId;
    //设备名称
    private String name;
    //产品分类
    private String type;
    //设备编码
    private String deviceCode;
    //设备型号
    private String deviceModel;
    //区域id
    private Long areaId;
    //区域名称
    private String areaName;

}
