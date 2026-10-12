package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyProjectItemParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyProjectParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyProjectQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DeviceInEnergyProjectVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DeviceProjectVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectItemVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：能耗分项服务层
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
public interface EnergyProjectService extends IService<EnergyProject> {

    Long add(EnergyProjectParam param);

    void update(Long id, EnergyProjectParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    EnergyProjectVO getById(Long id);

    Pagination<EnergyProjectVO> getPage(EnergyProjectQueryParam param);

    List<EnergyProjectVO> getList(EnergyProjectQueryParam param);

    List<DeviceProjectVo> getDeviceProject();

    /**
     * 分项、区域、支路等关联设备
     *
     * @param id
     * @param referDeviceList
     */
    void saveProjectReferDevice(Long id, List<EnergyProjectItemParam> referDeviceList);

    /**
     * 根据分项ID 查询关联的设备
     *
     * @param id
     *
     * @return
     */
    List<EnergyProjectItemVO> getProjectReferDevice(Long id);

    List<DeviceInEnergyProjectVO> getAllDeviceInProject(String projectId);
}