package com.guhecloud.cube.srv.gen.energymanagement.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProjectItem;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.BindTypeVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DeviceAreaVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DeviceProjectVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ProjectItemVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：分项及区域仪表绑定 Repository接口
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
@Repository
public interface EnergyProjectItemMapper extends BaseMapper<EnergyProjectItem> {
    @Delete("Delete FROM energy_project_item where project_id = #{projectId}")
    void deleteByProjectId(Long projectId);

    @Select("SELECT * FROM energy_project_item Where project_id = #{projectId} ")
    List<EnergyProjectItem> getAllByProjectIdList(Long projectId);

    @Select("SELECT a.id itemId,a.device_id deviceId,b.project_name projectName," +
            "b.id projectId,b.instrument_type instrumentType,b.tenant," +
            "c.device_type_id,a.share_num " +
            "FROM energy_project_item a " +
            "LEFT JOIN energy_project b ON a.project_id = b.id " +
            "LEFT JOIN meter_device c on a.device_id = c.device_id " +
            "WHERE a.bind_type = 'ITEM'"
    )
    List<DeviceProjectVo> getDeviceProject();

    @Select("SELECT a.id itemId,a.device_id deviceId,b.area_name areaName,b.id areaId," +
            "b.instrument_type instrumentType,b.`sequence` `sequence`,b.tenant,c.device_type_id,a.share_num " +
            "FROM energy_project_item a " +
            "LEFT JOIN area_project b ON a.project_id = b.id " +
            "LEFT JOIN meter_device c on a.device_id = c.device_id " +
            "WHERE a.bind_type = 'AREA'"
    )
    List<DeviceAreaVo> getDeviceAreas();

    @Select("SELECT DISTINCT\n" +
            "\tb.instrument_type instrumentType,\n" +
            "\tbind_type CODE,\n" +
            "CASE\n" +
            "\tbind_type \n" +
            "\tWHEN 'ITEM' THEN\n" +
            "\t'能耗分项' \n" +
            "\tWHEN 'AREA' THEN\n" +
            "\t'能耗区域' \n" +
            "END AS NAME \n" +
            "FROM\n" +
            "\tenergy_project_item a\n" +
            "\tLEFT JOIN meter_device b ON a.device_id = b.device_id"
    )
    List<BindTypeVo> getBindTypes();

    @Select("SELECT\n" +
            "\ta.bind_type bindType,\n" +
            "\ta.device_id deviceId,\n" +
            "\ta.id itemId,\n" +
            "\ta.project_id projectId,\n" +
            "\ta.tenant tenant,\n" +
            "\tb.instrument_type instrumentType,\n" +
            " IFNULL(c.area_name,d.project_name) projectName\t\n" +
            "FROM\n" +
            "\tenergy_project_item a\n" +
            "\tLEFT JOIN meter_device b ON a.device_id = b.device_id\n" +
            "\tLEFT JOIN area_project c on a.project_id  = c.id\n" +
            "\tLEFT JOIN energy_project d on a.project_id = d.id"
    )
    List<ProjectItemVo> getProjectItems();

    @Select("SELECT\n" +
            "\ta.id AS deviceId \n" +
            "FROM\n" +
            "\tenergy_project_item a\n" +
            "\tLEFT JOIN energy_project b ON a.project_id = b.id \n" +
            "WHERE\n" +
            "\ta.bind_type = #{bindType}"
    )
    List<EnergyProjectItem> getEnergyProjectItem(@Param("bindType") String bindType);

    @Select("SELECT\n" +
            "\ta.id AS deviceId \n" +
            "FROM\n" +
            "\tenergy_project_item a\n" +
            "\tLEFT JOIN area_project b ON a.project_id = b.id \n" +
            "WHERE\n" +
            "\ta.bind_type = #{bindType}\n" +
            "\t\n" +
            "\tAND b.instrument_type = #{instrumentType}"
    )
    List<EnergyProjectItem> getAreaProjectItem(@Param("instrumentType") String instrumentType, @Param("bindType") String bindType);

}