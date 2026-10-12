package com.guhecloud.cube.biz.adapter.park.server.application;

import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayTotalQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingSubscribeParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayTotalVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import org.springframework.data.redis.core.RedisTemplate;

public abstract class AbstractParkingService implements ParkingService {

    @Override
    public ParkingPayTotalVO getParkingPayTotal(ParkingPayTotalQueryParam param) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public Boolean parkingSubscribe(ParkingSubscribeParam param) {
        RedisTemplate redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        //将订阅信息存入redis
        redisTemplate.opsForHash().put("parkSubscribe", param.getParkingSubscribeType().toString() + ":" +
                                                        SessionContextHolder.getRequester().getTenant(), param);
        return Boolean.TRUE;
    }

    @Override
    public Boolean parkingUnSubscribe(ParkingSubscribeParam param) {
        RedisTemplate redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        redisTemplate.opsForHash().delete("parkSubscribe", param.getParkingSubscribeType().toString() + ":" +
                                                           SessionContextHolder.getRequester().getTenant(), param);
        return Boolean.TRUE;
    }

}
