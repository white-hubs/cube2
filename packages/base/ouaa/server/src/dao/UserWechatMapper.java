package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.UserRole;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.entity.UserWechat;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.param.query.UserWechatQueryParam;
import com.guhecloud.cube.biz.ouaa.server.third.wechat.vo.UserWechatVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户微信关联数据操作接口
 *
 * @author Woody
 * @date 2021/7/26
 */
@Mapper
public interface UserWechatMapper extends BaseMapper<UserWechat> {
    /**
     * 初始化微信用户配置信息
     *
     * @param userWechat 用户微信关联信息
     * @param userTenant 用户租户关联信息
     * @param userRoles  用户角色关联信息列表
     */
    void initializeWechatUserConfiguration(@Param("userWechat") UserWechat userWechat,
                                           @Param("userTenant") UserTenant userTenant,
                                           @Param("userRoles") List<UserRole> userRoles
                                          );

    /**
     * 查询用户微信信息
     *
     * @param param
     *
     * @return
     */
    List<UserWechatVO> selectUserWechatListByConfigId(@Param("param") UserWechatQueryParam param);
}
