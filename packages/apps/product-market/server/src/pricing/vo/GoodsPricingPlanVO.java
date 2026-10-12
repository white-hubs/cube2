package com.guhecloud.cube.srv.gen.product.market.server.pricing.vo;

import com.guhecloud.cube.srv.gen.product.market.server.common.vo.MarketGoodsCategoryVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Appet
 * @Description 商品采价计划
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPricingPlanVO {

    //
    private Long id;
    //计划名称
    private String name;
    //紧急程度
    private String urgency;
    private String urgencyName;
    //计划描述
    private String planDesc;
    //开始时间
    private LocalDateTime startTime;
    //结束时间
    private LocalDateTime endTime;
    /**
     * 任务有效期
     */
    private String taskValidity;
    //发布类型
    private String publishType;
    private String publishTypeName;
    //时间段
    private String publishPeriod;
    //时间点
    private String publishTime;
    //cron表达式
    private String cron;
    //是否启用
    private Boolean enabled;
    //状态
    private String status;
    //备注
    private String remark;
    //创建人
    private String createUserName;
    private LocalDateTime createTime;

    private List<MarketGoodsCategoryVO> goodsCategoryList;

    private List<UserVO> userList;

}