package com.guhecloud.cube.biz.fusion.communicate.server.third.tencent;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.IFusionCommunicateRoom;
import com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.api.TencentRealTimeCommunicationApiService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.api.request.DisbandRoomRequest;
import com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.api.request.RemoveUserRequest;
import com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.api.response.DisbandRoomResponse;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TencentCommunicateRoomProvider implements IFusionCommunicateRoom {

    private final RedisTemplate<String, String> redisTemplate;

    private final TencentRealTimeCommunicationApiService tencentRealTimeCommunicationApiService;

    private static final String ROOM_KEY_PREFIX = "tencent:trtc:room:";
    private static final int MAX_RETRIES = 3;
    private static final long ROOM_TTL = 60 * 12; // 单位：分钟

    public TencentCommunicateRoomProvider(RedisTemplate<String, String> redisTemplate,
                                          TencentRealTimeCommunicationApiService tencentRealTimeCommunicationApiService
                                         ) {
        this.redisTemplate = redisTemplate;
        this.tencentRealTimeCommunicationApiService = tencentRealTimeCommunicationApiService;
    }

    @Override
    public String getProviderCode() {
        return "tencent";
    }

    @Override
    public String createRoom() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        for (int i = 0; i < MAX_RETRIES; i++) {
            String roomId = IdWorker.getIdStr();
            String key = ROOM_KEY_PREFIX + roomId;
            Boolean success = ops.setIfAbsent(
                    key,
                    "active",
                    ROOM_TTL,
                    TimeUnit.MINUTES);

            if (Boolean.TRUE.equals(success)) {
                return roomId;
            }
        }
        throw new ServiceException("房间号生成失败，超过最大重试次数");
    }

    @Override
    public void joinConferenceRoom(String roomNumber, Set<Long> userIds) {
        //do nothing
    }

    @Override
    public void leaveConferenceRoom(String roomNumber, Set<Long> userIds) {
        //do nothing
    }

    @Override
    public void removeConferenceRoomUsers(String roomNumber, Set<Long> userIds) {
        RemoveUserRequest request = new RemoveUserRequest();
        request.setRoomId(roomNumber);
        request.setUserIds(userIds.stream().map(String::valueOf).collect(Collectors.toSet()));
        tencentRealTimeCommunicationApiService.removeUser(request);
    }

    @Override
    public void disbandRoomRequest(String roomNumber) {
        DisbandRoomRequest request = new DisbandRoomRequest();
        request.setRoomId(roomNumber);
        DisbandRoomResponse disbandRoomResponse = tencentRealTimeCommunicationApiService.disbandRoom(request);
        if (StringUtils.isNotBlank(disbandRoomResponse.getRequestId())) {
            String key = ROOM_KEY_PREFIX + roomNumber;
            redisTemplate.delete(key);
        }
    }
}
