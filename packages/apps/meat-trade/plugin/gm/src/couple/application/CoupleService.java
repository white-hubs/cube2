package com.guhecloud.cube.gm.meattrade.plugin.couple.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.gm.meattrade.plugin.couple.domain.entity.Couple;
import com.guhecloud.cube.gm.meattrade.plugin.couple.domain.po.CouplePO;
import com.guhecloud.cube.gm.meattrade.plugin.couple.infrastructure.repository.mapper.CoupleMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CoupleService extends ServiceImpl<CoupleMapper, CouplePO> {

    @Transactional(rollbackFor = Exception.class)
    public void saveCouples(List<Couple> couples) {
        if (!CollectionUtils.isEmpty(couples)) {
            List<CouplePO> couplePOS = new ArrayList<>();
            couples.forEach(couple -> {
                couplePOS.add(CouplePO.builder()
                                      .id(couple.getId())
                                      .code(couple.getCode())
                                      .status(couple.getStatus().getCoupleStatus())
                                      .reportTime(couple.getReportTime())
                                      .createTime(LocalDateTime.now())
                                      .updateTime(LocalDateTime.now())
                                      .build());
            });
            this.baseMapper.insertOrUpdateCouples(couplePOS);
        }
    }
}
