package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import com.guhecloud.cube.base.device.center.metadata.dto.AreaBaseInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.IdToName;
import lombok.Data;

import java.util.List;

@Data
public class MemberDevicePageResult {
    private Long id;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备编码
     */
    private String code;
    /**
     * 设备状态
     */
    private DeviceBaseArchivesInfoDTO.Status status;
    private IdToName department;
    private String thirdPartyId;
    private String lntAndLat;
    /**
     * 区域信息
     */
    private AreaBaseInfoDTO area;
    private Long picture;
    private DeviceProductInfoDTO productInfo;
    /**
     * 属性集合
     */
    private List<PropResult> propList;

    @Data
    public static class PropResult {
        private String propCode;
        private String propName;
        private String dataType;
        private Integer precision;
        private String unitName;
        private Integer dataWrite;
        private String dataConstraint;
        private String dataConstraintStr;
        private Boolean isTelemetry;
        private String remark;
        private String value;
    }
}
