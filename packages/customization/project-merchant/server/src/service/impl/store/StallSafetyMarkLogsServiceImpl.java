package com.guhecloud.cube.deliver.project.merchant.service.impl.store;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.mapper.store.StallSafetyMarkLogsMapper;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallSafetyMarkLogsService;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallSafetyMarkService;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallSafetyMarkLogsDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallSafetyMarkLogs;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 档位安全生产分值修改记录 服务实现类
 * </p>
 *
 * @author benhuabao
 * @since 2021-12-27
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallSafetyMarkLogsServiceImpl extends ServiceImpl<StallSafetyMarkLogsMapper, StallSafetyMarkLogs> implements StallSafetyMarkLogsService {

    private final StallSafetyMarkLogsMapper mapper;
    private final StallSafetyMarkService ssService;

    @Override
    public String save(StallSafetyMarkLogsDTO dto) {
        StallSafetyMarkLogs logs  = dto.convert();
        logs.setCreateTime(LocalDateTime.now());
        logs.setCreateBy( SecurityContextHolder.getPrincipal().getName());
        logs.setDeleted(Boolean.FALSE);
        //添加记录，
        mapper.insert(logs);
        //size =0 ，只修改分数，不修改巡检扣分项数
        //dto.getPoint()负数，就是减分，正数就是加分
        ssService.minusScore(dto.getStallId(), -dto.getPoint(), 0);
        return logs.getId();
    }

    @Override
    public void remove(String id) {
        StallSafetyMarkLogs log = this.lambdaQuery().eq(StallSafetyMarkLogs::getId,id).one();
        //log.getPoint()如果是正数，那么删除后，就是减分，否则就是加分
        ssService.minusScore(log.getStallId(), log.getPoint(), 0);
        mapper.deleteById(id);
    }
}
