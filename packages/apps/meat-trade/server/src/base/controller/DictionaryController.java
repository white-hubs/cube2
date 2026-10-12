package com.guhecloud.cube.biz.meattrade.server.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Dictionary;
import com.guhecloud.cube.biz.meattrade.server.base.param.DictionaryCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.DictionaryQueryParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.DictionaryUpdateParam;
import com.guhecloud.cube.biz.meattrade.server.base.service.DictionaryService;
import com.guhecloud.cube.biz.meattrade.server.base.vo.DictionaryPageVO;
import com.guhecloud.cube.biz.meattrade.server.base.vo.DictionaryVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 字典接口信息
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DictionaryController {

    private final DictionaryService dictionaryService;

    /**
     * 字典新增
     *
     * @param param 字典新增信息
     */
    @PostMapping("/dictionary")
    public void save(@Valid @RequestBody DictionaryCreateParam param) {
        dictionaryService.save(param);
    }

    /**
     * 字典更新
     *
     * @param param 字典更新信息
     */
    @PutMapping("/dictionary")
    public void update(@Valid @RequestBody DictionaryUpdateParam param) {
        dictionaryService.update(param);
    }

    /**
     * 字典ID查询
     *
     * @param id 字典主键
     *
     * @return 字典信息
     */
    @GetMapping("/dictionary/{id}")
    public DictionaryVO getById(@PathVariable("id") Long id) {
        Dictionary dictionary = dictionaryService.getById(id);
        return ObjectUtils.copying(dictionary, DictionaryVO.class);
    }

    /**
     * 字典分页查询
     *
     * @param queryParam 查询参数
     *
     * @return 字典信息集合
     */
    @GetMapping("/dictionary/page")
    public Pagination<DictionaryPageVO> page(DictionaryQueryParam queryParam) {
        Page<Dictionary> page = dictionaryService.page(queryParam);
        List<DictionaryPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), DictionaryPageVO.class);
        return PagingUtils.convert(page, pageVOS);
    }

    /**
     * 根据类型查找
     *
     * @param type 类型
     *
     * @return 字典信息集合
     */
    @GetMapping("/dictionary/type")
    public List<DictionaryVO> getByType(@RequestParam("type") String type) {
        List<Dictionary> dictionaries = dictionaryService.getByType(type);
        return ObjectUtils.copying(dictionaries, DictionaryVO.class);
    }
}
