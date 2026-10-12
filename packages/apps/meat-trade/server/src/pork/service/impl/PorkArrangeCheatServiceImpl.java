package com.guhecloud.cube.biz.meattrade.server.pork.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.PorkArrangeCheatMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeCheat;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeCheatModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeCheatService;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PorkArrangeCheatServiceImpl extends ServiceImpl<PorkArrangeCheatMapper, PorkArrangeCheat> implements PorkArrangeCheatService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull PorkArrangeCheatModifyParam param) {
        PorkArrangeCheat porkArrangeCheat = ObjectUtils.copying(param, PorkArrangeCheat.class);
        this.baseMapper.insert(porkArrangeCheat);
        return porkArrangeCheat.getId();
    }
}
