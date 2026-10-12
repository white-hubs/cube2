package com.guhecloud.cube.base.dataintegrate.server.entity;

import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 隐患数据采集
 * @date 2023-09-12 15:08:36
 */
@Data
public class SafetyHiddenDanger {

    //
    private Long id;
    //隐患单名称
    private String name;
    //隐患分类
    private String hiddenDangerCategory;
    //隐患状态
    private String hiddenDangerStatus;
    //隐患级别
    private String levelName;
    //隐患描述
    private String remark;
    //市场租户
    private Integer marketTenant;
    //市场名称
    private String marketName;

    private Integer tenant;

    private String createBy;

    private String creatorId;

    private ActorTypeEnum creatorType;

    private LocalDateTime createTime;

    private String updateBy;

    private String updaterId;

    private ActorTypeEnum updaterType;

    private LocalDateTime updateTime;

    private Boolean deleted;
}