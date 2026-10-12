package com.guhecloud.cube.base.dictionary.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.dictionary.server.entity.UserDictionary;
import com.guhecloud.cube.base.dictionary.server.param.UserDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.server.param.UserDictionaryModifyParam;
import com.guhecloud.cube.base.dictionary.server.param.UserDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.server.param.UserDictionaryParam;
import com.guhecloud.cube.base.dictionary.server.vo.UserDictionaryVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @author 15561
 * @description 针对表【user_dictionary(用户字典)】的数据库操作Service
 * @createDate 2024-10-28 13:51:15
 */
public interface UserDictionaryService extends IService<UserDictionary> {

    /**
     * 根据字典编码获取用户字典
     *
     * @param userDictionaryParam 用户字典参数
     *
     * @return UserDictionaryVO 用户字典VO
     */
    UserDictionaryVO getUserDictionaryByCode(UserDictionaryParam userDictionaryParam);

    /**
     * 获取用户字典列表
     *
     * @param userDictionaryParam 用户字典参数
     *
     * @return List<UserDictionaryVO> 用户字典列表
     */
    List<UserDictionaryVO> getUserDictionaryList(UserDictionaryParam userDictionaryParam);

    /**
     * 分页获取用户字典
     *
     * @param param 查询参数
     *
     * @return Pagination<UserDictionaryVO> 分页用户字典
     */
    Pagination<UserDictionaryVO> getUserDictionaryByItemPage(UserDictionaryPageParam param);

    /**
     * 新增用户字典
     *
     * @param param 新增字典参数
     *
     * @return UserDictionaryVO 新增用户字典
     */
    UserDictionaryVO addUserDictionary(UserDictionaryAddParam param);

    Boolean addOrUpdateBatchUserDictionary(List<UserDictionaryModifyParam> param);

    /**
     * 修改用户字典
     *
     * @param param 修改用户字典参数
     *
     * @return UserDictionaryVO 修改用户字典
     */
    UserDictionaryVO updateUserDictionary(UserDictionaryModifyParam param);

    /**
     * 删除用户字典
     *
     * @param id 用户字典ID
     */
    void deleteUserDictionary(Long id);
}
