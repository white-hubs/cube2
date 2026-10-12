package com.guhecloud.cube.base.message.sdk.api.channel;

import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import com.guhecloud.cube.base.message2.client.param.MsgSendTargetParam;
import com.guhecloud.cube.biz.ouaa.client.UserWechatClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserWechatDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserWechatQueryParam;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class WePublicServiceChannelReceiver implements ChannelReceiver {

    @Override
    public String getChannelCode() {
        return MessageChannelEnum.CHANNEL_WE_PUBLIC_SERVICE_ACCOUNT.name();
    }

    @Override
    public List<MsgSendTargetParam> getReceiver(List<UserDTO> userDTOS) {
        if (CollectionUtils.isEmpty(userDTOS)) {
            return Collections.emptyList();
        }
        Optional<DataDictionaryDTO> wePublicServiceConfig = getWePublicServiceConfig();
        if (wePublicServiceConfig.isEmpty()) {
            log.warn("wechat_public_service_config not found");
            return Collections.emptyList();
        }
        Set<Long> userIds = userDTOS.stream().map(UserDTO::getId).collect(Collectors.toSet());
        Map<Long, UserWechatDTO> userWechatDTOMap = getUserWechatList(wePublicServiceConfig.get().getId(), userIds);
        if (MapUtils.isEmpty(userWechatDTOMap)) {
            log.warn("user_nothing_ref_wechatInfo");
            return Collections.emptyList();
        }
        List<MsgSendTargetParam> msgSendTargetParams = new ArrayList<>();
        for (UserDTO userDTO : userDTOS) {
            if (userWechatDTOMap.containsKey(userDTO.getId())) {
                msgSendTargetParams.add(MsgSendTargetParam.builder()
                                                .userId(userDTO.getId())
                                                .userName(userDTO.getUsername())
                                                .userPhone(userDTO.getMobile())
                                                .receiver(userWechatDTOMap.get(userDTO.getId()).getOpenId())
                                                .build());
            }
        }
        return msgSendTargetParams;
    }

    private Optional<DataDictionaryDTO> getWePublicServiceConfig() {
        DataDictionaryClient dataDictionaryClient = ApplicationContextHolder.getBean(DataDictionaryClient.class);
        DataDictionaryaramParam dataDictionaryaramParam = new DataDictionaryaramParam();
        dataDictionaryaramParam.setItemCode("THIRD_SERVICE");
        dataDictionaryaramParam.setCode("WX_OFFICIAL_ACCOUNT");
        dataDictionaryaramParam.setDisable(true);
        List<DataDictionaryDTO> dictionaryByItem = dataDictionaryClient.getDataDictionaryByItem(dataDictionaryaramParam);
        if (CollectionUtils.isEmpty(dictionaryByItem)) {
            return Optional.empty();
        }
        return Optional.of(dictionaryByItem.get(0));
    }

    private Map<Long, UserWechatDTO> getUserWechatList(Long configId, Set<Long> userIds) {
        UserWechatClient userWechatClient = ApplicationContextHolder.getBean(UserWechatClient.class);
        UserWechatQueryParam userWechatQueryParam = new UserWechatQueryParam();
        userWechatQueryParam.setConfigId(configId);
        userWechatQueryParam.setUserIds(userIds);
        userWechatQueryParam.setTenant(SessionContextHolder.getRequester().getTenant());
        List<UserWechatDTO> userWechatList = userWechatClient.getUserWechatList(userWechatQueryParam);
        if (CollectionUtils.isEmpty(userWechatList)) {
            return Collections.emptyMap();
        }
        return userWechatList.stream().collect(Collectors.toMap(UserWechatDTO::getUserId, userWechatDTO -> userWechatDTO));
    }
}
