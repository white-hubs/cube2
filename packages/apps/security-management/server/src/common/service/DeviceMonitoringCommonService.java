package com.guhecloud.cube.srv.gen.securitymanagement.server.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.client.vo.AreaListVO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.dto.SecurityDeviceInfoDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceMonitoringParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.HistoryDataParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.InsideSecurityDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.DeviceCollectionVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.DeviceProdCategoryVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.ModelVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityStatusDevicesNumVo;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

public interface DeviceMonitoringCommonService extends IService<SecurityDevice> {
    List<AreaListVO> getAreaTree(String applicationCode);

    SecurityStatusDevicesNumVo getStatusList(String applicationCode);

    List<DeviceProdCategoryVo> getApplicationGenreVo(String applicationCode);

    List<ModelVo> getApplicationModels(String applicationCode);

    List<DeviceProdCategoryVo> getStatisticsGenreVo(List<Long> ids);

    List<DeviceCollectionVo> getDeviceCollection(String applicationCode);

    Long getDevicesNum(@PathVariable String applicationCode);

    Long getCollectionDevicesNum(@PathVariable String applicationCode);

    Pagination<SecurityDeviceInfoDTO> getDeviceMonitoringPage(String applicationCode, DeviceMonitoringParam param);

    Pagination<SecurityDeviceInfoDTO> getCollectionDeviceMonitoringPage(String applicationCode, DeviceMonitoringParam param);

    Map<String, Object> getDeviceInfo(Long deviceId);

    DeviceTSDTO getDeviceHistoryData(HistoryDataParam param);

    Pagination<SecurityDeviceInfoDTO> getInsideDeviceMonitoringPage(InsideSecurityDeviceParam param);

    Pagination<SecurityDeviceInfoDTO> getDeviceMonitoringList(InsideSecurityDeviceParam param);

}
