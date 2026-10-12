package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 采价任务详情
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPricingTaskDetailQueryParam extends Paging {

    //采价任务ID
    private Long taskId;
    //商品品类ID
    private Long goodsCategoryId;
    //品类父级IDS
    private String goodsCategoryIds;
    //采价人员IDS
    private String userIds;
    //接受人员ID
    private Long userId;
    //接收人员名称
    private String userName;
    //采价员联系电话
    private String userPhone;

}