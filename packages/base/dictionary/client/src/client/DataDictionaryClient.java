package com.guhecloud.cube.base.dictionary.client;

import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.fallBack.DataDictionaryClientFallBack;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryParam;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "dictionary", fallback = DataDictionaryClientFallBack.class)
public interface DataDictionaryClient {
    /**
     * 根据id查询字典详情
     */
    @GetMapping("/getDataDictionaryById/{id}")
    @Deprecated
    DataDictionaryDTO getDataDictionaryById(@PathVariable("id") Long id);

    /**
     * 根据id集合批量查询字典详情
     */
    @PostMapping("/getDataDictionaryByIds")
    @Deprecated
    List<DataDictionaryDTO> getDataDictionaryByIds(@RequestBody List<Long> ids);

    /**
     * 根据字典id（用逗号分割）批量查询字典详情
     */
    @GetMapping("/getDataDictionaryByIdsStr")
    @Deprecated
    List<DataDictionaryDTO> getDataDictionaryByIdsStr(@RequestParam("ids") String ids);

    /**
     * 根据字典项编码查询字典值
     */
    @GetMapping("/getDataDictionaryByItem")
    List<DataDictionaryDTO> getDataDictionaryByItem(@SpringQueryMap @Valid DataDictionaryaramParam param);

    /**
     * 根据字典项编码查询字典值分页
     */
    @GetMapping("/getDataDictionaryByItemPage")
    Pagination<DataDictionaryDTO> getDataDictionaryByItemPage(@SpringQueryMap @Valid DataDictionaryPageParam param);

    /**
     * 根据字典项编码+字典编码查询字典值
     */
    @GetMapping("/getDataDictionaryByItemAndCode")
    DataDictionaryDTO getDataDictionaryByItemAndCode(@SpringQueryMap @Valid DataDictionaryaramByItemAndCodeParam param);

    /**
     * 新增字典
     */
    @PostMapping("/addDataDictionary")
    DataDictionaryDTO addDataDictionary(@Valid @RequestBody DataDictionaryAddParam param);

    /**
     * 修改字典
     */
    @PutMapping("/updateDataDictionary")
    DataDictionaryDTO updateDataDictionary(@Valid @RequestBody DataDictionaryParam param);

    /**
     * 删除字典
     */
    @DeleteMapping("/deleteDataDictionary/{id}")
    Boolean deleteDataDictionary(@PathVariable("id") Long id);

}
