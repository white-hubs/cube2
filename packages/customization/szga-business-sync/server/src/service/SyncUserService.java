package com.guhecloud.cube.srv.gen.business.info.sync.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.param.UserInfoParam;
import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo.UserInfoVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo.UserPermissionVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.User;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;

import java.util.List;

/**
 * @author 15561
 * @description 针对表【user(系统用户表)】的数据库操作Service
 * @createDate 2024-12-17 16:11:56
 */
public interface SyncUserService extends IService<User> {

    /**
     * 全量获取员工信息
     *
     * @param keyword
     * @param period
     *
     * @return 全量数据
     */
    List<UserInfoVO> getUserInfoListAll(String keyword, Period period);

    /**
     * 分页获取员工信息
     *
     * @param userInfoParam 查询参数
     *
     * @return 分页数据
     */

    Pagination<UserInfoVO> getUserInfoList(UserInfoParam userInfoParam);

    /**
     * 根据id查询员工信息
     */
    UserInfoVO getUserInfo(Long userId);

    /**
     * 根据电话号码查询指定用户权限
     *
     * @param mobile
     *
     * @return
     */
    UserPermissionVO getUserPermissionById(String mobile);

    /**
     * 更新用户权限
     *
     * @param userPermissionVO
     *
     * @return
     */
    boolean userPermissionModify(UserPermissionVO userPermissionVO);

}
