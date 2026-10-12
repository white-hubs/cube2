package com.guhecloud.cube.base.dictionary.adapter;

import com.guhecloud.cube.base.dictionary.dto.UserDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.UserDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.param.UserDictionaryModifyParam;
import com.guhecloud.cube.base.dictionary.param.UserDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.param.UserDictionaryParam;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/28 16:03
 * @Description: 用户级字典数据缓存
 */
public interface UserDictionaryAdapter {
    /**
     * 根据字典编码获取用户字典
     *
     * @param userDictionaryParam
     *
     * @return
     */
    UserDictionaryDTO getUserDictionaryByCode(UserDictionaryParam userDictionaryParam);

    /**
     * 获取用户字典列表
     *
     * @param userDictionaryParam
     *
     * @return
     */
    List<UserDictionaryDTO> getUserDictionaryList(UserDictionaryParam userDictionaryParam);

    /**
     * 分页获取用户字典
     *
     * @param param
     *
     * @return
     */
    Pagination<UserDictionaryDTO> getUserDictionaryByItemPage(UserDictionaryPageParam param);

    /**
     * 新增用户字典
     *
     * @param param
     *
     * @return
     */
    UserDictionaryDTO addUserDictionary(UserDictionaryAddParam param);

    /**
     * 修改用户字典
     *
     * @param param
     *
     * @return
     */
    UserDictionaryDTO updateUserDictionary(UserDictionaryModifyParam param);

    /**
     * 删除用户字典
     *
     * @param id
     */
    void deleteUserDictionary(Long id);
}
