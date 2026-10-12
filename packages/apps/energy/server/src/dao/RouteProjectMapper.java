package com.guhecloud.cube.srv.gen.energymanagement.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.RouteProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.RouteDeviceVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：支路分项配置 Repository接口
 *
 * @author appet
 * @date 2023-03-29 15:12:36
 */
@Repository
public interface RouteProjectMapper extends BaseMapper<RouteProject> {
    @Select("SELECT a.device_id deviceId,a.device_name deviceName,a.tenant tenant," +
            "b.instrument_type instrumentType,b.`sequence` `sequence`,b.route_name routeName,b.id routeId," +
            "c.device_type_id,a.share_num " +
            "FROM energy_project_item a " +
            "LEFT JOIN route_project b ON a.project_id = b.id " +
            "LEFT JOIN meter_device c ON a.device_id = c.device_id " +
            "WHERE a.bind_type = 'ROUTE'"
    )
    List<RouteDeviceVo> getRouteDevices();

    @Select("SELECT\n" +
            "\tc.id \n" +
            "FROM\n" +
            "\t(\n" +
            "SELECT\n" +
            "\ta.id id,\n" +
            "\tIFNULL( COUNT( b.device_id ), 0 ) num \n" +
            "FROM\n" +
            "\troute_project a\n" +
            "\tLEFT JOIN energy_project_item b ON a.id = b.project_id \n" +
            "WHERE\n" +
            "\ta.instrument_type = #{instrumentType}\n" +
            "\t\n" +
            "GROUP BY\n" +
            "\ta.id \n" +
            "\t) c \n" +
            "WHERE\n" +
            "c.num >0"
    )
    List<Long> getRouteCount(@Param("instrumentType") String instrumentType);
}