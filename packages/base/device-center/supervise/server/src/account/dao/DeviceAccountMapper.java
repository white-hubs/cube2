package com.guhecloud.cube.srv.gen.device.supervise.server.account.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.interfaces.vo.DeviceAccountVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.po.DeviceAccountPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 设备台账 Mapper 接口
 * </p>
 *
 * @author zwb
 * @since 2022-06-20
 */
@Mapper
public interface DeviceAccountMapper extends BaseMapper<DeviceAccountPO> {

    @Select("SELECT DISTINCT a.*,if(b.device_id,1,0) AS collected " +
            "FROM device_account a " +
            "LEFT JOIN device_account_collect b " +
            "ON b.device_id = a.id and b.tenant=a.tenant and b.user_id= #{userId} " +
            "${ew.customSqlSegment}"
    )
    List<DeviceAccountVO> getList(@Param("userId") Long userId, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT DISTINCT a.*,if(b.device_id,1,0) AS collected " +
            "FROM device_account a " +
            "LEFT JOIN device_account_collect b " +
            "ON b.device_id = a.id and b.tenant=a.tenant and b.user_id= #{userId} " +
            "${ew.customSqlSegment}"
    )
    Page<DeviceAccountVO> getPage(Page page, @Param("userId") Long userId, @Param(Constants.WRAPPER) Wrapper wrapper);

    @InterceptorIgnore(tenantLine = "true")
    @Select("SELECT * FROM device_account where deleted = 0")
    Page<DeviceAccountPO> pageIngoreTenant(Page page);

}
