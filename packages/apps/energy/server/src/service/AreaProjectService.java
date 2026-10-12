package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.AreaProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.AreaProjectParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyProjectItemParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.AreaProjectQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.AreaProjectVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DeviceAreaVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DeviceInEnergyProjectVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectItemVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;

/**
 * 描述：区域分项配置服务层
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
public interface AreaProjectService extends IService<AreaProject> {

    Long add(AreaProjectParam param);

    void areaReferDevice(Long id, List<EnergyProjectItemParam> deviceList);

    void update(Long id, AreaProjectParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    AreaProjectVO getById(Long id);

    Pagination<AreaProjectVO> getPage(AreaProjectQueryParam param);

    List<DeviceAreaVo> getDeviceAreas();

    List<EnergyProjectItemVO> getReferDeviceById(Long id);

    long getDeviceNumInAreaProject(Long projectId);

    List<DeviceInEnergyProjectVO> getAllDeviceInAreaProject(String projectId);

    List<Long> getDeviceForStat();

    Map<Long, List<Long>> getAreaLinkDevice(List<Long> areaIdList);
}