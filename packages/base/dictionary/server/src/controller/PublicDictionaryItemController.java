package com.guhecloud.cube.base.dictionary.server.controller;

import com.guhecloud.cube.base.dictionary.server.vo.DictionaryItemVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典项公共接口
 *
 * @author lanchun
 * @date 2025/1/3 16:18
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicDictionaryItemController {

    private final DictionaryItemController dictionaryItemController;

    /**
     * 根据code查询字典项详情
     */
    @GetMapping("/public/getDictionaryItemByCode/{code}")
    public DictionaryItemVO getDictionaryItemByCode(@PathVariable("code") String code) {
        return dictionaryItemController.getDictionaryItemByCode(code);
    }
}
