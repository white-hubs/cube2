package com.guhecloud.cube.srv.gen.product.market.server.pricing.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Appet
 * @Description 采价预警规则组
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPriceWarnRuleGroupVO {

    //
    private Long id;
    //告警规则组名称
    private String groupName;
    //创建人姓名
    private String creatorName;
    //tru开启 false关闭
    private Boolean status;
    //备注
    private String remark;
    //告警规则
    List<GoodsPriceWarnRuleVO> rules;

    //通知人员
    List<GoodsPriceWarnUserVO> users;
}