package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerContractTypeDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerContractTypeQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerContractType;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-13
 */
public interface WholesalerContractTypeService extends IService<WholesalerContractType> {


    /**
     * 筛选出合同类型列表
     * @param queryDTO 查询条件
     * @return 合同类型列表
     */
    List<WholesalerContractTypeDTO> getTypes(WholesalerContractTypeQueryDTO queryDTO);
    /**
     * 新增或者修改合同类型
     * @param dto 合同类型DTO
     * @return 返回ID
     */
    String saveOrUpdate(WholesalerContractTypeDTO dto);

    /**
     * 根据ID删除,如果类型下面有合同，那么就不能删除
     * @param id 类型的ID
     */
    void removeById(String id);
}
