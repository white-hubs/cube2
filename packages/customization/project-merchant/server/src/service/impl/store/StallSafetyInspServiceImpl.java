package com.guhecloud.cube.deliver.project.merchant.service.impl.store;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallSafetyInspService;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallSafetyMarkService;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallSafetyInspDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallSafetyInsp;
import com.guhecloud.cube.deliver.project.merchant.mapper.store.StallSafetyInspMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 档位安全生产巡检扣分记录 服务实现类
 * </p>
 *
 * @author zwb
 * @since 2021-12-23
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallSafetyInspServiceImpl extends ServiceImpl<StallSafetyInspMapper, StallSafetyInsp> implements StallSafetyInspService {

    private final StallSafetyMarkService safetyMarkService;

    @Override
    public void save(List<StallSafetyInspDTO> dtos) {
        if (dtos != null && dtos.size() > 0) {
            //该档位上新增的巡检扣分记录，扣分总和mark
            AtomicInteger mark = new AtomicInteger();
            dtos.stream().forEach(dto -> {
                StallSafetyInsp one = this.lambdaQuery().eq(StallSafetyInsp::getStallId, dto.getStallId())
                        .eq(StallSafetyInsp::getRecordId, dto.getRecordId()).eq(StallSafetyInsp::getItemId, dto.getItemId())
                        .eq(StallSafetyInsp::getItemResultName, dto.getItemResultName()).one();
                if (one == null) {
                    this.save(dto.convert());
                    mark.addAndGet(dto.getItemResultMark());
                } else {
                    dto.setId(one.getId());
                    this.updateById(dto.convert());
                    mark.addAndGet((dto.getItemResultMark() - one.getItemResultMark()));
                }
            });
            //开始扣分
            safetyMarkService.minusScore(dtos.get(0).getStallId(), mark.get(), dtos.size());
        }
    }

    @Override
    public void remove(String id) {
        StallSafetyInsp safetyInsp = this.lambdaQuery().eq(StallSafetyInsp::getId, id).one();
        //将该条记录的分加回去，并且减去一次“巡检扣分次数”（store_safety_mark.insp_total）
        safetyMarkService.minusScore(safetyInsp.getStallId(), -(safetyInsp.getItemResultMark()), -1);
        //然后删掉该记录
        this.removeById(id);

    }
}
