package com.guhecloud.cube.biz.vmonitor.server.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 摄像机区域设备分组查询返回参数
 *
 * @author lanchun
 * @date 2022/11/24 9:14
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CameraAreaDeviceGroupVO extends Tree.Simple<Long, CameraAreaDeviceGroupVO> {

    private Long id;

    /**
     * 区域编号
     */
    private String code;

    /**
     * 区域名称
     */
    private String name;

    private String type;

    /**
     * 序列号
     */
    private String sequence;

    /**
     * 设备数量
     */
    private Integer deviceCount;

    /**
     * 第三方设备id
     */
    private String thirdPartyId;

    /**
     * 状态
     */
    private Boolean status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 流媒体信息
     */
    private StreamMediaVO streamMedia;

    /**
     * 设备标签列表
     */
    private List<CameraMultipleVideoPageVO.DeviceTagVO> tags;
}
