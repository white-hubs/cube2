package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerProtocolTemplateDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerProtocolTemplate;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-10
 */
public interface WholesalerProtocolTemplateService extends IService<WholesalerProtocolTemplate> {

    String PROTOCOL_FILE = "protocol-file";

    /**
     * 查看模板详情
     * @param id id
     * @return 模板DTO
     */
    WholesalerProtocolTemplateDTO getTemplateById(String id);
    /**
     * 新增单个模板
     * @param dto 模板DTO
     * @return 新增记录的ID
     */
    String save(WholesalerProtocolTemplateDTO dto);

    /**
     * 编辑模板详情
     * @param dto 模板详情dto
     * @return 返回id
     */
    String update(WholesalerProtocolTemplateDTO dto);
    /**
     * 删除模板ID，并且将对应的文件也删除掉
     * @param id 模板ID
     */
    void removeById(String id);

    List<WholesalerProtocolTemplate> getTemplateList(String projectId);
}
