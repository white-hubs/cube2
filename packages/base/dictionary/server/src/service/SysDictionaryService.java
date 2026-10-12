package com.guhecloud.cube.base.dictionary.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.dictionary.server.entity.SysDictionary;
import com.guhecloud.cube.base.dictionary.server.param.SysDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.server.param.SysDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.server.param.SysDictionaryParam;
import com.guhecloud.cube.base.dictionary.server.param.SysDictionaryaramParam;
import com.guhecloud.cube.base.dictionary.server.vo.SysDictionaryVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface SysDictionaryService extends IService<SysDictionary> {
    /**
     * 根据id查询字典详情
     */
    SysDictionaryVO getSysDictionaryById(Long id);

    /**
     * 根据id集合批量查询字典详情
     */
    List<SysDictionaryVO> getSysDictionaryByIds(List<Long> ids);

    /**
     * 根据字典id（用逗号分割）批量查询字典详情
     */
    List<SysDictionaryVO> getSysDictionaryByIdsStr(String ids);

    SysDictionaryVO getSysDictionaryByCode(String itemCode, String code);

    /**
     * 根据字典项编码查询字典值
     */
    List<SysDictionaryVO> getSysDictionaryByItem(SysDictionaryaramParam param);

    /**
     * 根据字典项编码查询字典值分页
     */
    Pagination<SysDictionaryVO> getSysDictionaryByItemPage(SysDictionaryPageParam param);

    SysDictionaryVO addSysDictionary(SysDictionaryAddParam param);

    SysDictionaryVO updateSysDictionary(SysDictionaryParam param);

    Boolean deleteSysDictionary(Long id);

    /**
     * 内部使用: 获取全部系统字典项. 不得在业务代码中使用.
     *
     * @return List<SysDictionaryVO> 系统字典项列表
     */
    List<SysDictionaryVO> getAll();

    /**
     * 内部使用， 暴露批量更新或新增能力。
     *
     * @param entities 批量更新的字典项列表
     *
     * @return boolean 操作是否成功
     */
    boolean batchSaveOrUpdate(List<SysDictionary> entities);

    /**
     * 内部使用，暴露批量删除能力。
     */
    int batchRemoveById(List<Long> idsToRemove);

    /**
     * 获取系统能力配置
     */
    List<SysDictionaryVO> getSysAbilityConfig();
}
