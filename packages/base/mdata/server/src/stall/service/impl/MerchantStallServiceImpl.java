package com.guhecloud.cube.biz.mdata.server.stall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.merchant.dao.MerchantStallMapper;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.MerchantStall;
import com.guhecloud.cube.biz.mdata.server.stall.service.MerchantStallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author lzy
 * @date 2021/10/26
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantStallServiceImpl extends ServiceImpl<MerchantStallMapper, MerchantStall> implements MerchantStallService {
    @Override
    public List<MerchantStall> getStallByMerchantId(@NotNull Long merchantId) {
        return this.lambdaQuery().eq(MerchantStall::getMerchantId, merchantId).list();
    }
}
