package com.guhecloud.cube.srv.gen.maintain.server.electron.vo;

import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PatrolRouteVO {
    private Long id;
    /**
     * 巡更路线名称
     */
    private String patrolRouteName;
    /**
     * 巡更点数量
     */
    private Integer patrolLocationNumber;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建人
     */
    private String createBy;
    private String creatorId;
    private ActorTypeEnum creatorType;
    /**
     * 巡更点信息
     */
    private List<PatrolLocationVO> patrolLocation;

}
