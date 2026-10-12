package com.guhecloud.cube.srv.gen.library.management.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.library.management.server.po.SystemLoginInfoPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LibraryLoginInfoMapper extends BaseMapper<SystemLoginInfoPO> {

    /**
     * 获取图书管理的登录信息
     *
     * @return 登录信息
     */
    @Select("select * from system_login_info where scene_code = 'lamp-library'")
    SystemLoginInfoPO getLoginInfoByLibrary();
}
