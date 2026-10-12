package com.guhecloud.cube.srv.gen.energymanagement.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.AreaProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.BindTypeVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DeviceInEnergyProjectVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：区域分项配置 Repository接口
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
@Repository
public interface AreaProjectMapper extends BaseMapper<AreaProject> {
    @Select("SELECT id code,area_name name FROM area_project where instrument_type = #{instrumentType}")
    List<BindTypeVo> getAreas(@Param("instrumentType") String instrumentType);

    List<Long> getAreaCount(@Param("instrumentType") String instrumentType);

    /**
     * 根据分区ID 查询设备
     * 分区ID 可以为空
     *
     * @param projectId
     *
     * @return
     */
    List<DeviceInEnergyProjectVO> getAllDeviceInAreaProject(@Param("projectId") String projectId);
}