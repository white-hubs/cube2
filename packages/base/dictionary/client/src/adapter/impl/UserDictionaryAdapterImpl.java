package com.guhecloud.cube.base.dictionary.adapter.impl;

import com.guhecloud.cube.base.dictionary.adapter.UserDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.client.UserDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.UserDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.UserDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.param.UserDictionaryModifyParam;
import com.guhecloud.cube.base.dictionary.param.UserDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.param.UserDictionaryParam;
import com.guhecloud.cube.base.dictionary.utils.DictionaryRedisUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/28 16:07
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDictionaryAdapterImpl implements UserDictionaryAdapter {
    private final DictionaryRedisUtils dictionaryRedisUtils;
    private final UserDictionaryClient userDictionaryClient;

    @Override
    public UserDictionaryDTO getUserDictionaryByCode(UserDictionaryParam userDictionaryParam) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserDictionaryDTO userDictionaryDTO = dictionaryRedisUtils
                .get(userDictionaryParam.getItemCode(), userDictionaryParam.getCode() + userId, UserDictionaryDTO.class);
        if (Objects.isNull(userDictionaryDTO)) {
            log.debug("can not get userDictionary from redis, try to get from remote service");
            return userDictionaryClient.getUserDictionaryByCode(userDictionaryParam);
        }
        return userDictionaryDTO;
    }

    @Override
    public List<UserDictionaryDTO> getUserDictionaryList(UserDictionaryParam userDictionaryParam) {
        List<UserDictionaryDTO> userDictionaryDTOList = dictionaryRedisUtils
                .getListByUserId(userDictionaryParam.getItemCode(), UserDictionaryDTO.class, userDictionaryParam);
        if (CollectionUtils.isEmpty(userDictionaryDTOList)) {
            log.debug("can not get userDictionary from redis, try to get from remote service");
            return userDictionaryClient.getUserDictionaryList(userDictionaryParam);
        }
        return userDictionaryDTOList;
    }

    @Override
    public Pagination<UserDictionaryDTO> getUserDictionaryByItemPage(UserDictionaryPageParam param) {
        List<UserDictionaryDTO> userDictionaryDTOList = dictionaryRedisUtils
                .getListByUserId(param.getItemCode(), UserDictionaryDTO.class, param);
        if (CollectionUtils.isEmpty(userDictionaryDTOList)) {
            log.debug("can not get userDictionary from redis, try to get from remote service");
            return userDictionaryClient.getUserDictionaryByItemPage(param);
        } else {
            long pageL = Long.parseLong(String.valueOf(param.getPageNumber())) - 1;
            long pageSizeL = Long.parseLong(String.valueOf(param.getPageSize()));
            return Pagination.<UserDictionaryDTO>builder()
                    .total((long) userDictionaryDTOList.size())
                    .pages(((userDictionaryDTOList.size() / pageSizeL) + 1))
                    .records(userDictionaryDTOList.stream().skip(pageSizeL * pageL).limit(pageSizeL).collect(Collectors.toList()))
                    .build();
        }
    }

    @Override
    public UserDictionaryDTO addUserDictionary(UserDictionaryAddParam param) {
        return userDictionaryClient.addDataDictionary(param);
    }

    @Override
    public UserDictionaryDTO updateUserDictionary(UserDictionaryModifyParam param) {
        return userDictionaryClient.updateDataDictionary(param);
    }

    @Override
    public void deleteUserDictionary(Long id) {
        userDictionaryClient.deleteDataDictionary(id);
    }
}
