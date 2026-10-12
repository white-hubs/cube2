package com.guhecloud.cube.biz.ouaa.server.third.common.entity;

import com.guhecloud.cube.biz.ouaa.server.third.common.enumeration.ThirdTypeEnum;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ThirdUserRef {

    private Long id;

    /**
     * 三方账号类型(微信等其它三方)
     */
    private ThirdTypeEnum type;

    /**
     * 三方账号配置id
     */
    private Long configId;

    /**
     * 系统用户id
     */
    private Long userId;

    /**
     * 关联的第三方账号id
     */
    private Long refId;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人主键
     */
    private String creatorId;

    /**
     * 创建人类型
     */
    private ActorTypeEnum creatorType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String updateBy;

    /**
     * 修改人主键
     */
    private String updaterId;

    /**
     * 修改人类型
     */
    private ActorTypeEnum updaterType;

    /**
     * 创建时间
     */
    private LocalDateTime updateTime;

    public ThirdUserRef(Long id, ThirdTypeEnum type, Long configId, Long userId, Long refId, Integer tenant) {
        this.id = id;
        this.type = type;
        this.configId = configId;
        this.userId = userId;
        this.refId = refId;
        this.tenant = tenant;
    }
}
