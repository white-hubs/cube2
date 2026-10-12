package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.entity.Device;
import com.guhecloud.cube.deliver.project.merchant.dto.DeviceDTO;

import java.util.List;
import java.util.Map;

public interface DeviceService extends IService<Device> {

    public static final String RELATEDDRAWING="relatedDrawing";
    public static final String LIVEACTION="liveAction";
    public static final String PURCHASECONTRACT="purchaseContract";

    public static final String STATUS_NORMAL="deviceStatus_normal";
    public static final String STATUS_ABNORMAL="deviceStatus_abnormal";

    public static final String BIMINFO_BUSINESS_TYPE = "device";

    public static final String MODEL_CAMERA = "cameraModel";
    public static final String MODEL_CAMERA_BALL = "cameraModel_ball";
    public static final String MODEL_CAMERA_GUN = "cameraModel_gun";

    public static final String TIME_LIMIT_NORMAL = "time_limit_normal";
    public static final String TIME_LIMIT_SOON = "time_limit_soon";
    public static final String TIME_LIMIT_EXPIRE = "time_limit_expire";

    // 监控对象
    public static final String MONITOR_POWER_DISTRIBUTION_BOX = "powerDistributionBox"; // 配电箱
    public static final String MONITOR_CHARGING_PILE = "chargingPile";                  // 充电桩


    void save(DeviceDTO deviceDTO, LoginUser loginUser);

    void update(DeviceDTO deviceDTO, String userId);

    void remove(String id);

    void removeBatch(String[] ids);

    void updateField(DeviceDTO deviceDTO);


    void updateData(String projectId, Map<String, String> nameMap);

    void updateCamOnLineJob();

    /**
     * 通过权限筛选摄像头
     * @param projectId
     * @param loginUser
     * @param dtos
     * @return
     */
    List<DeviceDTO> filterCameraByAuth(String projectId, LoginUser loginUser, List<DeviceDTO> dtos);

}
