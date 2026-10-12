package com.guhecloud.cube.customized.guhecloudwebsite.service;

import com.guhecloud.cube.customized.guhecloudwebsite.param.MaterialFolderBatchUpdateParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.MaterialFolderParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.MaterialFolderVO;

import java.util.List;

/**
 * 素材文件夹服务接口
 */
public interface MaterialFolderService {

    /**
     * 新增文件夹
     */
    void add(MaterialFolderParam param);

    /**
     * 重命名文件夹名称
     */
    void update(MaterialFolderParam param);

    /**
     * 删除文件夹
     */
    void delete(Long id);

    /**
     * 批量删除文件夹
     */
    void deleteBatch(List<Long> ids);

    /**
     * 批量修改文件夹
     */
    void updateBatch(MaterialFolderBatchUpdateParam param);

    /**
     * 查询文件夹详情
     */
    MaterialFolderVO detail(Long id);

    /**
     * 查询文件夹
     */
    List<MaterialFolderVO> list(Long pathId);

}
