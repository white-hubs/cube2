package com.guhecloud.cube.srv.gen.product.market.server.pricing.vo;

import com.guhecloud.cube.srv.gen.product.market.server.common.vo.MarketGoodsCategoryVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Appet
 * @Description 采价任务
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPricingTaskVO {

    //
    private Long id;
    //采价计划ID
    private Long planId;
    //任务名称
    private String name;
    //紧急程度
    private String urgency;
    private String urgencyName;
    //任务描述
    private String taskDesc;
    //cron表达式
    private String cron;
    //任务状态
    private String status;
    //备注
    private String remark;
    //接受人
    private Long userId;
    private String userName;
    private String createUserName;
    //任务来源
    private String taskFrom;
    //创建时间
    private LocalDateTime createTime;

    private List<MarketGoodsCategoryVO> goodsCategoryList;

    private List<UserVO> userList;

    /**
     * 任务有效期
     */
    private String taskValidity;
}