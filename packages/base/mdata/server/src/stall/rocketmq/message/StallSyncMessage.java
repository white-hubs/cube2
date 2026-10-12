package com.guhecloud.cube.biz.mdata.server.stall.rocketmq.message;

import com.guhecloud.cube.biz.mdata.server.stall.entity.Stall;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<Stall> stallList;

    /**
     * 删除的时候需要标识 DEL
     * 新增和修改的时候无须标识
     */
    private String OptFlag;

    public static StallSyncMessage buildDelStallMessage(List<Long> ids) {
        StallSyncMessage stallSyncMessage = new StallSyncMessage();
        if (!CollectionUtils.isEmpty(ids)) {
            stallSyncMessage.setOptFlag("DEL");
            List<Stall> stalls = ids.stream().map(id -> {
                Stall stall = new Stall();
                stall.setId(id);
                return stall;
            }).collect(Collectors.toList());
            stallSyncMessage.setStallList(stalls);
            return stallSyncMessage;
        }
        return stallSyncMessage;
    }
}
