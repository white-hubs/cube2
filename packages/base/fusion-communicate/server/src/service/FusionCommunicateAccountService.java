package com.guhecloud.cube.biz.fusion.communicate.server.service;

import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.fusion.communicate.server.gps.service.UserGpsHistoryService;
import com.guhecloud.cube.biz.fusion.communicate.server.gps.vo.UserGpsHistoriesVO;
import com.guhecloud.cube.biz.fusion.communicate.server.param.UserQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.param.AccountQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.strategy.FusionCommunicateStrategy;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.AccountAllocationUserVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.FusionCommunicateAccountVO;
import com.guhecloud.cube.biz.fusion.communicate.server.vo.UserVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FusionCommunicateAccountService {

    private final FusionCommunicateStrategy fusionCommunicateStrategy;

    private final UserGpsHistoryService userGpsHistoryService;

    private final UserClient userClient;

    private final FileClient fileClient;

    public FusionCommunicateAccountService(FusionCommunicateStrategy fusionCommunicateStrategy,
                                           UserGpsHistoryService userGpsHistoryService,
                                           UserClient userClient,
                                           FileClient fileClient
                                          ) {
        this.fusionCommunicateStrategy = fusionCommunicateStrategy;
        this.userGpsHistoryService = userGpsHistoryService;
        this.userClient = userClient;
        this.fileClient = fileClient;
    }

    public Pagination<FusionCommunicateAccountVO> getThirdFusionAccountPage(@Valid AccountQueryParam query) {
        return this.fusionCommunicateStrategy.getFusionCommunicateAccount().getThirdFusionAccountPage(query);
    }

    public UserVO getSysUserByFusionCommunicateAccount(String fusionCommunicateAccount) {
        Long userId = this.fusionCommunicateStrategy
                .getFusionCommunicateAccount().getSysUserByFusionCommunicateAccount(fusionCommunicateAccount);
        if (Objects.isNull(userId)) {
            return null;
        }
        UserDTO user = this.userClient.getUser(userId);
        if (Objects.isNull(user)) {
            return null;
        }
        List<AccountAllocationUserVO> onlineUserIds = this.fusionCommunicateStrategy.getFusionCommunicateAccount()
                .getOnlineUserIds(Collections.singleton(userId));
        UserVO userVO = ObjectUtils.copying(user, UserVO.class);
        extendUser(Collections.singletonList(userVO), onlineUserIds);
        return userVO;
    }

    public List<UserVO> getOnlineUserIds(Set<Long> sysUserIds) {
        List<AccountAllocationUserVO> onlineUsers = this.fusionCommunicateStrategy
                .getFusionCommunicateAccount().getOnlineUserIds(sysUserIds);
        if (CollectionUtils.isEmpty(onlineUsers)) {
            return Collections.emptyList();
        }
        Set<Long> onlineUserIds = onlineUsers.stream().map(AccountAllocationUserVO::getUserId).collect(Collectors.toSet());
        UserQuery param = new UserQuery();
        param.setUserIds(onlineUserIds);
        List<UserDTO> userDTOS = this.userClient.postSystemList(param);
        if (CollectionUtils.isEmpty(userDTOS)) {
            return Collections.emptyList();
        }
        List<UserVO> userVOS = ObjectUtils.copying(userDTOS, UserVO.class);
        extendUser(userVOS, onlineUsers);
        return userVOS;
    }

    public void extendUser(List<UserVO> userVOS, List<AccountAllocationUserVO> onlineUsers) {
        List<Long> userIds = userVOS.stream().map(UserVO::getId).collect(Collectors.toList());
        // 获取用户头像
        BusinessKeysParam businessKeysParam = new BusinessKeysParam();
        businessKeysParam.setBusinessKeys(userIds);
        Map<Long, List<FileVO>> userFileMaps = this.fileClient.getByBusinessKeys(businessKeysParam);
        Map<Long, List<AccountAllocationUserVO>> accountAllocationUserMap = onlineUsers.stream()
                .collect(Collectors.groupingBy(AccountAllocationUserVO::getUserId));
        Map<Long, List<UserGpsHistoriesVO>> userGpsHistoriesVOMap = this.userGpsHistoryService.getUserLastGpsHistories(userIds).stream()
                .collect(Collectors.groupingBy(UserGpsHistoriesVO::getUserId));
        userVOS.forEach(userVO -> {
            if (userFileMaps.containsKey(userVO.getId())) {
                userVO.setFile(userFileMaps.get(userVO.getId()).get(0));
            }
            if (accountAllocationUserMap.containsKey(userVO.getId())) {
                userVO.setOrder(1);
                userVO.setAccount(accountAllocationUserMap.get(userVO.getId()).get(0).getAccount());
            } else {
                userVO.setOrder(0);
            }
            if (userGpsHistoriesVOMap.containsKey(userVO.getId())) {
                userVO.setGps(userGpsHistoriesVOMap.get(userVO.getId()).get(0));
            }
        });
    }

    public List<AccountAllocationUserVO> assignFusionCommunicateAccount(Set<Long> userIds) {
        return this.fusionCommunicateStrategy.getFusionCommunicateAccount().assignFusionCommunicateAccount(userIds);
    }

    public void disableFusionAccount(Set<Long> userIds) {
        this.fusionCommunicateStrategy.getFusionCommunicateAccount().disableFusionAccount(userIds);
    }

    public void keepAliveFusionAccount(Set<Long> userIds) {
        this.fusionCommunicateStrategy.getFusionCommunicateAccount().keepAliveFusionAccount(userIds);
    }

    public List<UserVO> getUsers(UserQueryParam param) {
        UserQuery userQuery = ObjectUtils.copying(param, UserQuery.class);
        List<UserDTO> users = this.userClient.postSystemList(userQuery);
        List<AccountAllocationUserVO> onlineUsers = this.fusionCommunicateStrategy
                .getFusionCommunicateAccount().getOnlineUserIds(Collections.emptySet());
        List<UserVO> userVOS = ObjectUtils.copying(users, UserVO.class);
        if (CollectionUtils.isEmpty(onlineUsers)) {
            return userVOS;
        }
        if (CollectionUtils.isEmpty(userVOS)) {
            return Collections.emptyList();
        }
        extendUser(userVOS, onlineUsers);
        return userVOS.stream().sorted(Comparator.comparing(UserVO::getOrder).reversed()).collect(Collectors.toList());
    }
}
