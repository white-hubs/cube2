package com.guhecloud.cube.base.dictionary.client;

import com.guhecloud.cube.base.dictionary.dto.UserDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.UserDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.param.UserDictionaryModifyParam;
import com.guhecloud.cube.base.dictionary.param.UserDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.param.UserDictionaryParam;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/28 16:08
 * @Description:
 */
@FeignClient("dictionary")
public interface UserDictionaryClient {
    /**
     * 根据编码和字典项获取用户字典
     *
     * @param userDictionaryParam 用户字典参数
     *
     * @return 用户字典
     */
    @GetMapping("/user-dictionary/get/by-code")
    UserDictionaryDTO getUserDictionaryByCode(@SpringQueryMap @Valid UserDictionaryParam userDictionaryParam);

    /**
     * 根据字典项编批量或模糊查询字典项
     */
    @GetMapping("/user-dictionary/list")
    List<UserDictionaryDTO> getUserDictionaryList(@SpringQueryMap @Valid UserDictionaryParam userDictionaryParam);

    /**
     * 根据字典项编码查询字典值分页
     */
    @GetMapping("/user-dictionary/page")
    Pagination<UserDictionaryDTO> getUserDictionaryByItemPage(@SpringQueryMap @Valid UserDictionaryPageParam param);

    /**
     * 新增字典
     */
    @PostMapping("/user-dictionary/add")
    UserDictionaryDTO addDataDictionary(@Valid @RequestBody UserDictionaryAddParam param);

    /**
     * 修改字典
     */
    @PutMapping("/user-dictionary/update")
    UserDictionaryDTO updateDataDictionary(@Valid @RequestBody UserDictionaryModifyParam param);

    /**
     * 删除字典
     */
    @DeleteMapping("/user-dictionary/delete/{id}")
    void deleteDataDictionary(@PathVariable("id") Long id);
}
