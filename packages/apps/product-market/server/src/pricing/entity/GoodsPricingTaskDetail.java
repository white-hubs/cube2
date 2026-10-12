package com.guhecloud.cube.srv.gen.product.market.server.pricing.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 采价任务详情
 * @date 2023-05-16 15:08:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GoodsPricingTaskDetail extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //采价任务ID
    private Long taskId;
    //商品品类IDS
    private String goodsCategoryIds;
    private String goodsCategoryNames;
    //采价人员IDS
    private String userIds;
    private String userNames;
    //接受人员ID
    private Long userId;
    //接收人员名称
    private String userName;
    //采价员联系电话
    private String userPhone;
    //催促接受任务的时间
    private LocalDateTime noticeTime;

    public GoodsPricingTaskDetail(Long taskId, String goodsCategoryIds, String userIds) {
        this.taskId = taskId;
        this.goodsCategoryIds = goodsCategoryIds;
        this.userIds = userIds;
    }
}