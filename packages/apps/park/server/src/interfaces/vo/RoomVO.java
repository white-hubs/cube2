package com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.park.management.server.enumeration.RoomStatusEnum;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;

import java.util.List;

@Data
public class RoomVO extends BasicTenantEntity<Long> {

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 房间类型ID
     */
    private Long roomTypeId;

    /**
     * 房间类型名
     */
    private String roomTypeName;

    /**
     * 区域ID
     */
    private Long areaId;
    /**
     * 区域
     */
    private String areaName;

    /**
     * 区域序列号
     */
    private String areaSequence;

    /**
     * 状态
     */
    private RoomStatusEnum status;

    /**
     * 状态名
     */
    private String statusName;

    /**
     * 建筑面积
     */
    private double floorage;

    /**
     * 计费面积
     */
    private double floorageBilling;

    private List<FileVO> fileVOS;

    private Boolean deleted;

}
