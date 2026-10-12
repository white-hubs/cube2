package com.guhecloud.cube.base.dictionary.server.controller;

import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryParam;
import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryaramParam;
import com.guhecloud.cube.base.dictionary.server.service.DataDictionaryServer;
import com.guhecloud.cube.base.dictionary.server.vo.DataDictionaryVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 列表字典
 *
 * @author : lijie
 * @date : 2023-6-27
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataDictionaryController {
    private final DataDictionaryServer dataDictionaryServer;

    /**
     * 根据id查询字典详情
     */
    @GetMapping("/getDataDictionaryById/{id}")
    DataDictionaryVO getDataDictionaryById(@PathVariable("id") Long id) {
        return dataDictionaryServer.getDataDictionaryById(id);
    }

    /**
     * 根据id集合批量查询字典详情
     */
    @PostMapping("/getDataDictionaryByIds")
    List<DataDictionaryVO> getDataDictionaryByIds(@RequestBody List<Long> ids) {
        return dataDictionaryServer.getDataDictionaryByIds(ids);
    }

    /**
     * 根据字典id（用逗号分割）批量查询字典详情
     */
    @GetMapping("/getDataDictionaryByIdsStr")
    List<DataDictionaryVO> getDataDictionaryByIdsStr(String ids) {
        return dataDictionaryServer.getDataDictionaryByIdsStr(ids);
    }

    /**
     * 根据字典项编码查询字典值
     */
    @GetMapping("/getDataDictionaryByItem")
    public List<DataDictionaryVO> getDataDictionaryByItem(@Valid DataDictionaryaramParam param) {
        return dataDictionaryServer.getDataDictionaryByItem(param);
    }

    /**
     * 根据字典项编码查询字典值 (admin) 不限制租户
     */
    @GetMapping("/admin/getDataDictionaryByItem")
    List<DataDictionaryVO> adminGetDataDictionaryByItem(@Valid DataDictionaryaramParam param) {
        return dataDictionaryServer.adminGetDataDictionaryByItem(param);
    }

    /**
     * 根据字典项编码查询字典值分页
     */
    @GetMapping("/getDataDictionaryByItemPage")
    Pagination<DataDictionaryVO> getDataDictionaryByItemPage(@Valid DataDictionaryPageParam param) {
        return dataDictionaryServer.getDataDictionaryByItemPage(param);
    }

    /**
     * 根据字典项编码+字典编码查询字典值
     */
    @GetMapping("/getDataDictionaryByItemAndCode")
    DataDictionaryVO getDataDictionaryByItemAndCode(@Valid DataDictionaryaramByItemAndCodeParam param) {
        return dataDictionaryServer.getDataDictionaryByItemAndCode(param);
    }

    /**
     * 新增字典
     */
    @PostMapping("/addDataDictionary")
    DataDictionaryVO addDataDictionary(@Valid @RequestBody DataDictionaryAddParam param) {
        return dataDictionaryServer.addDataDictionary(param);
    }

    /**
     * 修改字典
     */
    @PutMapping("/updateDataDictionary")
    DataDictionaryVO updateDataDictionary(@Valid @RequestBody DataDictionaryParam param) {
        return dataDictionaryServer.updateDataDictionary(param);
    }

    /**
     * 删除字典
     */
    @DeleteMapping("/deleteDataDictionary/{id}")
    Boolean deleteDataDictionary(@PathVariable("id") Long id) {
        return dataDictionaryServer.deleteDataDictionary(id);
    }

}
