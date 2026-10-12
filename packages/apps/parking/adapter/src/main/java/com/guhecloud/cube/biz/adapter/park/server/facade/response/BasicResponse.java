package com.guhecloud.cube.biz.adapter.park.server.facade.response;

import com.guhecloud.cube.biz.adapter.park.server.application.bosnia.entity.BosniaConfig;
import com.guhecloud.cube.biz.adapter.park.server.facade.model.Head;
import com.guhecloud.cube.biz.adapter.park.server.facade.model.HeadResponse;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.Data;
import lombok.ToString;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Data
@ToString
public class BasicResponse<T> {
    private List<HeadResponse> head;
    private T body;

    public boolean isFail() {
        if (CollectionUtils.isEmpty(this.head) || !this.head.get(0).getRcode().equals("0000")) {
            return true;
        }
        return false;
    }

    public boolean isSuccess() {
        if (!CollectionUtils.isEmpty(this.head) && this.head.get(0).getRcode().equals("0000")) {
            return true;
        } else {
            return false;
        }
    }

    public BasicResponse() {
    }

    public BasicResponse(List<HeadResponse> head) {
        this.head = head;
    }

    public static BasicResponse buildBasicResponse(String mcode, Integer tenant, String manufacturer) {
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
        SubsystemAdapterConfig subsystemAdapterConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(
                SubsystemTypeConstants.PARKING_SERVER + ":" + manufacturer + ":" + tenant);
        BosniaConfig bosniaConfig = JsonUtils.deserialize(JsonUtils.serialize(subsystemAdapterConfig.getManufacturerJson()), BosniaConfig.class);
        Head head = new HeadResponse(mcode, "0001", "10", "11", bosniaConfig.getAppSecret());
        BasicResponse basicResponse = new BasicResponse(Collections.singletonList(head));
        return basicResponse;
    }
}
