package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.metadata.param.UserRuleConditionParam;
import com.guhecloud.cube.biz.ouaa.server.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.param.PasswordUpdateParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserBelongsInstitutionParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserCenterQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserPersonalUpdateParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserUpdateParam;
import com.guhecloud.cube.biz.ouaa.server.param.query.SysUserQuery;
import com.guhecloud.cube.biz.ouaa.server.param.query.UserMapQuery;
import com.guhecloud.cube.biz.ouaa.server.vo.UserVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户业务操作接口
 *
 * @author Woody
 * @date 2021/6/20
 * TODO: 改名 UserRepository
 */
public interface UserService extends IService<User> {

    /**
     * 平台管理端新增用户
     *
     * @param param 新增参数
     *
     * @return 用户信息
     */
    User saveByAdmin(UserModifyParam param);

    /**
     * 企业管理端新增用户
     *
     * @param user  用户实例
     * @param param 用户参数
     */
    void saveByTenant(User user, UserModifyParam param);

    /**
     * 平台管理端用户编辑
     *
     * @param param 编辑参数
     */
    void updateByAdmin(UserUpdateParam param);

    /**
     * 无角色更新
     *
     * @param id
     * @param param
     *
     * @return
     */
    User updateNormalByTenant(@NonNull Long id, @NonNull UserModifyParam param);

    /**
     * 企业管理端用户编辑
     *
     * @param id    用户主键
     * @param param 修改参数
     *
     * @return 用户实例
     */
    User updateByTenant(Long id, UserModifyParam param);

    /**
     * 用户个人信息修改
     *
     * @param param
     */
    void updateUserPersonal(UserPersonalUpdateParam param);

    /**
     * 注册或修改用户并返回是否注册成功
     *
     * @param user 用户信息
     *
     * @return true/false
     */
    boolean registerOrUpdate(User user);

    /**
     * 获取当前租户下的管理员
     *
     * @return 用户
     */
    User getAdmin();

    /**
     * 根据用户唯一标识（如账号、手机号等）查找用户
     *
     * @param key 用户唯一标识
     *
     * @return 用户
     */
    User lookupUser(String key);

    /**
     * 根据用户唯一标识（如账号、手机号等）查找用户
     *
     * @param key
     *
     * @return
     */
    User lookupUserNoContext(String key);

    /**
     * 运营平台用户列表
     *
     * @param queryParam 查询参数
     *
     * @return 运营平台用户列表分页
     */
    Page<User> getByPage(UserQueryParam queryParam);

    /**
     * 系统用户分页列表
     *
     * @param query
     *
     * @return
     */
    Page<User> getSysUsers(SysUserQuery query);

    /**
     * 岗位下的用户列表
     *
     * @param positionId
     *
     * @return
     */
    List<UserDTO> positionUsers(Long positionId);

    /**
     * 组织架构下的用户列表
     *
     * @param organizationId
     *
     * @return
     */
    List<UserDTO> organizationUsers(Long organizationId);

    /**
     * 某个组织架构岗位下的用户列表
     *
     * @param organizationId
     * @param positionId
     *
     * @return
     */
    List<UserDTO> organizationAndPostUsers(Long organizationId, Long positionId);

    /**
     * 某个组织架构岗位下启用的用户列表
     *
     * @param organizationId
     * @param positionId
     *
     * @return
     */
    List<UserDTO> organizationAndPostEnabledUsers(Long organizationId, Long positionId);

    /**
     * 组织架构下的用户列表
     *
     * @param organizationId
     *
     * @return
     */
    List<UserDTO> organizationEnableUsers(Long organizationId);

    /**
     * 修改密码
     *
     * @param id    用户主键
     * @param param 修改密码参数
     */
    void updatePassword(Long id, PasswordUpdateParam param);

    void resetPassword(Long id, String newPassword);

    Map<String, String> getUserOrganizationAndPostName(Long id);

    void removeUserTenant(Integer tenant, Long userId);

    /**
     * 职位下用户
     *
     * @param appointmentIds 职位主键
     *
     * @return
     */
    List<UserDTO> getUserListByAppointmentIds(String appointmentIds);

    /**
     * 下载模板
     */
    void downloadUserTemplate(HttpServletResponse response);

    /**
     * 导入用户数据
     */
    void importUser(MultipartFile file);

    /**
     * 初始化人员照片
     */
    String batchSyncPhotos(Integer tenantId);

    Boolean userBelongsInstitution(UserBelongsInstitutionParam userBelongsInstitutionParam);

    Map<String, UserDTO> getTenantUserMap(UserMapQuery userQuery);

    /**
     * 用户中心获取用户列表
     *
     * @param param
     *
     * @return
     */
    Pagination<UserVO> getRegisterUserListForUserCenter(UserCenterQueryParam param);

    /**
     * 用户中心获取用户详情
     *
     * @param id
     *
     * @return
     */
    UserVO getRegisterUserDetailForUserCenter(Long id);

    void importUserSyncSuzhou(MultipartFile file);

    boolean isRegister(String username);

    User getUserByMobile(String mobile);

    User getUserByAccount(String account);

    Map<Long, Set<Long>> findUserByRule(List<UserRuleConditionParam> ruleParam);

}
