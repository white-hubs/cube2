package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskMarketPO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.infrastructure.repository.mapper.HeadquartersExamTaskMarketMapper;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class HeadquartersExamTaskMarketService extends ServiceImpl<HeadquartersExamTaskMarketMapper, HeadquartersExamTaskMarketPO> {

    public List<HeadquartersExamTaskMarketPO> getHeadquartersExamTaskMarkets(@NonNull Collection<Long> headquartersExamTaskIds) {
        return this.baseMapper.selectList(Wrappers.<HeadquartersExamTaskMarketPO>lambdaQuery()
                                                  .in(HeadquartersExamTaskMarketPO::getHeadquartersExamTaskId, headquartersExamTaskIds));
    }
}
