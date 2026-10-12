package com.guhecloud.cube.base.message2.server.base.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发送终端
 *
 * @author lanchun
 * @date 2023/5/16 11:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgRecordTerminal extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 消息记录主键
     */
    private Long msgRecordId;

    /**
     * 终端编号
     */
    private String terminal;

    /**
     * 终端名称
     */
    private String terminalName;
}
