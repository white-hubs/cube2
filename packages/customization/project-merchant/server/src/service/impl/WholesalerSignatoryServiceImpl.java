package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerSignatory;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerSignatoryService;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerSignatoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-13
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerSignatoryServiceImpl extends ServiceImpl<WholesalerSignatoryMapper, WholesalerSignatory> implements WholesalerSignatoryService {

}
