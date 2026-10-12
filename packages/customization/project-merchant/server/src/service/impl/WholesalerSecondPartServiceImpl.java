package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerSecondPart;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerSecondPartMapper;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerSecondPartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-14
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerSecondPartServiceImpl extends ServiceImpl<WholesalerSecondPartMapper, WholesalerSecondPart> implements WholesalerSecondPartService {

}
