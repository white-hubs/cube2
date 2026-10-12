package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.dictionary.adapter.UserDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.dto.UserDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.cube.base.dictionary.param.UserDictionaryParam;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.DictionaryEnum;
import com.guhecloud.cube.biz.ouaa.server.dao.UserOrganizationMapper;
import com.guhecloud.cube.biz.ouaa.server.dto.UserOrganizationDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.UserOrganization;
import com.guhecloud.cube.biz.ouaa.server.param.UserQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.UserOrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.UserRoleService;
import com.guhecloud.cube.biz.ouaa.server.vo.UserVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DesensitizeUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户组织机构关联业务操作实现
 *
 * @author Woody
 * @date 2021/7/1
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserOrganizationServiceImpl extends ServiceImpl<UserOrganizationMapper, UserOrganization>
        implements UserOrganizationService {
    private final DataDictionaryClient dictionaryClient;
    private final UserRoleService userRoleService;
    private final UserDictionaryAdapter userDictionaryAdapter;

    @Override
    public List<UserOrganizationDTO> listUserOrganizations(@NonNull Collection<Long> userIds) {
        return userIds.isEmpty() ? Collections.emptyList() : this.baseMapper.listUserOrganizations(userIds);
    }

    @Override
    public List<UserOrganization> listOrganizationUsers(@NonNull Collection<Long> organizationIds) {
        return organizationIds.isEmpty() ? Collections.emptyList() :
               this.baseMapper.listOrganizationUsersByOrganizationIds(SessionContextHolder.getRequester().getTenant(), organizationIds);
    }

    @Override
    public List<UserOrganization> listOrganizationUsers(@NonNull Long organizationId) {
        return this.lambdaQuery().eq(UserOrganization::getOrganizationId, organizationId).list();
    }

    @Override
    public List<UserOrganization> listPositionUsers(@NonNull Long positionId) {
        return this.lambdaQuery().eq(UserOrganization::getPositionId, positionId).list();
    }

    @Override
    public Page<UserVO> listOrganizationUsers(@NonNull UserQueryParam param) {
        Requester requester = SessionContextHolder.getRequester();
        //查询参数赋值
        //角色
        Set<Long> userIds = new HashSet<>();
        if (StringUtils.isNotBlank(param.getRoleId())) {
            userIds.addAll(userRoleService.listUserIdsByRoleIds(Arrays
                                                                        .stream(param.getRoleId().split(","))
                                                                        .map(Long::valueOf)
                                                                        .collect(Collectors.toList())));
            if (CollectionUtils.isEmpty(userIds)) {
                return new Page<>();
            }
        }
        // 部门和岗位
        if (StringUtils.isNotBlank(param.getPositionId()) || Objects.nonNull(param.getOrganizationId())
            || Objects.nonNull(param.getAppointmentId())) {
            List<Long> positionIds = new ArrayList<>();
            if (StringUtils.isNotBlank(param.getPositionId())) {
                positionIds = Arrays.stream(param.getPositionId().split(","))
                        .map(Long::valueOf)
                        .collect(Collectors.toList());
            }
            List<Long> longs = this.listUserByOrgAndPost(Objects.nonNull(param.getOrganizationId())
                                                         ? Collections.singletonList(param.getOrganizationId()) : null,
                                                         positionIds, param.getAppointmentId());
            if (CollectionUtils.isEmpty(longs)) {
                return new Page<>();
            }
            //求交集
            if (CollectionUtils.isNotEmpty(userIds)) {
                Collection<Long> intersection = CollectionUtils.intersection(userIds, longs);
                if (CollectionUtils.isEmpty(intersection)) {
                    return new Page<>();
                } else {
                    userIds = new HashSet<>(intersection);
                }
            } else {
                userIds.addAll(longs);
            }
        }
        userIds.removeIf(Objects::isNull);
        if (CollectionUtils.isNotEmpty(param.getIds())) {
            userIds.addAll(param.getIds());
        }
        param.setIds(new ArrayList<>(userIds));
        Page<UserVO> page = this.baseMapper.listOrganizationUsers(PagingUtils.convert(param), requester.getTenant(), param);
        // 用户手机号加密，中间四位*号隐藏
        page.setRecords(this.encryptPhone(page.getRecords()));
        return page;
    }

    @Override
    public List<UserVO> userList(UserQueryParam param) {
        Requester requester = SessionContextHolder.getRequester();
        List<UserVO> list = this.baseMapper.listOrganizationUsers(requester.getTenant(), param);
        return this.encryptPhone(list);
    }

    @Override
    public List<Long> listEnableUserByOrgAndPost(List<Long> orgIds, List<Long> postIds) {

        return this.baseMapper.listEnableUserByOrgAndPost(orgIds, postIds);
    }

    @Override
    public List<Long> listUserByOrgAndPost(List<Long> orgIds, List<Long> postIds, Long appointmentId) {
        return this.baseMapper.listUserByOrgAndPost(orgIds, postIds, appointmentId);
    }

    /**
     * 手机号加密
     */

    @Override
    public List<UserVO> encryptPhone(List<UserVO> users) {
        try {
            Boolean phone_encryption = false;
            DataDictionaryaramParam dictionaryaramParam = new DataDictionaryaramParam();
            dictionaryaramParam.setItemCode(DictionaryEnum.SERVICE_CONFIGURATION.name());
            dictionaryaramParam.setCode(DictionaryEnum.SERVICE_CONFIGURATION.name());
            List<DataDictionaryDTO> dictionarys = dictionaryClient.getDataDictionaryByItem(dictionaryaramParam);
            if (ObjectUtils.isNotEmpty(dictionarys)) {
                String variable = dictionarys.get(0).getVariable();
                // String 转 json
                JSONObject json = JSON.parseObject(variable);
                phone_encryption = (Boolean) json.get("phone_encryption");
            }
            if (phone_encryption) {
                users.forEach(user -> {
                    String mobile = DesensitizeUtils.desensitize(user.getMobile(), DesensitizeUtils.DESENSITIZE_MODE_MOBILE);
                    user.setMobile(mobile);
                });
            } else {
                Set<Long> userIds = users.stream().map(UserVO::getId).collect(Collectors.toSet());
                UserDictionaryParam userDictionaryParam = new UserDictionaryParam();
                userDictionaryParam.setItemCode(DictionaryEnum.SERVICE_CONFIGURATION.name());
                userDictionaryParam.setCode(DictionaryEnum.SERVICE_CONFIGURATION.name());
                userDictionaryParam.setUserIds(userIds);
                List<UserDictionaryDTO> userDictionaryDTOList = userDictionaryAdapter.getUserDictionaryList(userDictionaryParam);
                if (ObjectUtils.isEmpty(userDictionaryDTOList)) {
                    return users;
                }
                Map<Long, String> map = userDictionaryDTOList.stream()
                        .collect(Collectors.toMap(UserDictionaryDTO::getUserId, UserDictionaryDTO::getName));
                users.forEach(user -> {
                    String config = map.get(user.getId());
                    if (ObjectUtils.isNotEmpty(config) && Boolean.TRUE.toString().equals(config)) {
                        String mobile = DesensitizeUtils.desensitize(user.getMobile(), DesensitizeUtils.DESENSITIZE_MODE_MOBILE);
                        user.setMobile(mobile);
                    }
                });
            }
        } catch (Exception e) {
            log.error("手机号加密失败", e);
        }
        return users;
    }

    @Override
    public Integer countUserOrganizations(List<Long> orgIds) {
        return this.baseMapper.countUserOrganizations(orgIds, SessionContextHolder.getRequester().getTenant());
    }

    @Override
    public Integer countUserAppointments(List<Long> appointmentIds) {
        return this.baseMapper.countUserAppointments(appointmentIds, SessionContextHolder.getRequester().getTenant());
    }

    @Override
    public Integer countUserPositions(List<Long> positionIds) {
        return this.baseMapper.countUserPositionIds(positionIds, SessionContextHolder.getRequester().getTenant());

    }
}
