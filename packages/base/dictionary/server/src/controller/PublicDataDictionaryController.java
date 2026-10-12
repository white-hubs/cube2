package com.guhecloud.cube.base.dictionary.server.controller;

import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryaramParam;
import com.guhecloud.cube.base.dictionary.server.vo.DataDictionaryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 列表字典公共接口
 *
 * @author lanchun
 * @date 2025/1/3 16:38
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicDataDictionaryController {

    private final DataDictionaryController dataDictionaryController;

    /**
     * 根据字典项编码查询字典值
     */
    @GetMapping("/public/getDataDictionaryByItem")
    public List<DataDictionaryVO> getDataDictionaryByItem(@Valid DataDictionaryaramParam param) {
        return dataDictionaryController.getDataDictionaryByItem(param);
    }
}
