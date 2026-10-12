package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyGateMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyGate;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyGateService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyGateVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 描述：区域分项配置服务层
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyGateServiceImpl extends ServiceImpl<EnergyGateMapper, EnergyGate> implements EnergyGateService {

    private final RedisTemplate redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<EnergyGateVO> getList() {
        //初始化数据
        this.initEnergyGate();
        //查询所有数据
        List<EnergyGate> energyGates = this.baseMapper.selectList(null);
        return ObjectUtils.copying(energyGates, EnergyGateVO.class);
    }

    private void initEnergyGate() {
        Long count = this.baseMapper.selectCount(Wrappers.emptyWrapper());
        if (count <= 0) {
            //初始化数据
            Arrays.stream(InstrumentTypeEnum.values()).forEach(em -> {
                if (InstrumentTypeEnum.ELECTRICITY_METER.getCode().equals(em.getCode())) {
                    this.baseMapper.insert(EnergyGate.builder().energyType(em.getCode()).open(true).build());
                } else {
                    this.baseMapper.insert(EnergyGate.builder().energyType(em.getCode()).open(false).build());
                }
            });
        }
    }

    @Override
    public void updateOpenStatus(Long id, Boolean open) {

        EnergyGate energyGate = this.baseMapper.selectById(id);
        Assert.isNotFound(energyGate, "未找到相关信息，无法修改");
        energyGate.setOpen(open);
        this.updateById(energyGate);
    }

    @Override
    public void updateOccupancyRate(String occupancyRate) {
        redisTemplate.opsForValue().set("occupancyRate", occupancyRate);
    }

    @Override
    public String getOccupancyRate() {
        String a = "1";
        Object o = redisTemplate.opsForValue().get("occupancyRate");
        if (Objects.nonNull(o)) {
            a = o.toString();
        }
        return a;
    }
}