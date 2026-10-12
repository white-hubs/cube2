package com.guhecloud.cube.biz.vmonitor.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

/**
 * 多路视频摄像机返回参数
 *
 * @author lanchun
 * @date 2021/12/6 19:51
 */
@Data
public class MultipleVideoCollectCameraVO {

    private Long id;

    /**
     * 多路视频主键
     */
    private Long multipleVideoCollectId;

    /**
     * 摄像机设备主键
     */
    private Long deviceId;

    /**
     * 摄像机设备编号
     */
    private String code;

    /**
     * 摄像机设备名称
     */
    private String name;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 在线状态
     */
    private Boolean deviceStatus;

    /**
     * 流媒体信息
     */
    private StreamMediaVO streamMedia;

    /**
     * 第三方设备标识
     */
    private String thirdPartyId;

    private FileVO image;
}