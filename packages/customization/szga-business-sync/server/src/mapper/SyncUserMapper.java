package com.guhecloud.cube.srv.gen.business.info.sync.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo.OuaaUser;
import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo.UserInfoVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.User;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 15561
 * @description 针对表【user(系统用户表)】的数据库操作Mapper
 * @createDate 2024-12-17 16:11:56
 * @Entity com.guhecloud.cube.srv.gen.business.info.sync.server.entity.User
 */

@Mapper
public interface SyncUserMapper extends BaseMapper<User> {

    @Select("select  us.`id`,`name`,`mobile`," +
            "        `gender`,`enabled`," +
            "        `id_card` as `paper_number`,`deleted`," +
            "        `create_time`,`update_time`, uorg.organization_id" +
            " from `CUBE-BIZ-OUAA`.`user` us" +
            "        left join `CUBE-BIZ-OUAA`.`user_organization` uorg on us.id = uorg.user_id"
    )
    List<OuaaUser> selectInitInfo();

    List<UserInfoVO> selectUserInfoAll(@Param("keyword") String keyword, @Param("period") Period period);

    Page<UserInfoVO> selectUserInfoAll(Page page, @Param("keyword") String keyword, @Param("period") Period period);

    UserInfoVO selectUserInfoById(@Param("id") Long id);

    void selectUserPermssionById(Long id);
}




