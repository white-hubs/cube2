package com.guhecloud.cube.gm.meattrade.plugin.tally.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.gm.meattrade.plugin.tally.domain.enumeration.TallyPushStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 理货数据推送信息
 *
 * @author lanchunimplements Creatable<Long>, Updatable<Long>
 * @date 2023/7/25 10:14
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("tally_push")
public class TallyPushPO extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 批次号
     */
    private String tallyNo;

    /**
     * 推送内容
     */
    private String pushContent;

    /**
     * 推送状态
     */
    private TallyPushStatusEnum pushStatus;
}
