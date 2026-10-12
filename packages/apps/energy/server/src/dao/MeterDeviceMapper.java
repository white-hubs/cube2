package com.guhecloud.cube.srv.gen.energymanagement.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.vo.AreaListVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDevice;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：仪表管理 Repository接口
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
@Repository
public interface MeterDeviceMapper extends BaseMapper<MeterDevice> {
    @Delete("DELETE FROM meter_device WHERE instrument_type = #{instrumentType}")
    void deleteByTenantId(@Param("instrumentType") String instrumentType);

    @Delete("DELETE FROM meter_device WHERE device_id = #{device}")
    void deleteByDeviceId(@Param("device") String device);

    @Select("SELECT DISTINCT device_id FROM meter_device")
    List<Long> getDeviceIds();

    void updateDevices(@Param("list") List<Long> list, @Param("date") long date);

    void updateItemDevices(@Param("list") List<Long> list, @Param("date") long date);

    @Select("select device_id as id,device_name as areaName,device_area_id, true as deviceFlag " +
            "from meter_device " +
            "where device_area_id like concat('%',#{areaId},'-')"
    )
    List<AreaListVO> getDeviceByAreaId(@Param("areaId") String areaId);

    List<Long> getChildrenDevice(@Param("deviceId") Long deviceId);

    @InterceptorIgnore(tenantLine = "on")
    int updateMeterProduct(@Param("productId") Long productId, @Param("productName") String productName);
}