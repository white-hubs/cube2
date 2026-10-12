package com.guhecloud.cube.gm.meattrade.plugin.couple.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.gm.meattrade.plugin.couple.domain.enumeration.CoupleStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("couple")
public class CouplePO extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 挂钩编号
     */
    private String code;

    /**
     * 状态
     */
    private CoupleStatusEnum status;

    /**
     * 上报时间
     */
    private LocalDateTime reportTime;
}
