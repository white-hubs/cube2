package com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.RentRecordRoomParam;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoomRentRecordVO extends BasicTenantEntity<Long> {

    /**
     * ID
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 房间ID
     */
    private String roomIds;

    /**
     * 房间名称
     */
    private String roomNames;

    /**
     * 签订时间
     */
    private LocalDateTime signTime;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    private List<RentRecordRoomParam> rentRooms;

    private List<FileVO> fileVOS;

}
