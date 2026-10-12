package com.guhecloud.cube.srv.gen.business.info.sync.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.Organization;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.UserOrganization;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 15561
 * @description 针对表【organization(组织机构信息表)】的数据库操作Mapper
 * @createDate 2024-12-17 16:11:33
 * @Entity com.guhecloud.cube.srv.gen.business.info.sync.server.entity.Organization
 */
public interface SyncOrganizationMapper extends BaseMapper<Organization> {

    @Select("select * from `CUBE-BIZ-OUAA`.`organization`")
    List<Organization> selectInitInfo();

    @Select("select id ,`user_id`,`organization_id` from `CUBE-BIZ-OUAA`.`user_organization`")
    List<UserOrganization> selectRef();
}




