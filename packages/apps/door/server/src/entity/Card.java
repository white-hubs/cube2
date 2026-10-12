package com.guhecloud.cube.biz.door.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门禁卡
 */
@Data
public class Card extends BasicEnhancedTenantEntity<String> {
    private String id;
    private String cardNo;
    private String personId;
    private String personName;
    private String useStatus;//使用状态 1 正常 2 挂失
    private LocalDateTime startDate;//生效日期
    private LocalDateTime endDate;//失效日期
    private LocalDateTime lossDate;//挂失开始日期
    private LocalDateTime unlossDate;//解除挂失日期
    private String remark;//卡描述
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
    private String cardType;//卡类型
    private String cardTypeName;
    private String orgId;//组织ID
    private Boolean isBindFinger;//是否绑定指纹
    private Boolean isBindFace;//是否绑定人脸
}
