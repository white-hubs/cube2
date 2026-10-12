package com.guhecloud.cube.biz.linkage.server.facade.iot.repository.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 物联事件消息
 *
 * @author lanchun
 * @date 2023/3/27 18:44
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IotEventConsume extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 物联网事件实例主键
     */
    private String iotEventInstanceId;

    /**
     * 附件
     */
    private String additional;

    /**
     * 是否消费
     */
    private Boolean consumed;

}
