package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.entity.SensitiveWord;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.BatchDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ErrorExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.SensitiveWordDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.SensitiveWordExcelDTO;

import java.util.List;

/**
 * <p>
 * 敏感词库 服务类
 * </p>
 *
 * @author bq
 * @since 2022-06-14
 */
public interface SensitiveWordService extends IService<SensitiveWord> {

    /**
     * 新增和修改
     * @param word
     */
    void saveOrUpdate(SensitiveWordDTO word);

    /**
     * 批量删除
     * @param dto
     */
    void removeBatch(BatchDTO dto);

    /**
     * 修改初始化的敏感词库
     */
    void updateSensitiveWordInit();

    /**
     * 批量导入
     * @param dtos
     */
    List<ErrorExcelDTO> importData(List<SensitiveWordExcelDTO> dtos);

}
