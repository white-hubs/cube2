package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import lombok.Data;

import java.util.List;

@Data
public class MemberGroupDeviceCommandParam {
    /**
     * 产品id
     */
    private Long productId;
    /**
     * 产品品类id
     */
    private Long prodCategoryId;
    /**
     * 设备id集合
     */
    private List<Long> deviceIds;
    /**
     * 设备序列号
     */
    private String deviceCode;
    /**
     * 所属组织
     */
    private Long groupId;

    /**
     * 绑定设备时所在纬度（前端 coords.latitude）
     */
    private Double latitude;

    /**
     * 绑定设备时所在经度（前端 coords.longitude）
     */
    private Double longitude;

    /**
     * 区县位置
     */
    private String districtLocation;

    /**
     * 详细位置
     */
    private String locationDetail;

}
