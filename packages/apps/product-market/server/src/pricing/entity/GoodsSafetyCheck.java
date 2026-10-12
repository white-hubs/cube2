package com.guhecloud.cube.srv.gen.product.market.server.pricing.entity;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 食品安全检查
 * @date 2023-12-04 10:08:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GoodsSafetyCheck extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //档位号
    private String stallCode;
    //品类名
    private String goodsCategoryName;
    //产地
    private String goodsFrom;
    //检测日期
    private LocalDateTime checkDate;
    //检测项
    private String checkItem;
    //检测结果：检测结果（POS 阳性、NEG 阴性）
    private String checkResult;
    //检查人名称
    private String checkerName;

    private String sampleCode;

    public GoodsSafetyCheck(String stallCode, String goodsCategoryName, String goodsFrom, LocalDateTime checkDate, String checkItem,
                            String checkResult, String sampleCode
                           ) {
        var currentTime = LocalDateTime.now();
        this.stallCode = stallCode;
        this.goodsCategoryName = goodsCategoryName;
        this.goodsFrom = goodsFrom;
        this.checkDate = checkDate;
        this.checkItem = checkItem;
        this.checkResult = checkResult;
        this.sampleCode = sampleCode;
        this.setTenant(SessionContextHolder.getRequester().getTenant());
        this.setDeleted(false);
        EntityAuditor.populateAudit(this);
    }

    public GoodsSafetyCheck(String stallCode, String goodsCategoryName, String goodsFrom, LocalDateTime checkDate, String checkItem,
                            String checkResult, String sampleCode, String checkerName
                           ) {
        this.stallCode = stallCode;
        this.goodsCategoryName = goodsCategoryName;
        this.goodsFrom = goodsFrom;
        this.checkDate = checkDate;
        this.checkItem = checkItem;
        this.checkResult = checkResult;
        this.sampleCode = sampleCode;
        this.checkerName = checkerName;
        this.setDeleted(false);
        EntityAuditor.populateAudit(this);
    }
}