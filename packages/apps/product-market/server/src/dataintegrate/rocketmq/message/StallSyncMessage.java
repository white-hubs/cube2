package com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.rocketmq.message;

import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketStall;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 档位同步消息
 *
 * @author appet
 * @date 2021/9/10 14:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StallSyncMessage {

    public static final String TOPIC = "STALL-SYNC";

    /**
     * 档位列表
     */
    private List<MarketStall> stallList;

    /**
     * 删除的时候需要标识 DEL
     * 新增和修改的时候无须标识
     */
    private String OptFlag;
}
