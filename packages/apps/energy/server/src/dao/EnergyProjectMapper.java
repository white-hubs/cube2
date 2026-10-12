package com.guhecloud.cube.srv.gen.energymanagement.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.BindTypeVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DeviceInEnergyProjectVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：能耗分项 Repository接口
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
@Repository
public interface EnergyProjectMapper extends BaseMapper<EnergyProject> {
    @Select("SELECT id code,project_name name,instrument_type instrumentType FROM energy_project where instrument_type =#{instrumentType}")
    List<BindTypeVo> getProjects(@Param("instrumentType") String instrumentType);

    /**
     * 根据分项ID 查询设备
     * 分项ID 可以为空
     *
     * @param projectId
     *
     * @return
     */
    List<DeviceInEnergyProjectVO> getAllDeviceInProject(@Param("projectId") String projectId);
}