package com.guhecloud.cube.srv.gen.securitymanagement.server.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceDeletedParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceExportAppParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.ModelUnderDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.DeviceProdCategoryVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.ModelVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceApplicationCodeStatisticsVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceApplicationStatusStatisticsVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceOutVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceVo;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface SecurityCommonService extends IService<SecurityDevice> {
    List<Long> getTenantDevices(String applicationCode);

    Pagination<SecurityDeviceVo> getSecurityDevicePage(String applicationCode, DeviceParam param);

    Pagination<SecurityDeviceVo> doPostSecurityDevicePage(DeviceParam param);

    void deleteSecurityDevice(Long id);

    void deleteSecurityDeviceBatch(List<Long> ids);

    void conditionDeletedSecurityDevice(DeviceDeletedParam param);

    void addSecurityDevice(DeviceExportAppParam param);

    List<DeviceProdCategoryVo> getGenreVo(String applicationCode);

    List<ModelVo> getModelVo(String applicationCode, String genre);

    Pagination<SecurityDeviceVo> getDeviceByModel(ModelUnderDeviceParam param);

    List<SecurityDeviceVo> getDeviceListByModel(ModelUnderDeviceParam param);

    List<SecurityDeviceOutVo> getSecurityDeviceList(@PathVariable String applicationCode, DeviceParam param);

    List<Long> getSecurityDeviceIds(String applicationCode);

    List<SecurityDeviceApplicationCodeStatisticsVO> getApplicationDeviceStatistics();

    List<SecurityDeviceApplicationStatusStatisticsVO> getApplicationDeviceStatusStatistics();
}
