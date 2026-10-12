package com.guhecloud.cube.base.dictionary.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.dictionary.server.entity.CascadeDictionary;
import com.guhecloud.cube.base.dictionary.server.param.CascadeDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.server.param.CascadeDictionaryDetailParam;
import com.guhecloud.cube.base.dictionary.server.param.CascadeDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.server.param.CascadeDictionaryParam;
import com.guhecloud.cube.base.dictionary.server.param.CascadeDictionaryUpdateParam;
import com.guhecloud.cube.base.dictionary.server.vo.CascadeDictionaryVO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

public interface CascadeDictionaryService extends IService<CascadeDictionary> {
    /**
     * 查询字典详情
     */
    CascadeDictionaryVO getCascadeDictionaryById(Long id);

    /**
     * 查询字典详情
     */
    CascadeDictionaryVO getCascadeDictionary(CascadeDictionaryDetailParam param);

    /**
     * 根据字典项查询字典树
     */
    List<CascadeDictionaryVO> getCascadeDictionaryItem(CascadeDictionaryParam param);

    Pagination<CascadeDictionaryVO> getCascadeDictionaryTreeByPage(CascadeDictionaryPageParam param);

    List<CascadeDictionaryVO> getAllCascadeDictionaryItem(CascadeDictionaryParam param);

    /**
     * 新增字典
     */
    CascadeDictionaryVO addCascadeDictionary(CascadeDictionaryAddParam param);

    /**
     * 修改字典
     */
    @PutMapping("/updateDataDictionary")
    CascadeDictionaryVO updateCascadeDictionary(CascadeDictionaryUpdateParam param);

    /**
     * 删除字典
     */
    Boolean deleteCascadeDictionary(Long id);

    /**
     * 返回全部有效业务实体对象.
     *
     * @return 业务实体对象列表
     */
    List<CascadeDictionary> getByTenant(Long tenantId);
}
