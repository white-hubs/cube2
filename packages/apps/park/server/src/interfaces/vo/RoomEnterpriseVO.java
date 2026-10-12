package com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo;

import lombok.Data;

@Data
public class RoomEnterpriseVO extends RoomVO {

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 企业详情
     */
    private EnterpriseVO enterpriseVO;

}
