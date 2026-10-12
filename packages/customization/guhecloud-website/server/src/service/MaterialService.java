package com.guhecloud.cube.customized.guhecloudwebsite.service;

import com.guhecloud.cube.customized.guhecloudwebsite.param.MaterialParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.MaterialQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.MaterialVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 素材服务接口
 */
public interface MaterialService {

    /**
     * 新增素材
     */
    void add(List<MaterialParam> param);

    /**
     * 删除素材
     */
    void delete(Long id);

    /**
     * 批量删除素材
     */
    void deleteBatch(List<Long> ids);

    /**
     * 查询素材详情
     */
    MaterialVO detail(Long id);

    /**
     * 查询素材
     */
    List<MaterialVO> list(MaterialQueryParam param);

    /**
     * 重命名素材
     * @param id 素材ID
     * @param name 新名称（不含扩展名）
     */
    void rename(Long id, String name);

}
