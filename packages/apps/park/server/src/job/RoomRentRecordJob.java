package com.guhecloud.cube.srv.gen.park.management.server.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.RoomRentRecordCommandService;
import com.guhecloud.cube.srv.gen.park.management.server.enumeration.RoomRentRecordStatusEnum;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomRentRecordPO;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
public class RoomRentRecordJob {

    @Autowired
    private RoomRentRecordCommandService commandService;

    @JobAnnotation(name = "roomRentRecordExpire", cron = "0 0 0 * * ?", desc = "园区管理-租赁任务")
    public void roomRentRecordExpire(String param) {
        var list = commandService.list(Wrappers.<RoomRentRecordPO>lambdaQuery()
                                               .eq(RoomRentRecordPO::getStatus, RoomRentRecordStatusEnum.EXECUTING.name())
                                               .or()
                                               .eq(RoomRentRecordPO::getStatus, RoomRentRecordStatusEnum.NORMAL));
        var collect = list.stream().map(RoomRentRecordPO::getId).toList();
        var join = collect.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        commandService.updateRoomRentInfo(join);
    }
}
