package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.BindTypeEnum;
import lombok.Data;

/**
 * @author lrj
 * @Description 分项及区域仪表绑定
 * @date 2022-07-04 15:12:36
 */
@Data
public class EnergyProjectItemParam {

    //绑定类型
    private BindTypeEnum bindType;
    //分项id
    private Long projectId;
    //分摊比例
    private Integer shareNum;
    //设备类型
    private String deviceType;
    //归属
    private String deviceArea;
    //设备名称
    private String deviceName;
    //设备id
    private String deviceId;
    //设备编码
    private String code;
    //父级设备id
    private String parentId;

}