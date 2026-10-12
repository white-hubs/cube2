package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerResponse;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerResponseMapper;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-12
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerResponseServiceImpl extends ServiceImpl<WholesalerResponseMapper, WholesalerResponse> implements WholesalerResponseService {

    private final WholesalerResponseMapper mapper;

    @Override
    public void saveOrUpdateResponse(WholesalerResponse response) {
        if (response.getId() != null) {
            mapper.updateById(response);
        } else {
            mapper.insert(response);
        }
    }
}
