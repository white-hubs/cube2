package com.guhecloud.cube.srv.gen.park.management.server.validator;

import com.guhecloud.cube.srv.gen.park.management.server.enumeration.RoomRentRecordStatusEnum;
import com.guhecloud.cube.srv.gen.park.management.server.enumeration.RoomStatusEnum;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomDeletedVO;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomRentRecordPO;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomRentRecordRoomPO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.RoomQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.RoomRentRecordQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.service.RoomRentRecordRoomService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomRentRecordValidator {

    private final RoomRentRecordQueryService roomRentRecordQueryService;
    private final RoomQueryService roomQueryService;
    private final RoomRentRecordRoomService rentRecordRoomService;

    /**
     * 房间是否已被选择
     *
     * @param roomIds 房间IDS
     */
    public void verifyIsExistByRoomIds(List<Long> roomIds, LocalDateTime beginTime, LocalDateTime endTime) {
        List<RoomDeletedVO> roomDeletedVOS = roomQueryService.tenantRoomByIds(roomIds);
        List<RoomDeletedVO> roomDeletedVOS1 = roomQueryService.tenantStopRoomById(roomIds);
        roomDeletedVOS.forEach(s -> Assert.isNotAcceptable(s.getDeleted() == false, s.getName() + " 该房间已被删除"));

        roomDeletedVOS1.forEach(s -> Assert.isNotAcceptable(RoomStatusEnum.STOPPING.name().equals(s.getStatus()),
                                                            s.getName() + " 该房间被停用"));

        // 根据房间ID，获得所关联的租赁记录ID
        List<RoomRentRecordRoomPO> recordRoomPOS = rentRecordRoomService.getListByRoomIds(roomIds);
        if (CollectionUtils.isEmpty(recordRoomPOS)) {
            return;
        }
        Set<Long> roomRentRecordIds = recordRoomPOS.stream().map(RoomRentRecordRoomPO::getRecordId).collect(Collectors.toSet());
        Map<Long, String> roomIdNameMap = recordRoomPOS.stream()
                .collect(Collectors.toMap(RoomRentRecordRoomPO::getRoomId, RoomRentRecordRoomPO::getRoomName, (v1, v2) -> v1));

        // 获取租赁记录
        List<RoomRentRecordPO> recordPOS = roomRentRecordQueryService.lambdaQuery()
                .in(RoomRentRecordPO::getId, roomRentRecordIds)
                .ne(RoomRentRecordPO::getStatus, RoomRentRecordStatusEnum.EXPIRE)
                .list();
        Optional.ofNullable(recordPOS).orElse(Collections.emptyList())
                .forEach(po -> {

                    boolean beginTimeError = beginTime.isBefore(po.getBeginTime()) || beginTime.isAfter(po.getEndTime());
                    boolean endTimeError = endTime.isBefore(po.getBeginTime()) || endTime.isAfter(po.getEndTime());

                    // 获取房间名
                    String roomNames = "";
                    if (!beginTimeError || !endTimeError) {

                        List<Long> poRoomIds = Arrays.stream(po.getRoomIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
                        poRoomIds.retainAll(roomIds);
                        roomNames = poRoomIds.stream().map(roomIdNameMap::get).collect(Collectors.joining(","));

                    }

                    Assert.isConflict(
                            !beginTimeError,
                            roomNames + " 该时间范围内，该房间已被租赁"
                                     );

                    Assert.isConflict(
                            !endTimeError,
                            roomNames + " 该时间范围内，该房间已被租赁"
                                     );

                });

    }

}
