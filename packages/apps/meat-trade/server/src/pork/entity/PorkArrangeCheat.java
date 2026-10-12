package com.guhecloud.cube.biz.meattrade.server.pork.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@TableName(value = "meattrade_pork_arrange_cheat")
@AllArgsConstructor
@NoArgsConstructor
public class PorkArrangeCheat extends BasicEnhancedTenantEntity<Long> {

    public Long id;

    /**
     * 理货详情主键
     */
    private Long porkArrangeDetailId;

    /**
     * 作弊状态
     */
    private Boolean cheat;
}
