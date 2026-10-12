package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerQuesTempDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerQuesTemp;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerQuesTempQueryDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-11
 */
public interface WholesalerQuesTempService extends IService<WholesalerQuesTemp> {

    /**
     * 根据查询条件返回模板DTO集合
     * @param queryDto 查询条件
     * @return 返回模板LIST
     */
    List<WholesalerQuesTempDTO> getTempDtoList(WholesalerQuesTempQueryDTO queryDto);

    /**
     * 根据模板ID返回模板详情DTO，包含得有题目等内容
     * @param id 模板ID
     * @return 返回的是模板详情
     */
    WholesalerQuesTempDTO getTempDto(String id);
    /**
     *
     * 需要传入的有模板参数，还有问题
     * @param dto 新增问卷模板
     * @return 返回id
     */
    String saveOrUpdate(WholesalerQuesTempDTO dto );

    /**
     * 根据ID删除模板
     * @param id 模板ID
     */
    void remove(String id);
}
