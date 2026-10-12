package com.guhecloud.cube.base.dictionary.server.controller;

import com.guhecloud.cube.base.dictionary.server.param.UserDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.server.param.UserDictionaryModifyParam;
import com.guhecloud.cube.base.dictionary.server.param.UserDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.server.param.UserDictionaryParam;
import com.guhecloud.cube.base.dictionary.server.service.UserDictionaryService;
import com.guhecloud.cube.base.dictionary.server.vo.UserDictionaryVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户字典
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/28 14:01
 * @Description: 用户级字典Api
 */

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/user-dictionary")
public class UserDictionaryController {
    private final UserDictionaryService userDictionaryService;

    /**
     * 根据编码和字典项获取用户字典
     *
     * @param userDictionaryParam 查询参数
     *
     * @return 用户字典
     */
    @GetMapping("/get/by-code")
    public UserDictionaryVO getUserDictionaryByCode(@Valid UserDictionaryParam userDictionaryParam) {
        return userDictionaryService.getUserDictionaryByCode(userDictionaryParam);
    }

    /**
     * 根据字典项编批量或模糊查询字典项
     */
    @GetMapping("/list")
    public List<UserDictionaryVO> getUserDictionaryList(@Valid UserDictionaryParam userDictionaryParam) {
        return userDictionaryService.getUserDictionaryList(userDictionaryParam);
    }

    /**
     * 根据字典项编码查询字典值分页
     */
    @GetMapping("/page")
    public Pagination<UserDictionaryVO> getUserDictionaryByItemPage(@Valid UserDictionaryPageParam param) {
        return userDictionaryService.getUserDictionaryByItemPage(param);
    }

    /**
     * 新增字典
     */
    @PostMapping("/add")
    public UserDictionaryVO addDataDictionary(@Valid @RequestBody UserDictionaryAddParam param) {
        return userDictionaryService.addUserDictionary(param);
    }

    /**
     * 批量新增字典
     */
    @PostMapping("/add/or/update/batch")
    public Boolean addOrUpdateBatchUserDictionary(@Valid @RequestBody List<UserDictionaryModifyParam> param) {
        return userDictionaryService.addOrUpdateBatchUserDictionary(param);
    }

    /**
     * 修改字典
     */
    @PutMapping("/update")
    public UserDictionaryVO updateDataDictionary(@Valid @RequestBody UserDictionaryModifyParam param) {
        return userDictionaryService.updateUserDictionary(param);
    }

    /**
     * 删除字典
     */
    @DeleteMapping("/delete/{id}")
    public void deleteDataDictionary(@PathVariable("id") Long id) {
        userDictionaryService.deleteUserDictionary(id);
    }
}
