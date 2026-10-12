package com.guhecloud.cube.deliver.project.merchant.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.BizCodeUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerContractTypeDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerContract;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerContractType;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerContractTypeMapper;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerContractService;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerContractTypeQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerContractTypeService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-13
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerContractTypeServiceImpl extends ServiceImpl<WholesalerContractTypeMapper, WholesalerContractType> implements WholesalerContractTypeService {

    private final WholesalerContractService contractService;
    private final WholesalerContractTypeMapper mapper;

    @Override
    public List<WholesalerContractTypeDTO> getTypes(WholesalerContractTypeQueryDTO queryDTO) {
        List<WholesalerContractType> types = this.lambdaQuery()
                .eq(StrUtil.isNotBlank(queryDTO.getProjectId()), WholesalerContractType::getProjectId, queryDTO.getProjectId())
                .eq(queryDTO.getEnabled() != null, WholesalerContractType::getEnabled, queryDTO.getEnabled())
                .and(StrUtil.isNotBlank(queryDTO.getSearch()), q -> q
                        .like(WholesalerContractType::getCode, queryDTO.getSearch())
                        .or()
                        .like(WholesalerContractType::getName, queryDTO.getSearch())
                        .or()
                        .like(WholesalerContractType::getCreateBy, queryDTO.getSearch())
                    )
                .between(null != queryDTO.getBeginTime() && null != queryDTO.getEndTime(),
                         WholesalerContractType::getCreateTime, queryDTO.getBeginTime(), queryDTO.getEndTime())
                .orderByDesc(WholesalerContractType::getCreateTime)
                .list();
        return new WholesalerContractTypeDTO(types).list();
    }

    @Override
    public String saveOrUpdate(WholesalerContractTypeDTO dto) {
        WholesalerContractType type = dto.convert();
        boolean exist =
                dto.getId() != null && (this.lambdaQuery().eq(WholesalerContractType::getId, dto.getId()).one() != null);
        if (exist) {
            mapper.updateById(type);
        } else {
            type.setCode(BizCodeUtils.generate("HL"));
            type.setCreateBy(SecurityContextHolder.getPrincipal().getName());
            type.setCreateTime(LocalDateTime.now());
            mapper.insert(type);
        }
        return type.getId();
    }

    @Override
    public void removeById(String id) {
        //首先看下面有不有合同
        long count = contractService.lambdaQuery().eq(WholesalerContract::getContractTypeId, id).count();
        if (count == 0) {
            mapper.deleteById(id);
        } else {
            throw new ServiceException("已有创建好的该类型合同，请先删除对应的合同，再来删除该类型");
        }
    }
}
