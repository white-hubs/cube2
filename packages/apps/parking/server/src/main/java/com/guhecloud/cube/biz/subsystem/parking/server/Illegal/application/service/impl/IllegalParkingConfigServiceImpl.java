package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.command.IllegalParkingConfigCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.service.IllegalParkingConfigService;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.enumeration.IllegalLevelEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingConfigPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.IllegalParkingConfigRepository;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo.IllegalParkingConfigVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 17:02
 * @Description: 违停配置应用服务实现类
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IllegalParkingConfigServiceImpl implements IllegalParkingConfigService {

    private final IllegalParkingConfigRepository illegalParkingConfigRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdate(List<IllegalParkingConfigCommand> param) {
        if (CollectionUtils.isEmpty(param)) {
            return false;
        }
        Set<Long> idList = param.stream().filter(t -> Objects.nonNull(t.getId()))
                .map(IllegalParkingConfigCommand::getId).collect(Collectors.toSet());
        Map<Long, IllegalParkingConfigPO> parkingConfigPOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(idList)) {
            parkingConfigPOMap = Optional.ofNullable(illegalParkingConfigRepository.lambdaQuery()
                                                             .in(IllegalParkingConfigPO::getId, idList)
                                                             .list())
                    .orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(IllegalParkingConfigPO::getId, Function.identity()));
        }
        List<IllegalParkingConfigPO> updateList = new ArrayList<>();
        List<IllegalParkingConfigPO> saveList = new ArrayList<>();
        for (IllegalParkingConfigCommand command : param) {
            if (parkingConfigPOMap.containsKey(command.getId())) {
                IllegalParkingConfigPO illegalParkingConfigPO = parkingConfigPOMap.get(command.getId());
                illegalParkingConfigPO.setTimeCycle(command.getTimeCycle());
                illegalParkingConfigPO.setIllegalLevel(command.getIllegalLevel());
                illegalParkingConfigPO.setScore(command.getScore());
                updateList.add(illegalParkingConfigPO);
            } else {
                IllegalParkingConfigPO illegalParkingConfigPO = IllegalParkingConfigPO.builder()
                        .id(IdWorker.getId())
                        .timeCycle(command.getTimeCycle())
                        .illegalLevel(command.getIllegalLevel())
                        .score(command.getScore())
                        .build();
                saveList.add(illegalParkingConfigPO);
            }
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            illegalParkingConfigRepository.updateBatchById(updateList);
        }
        if (CollectionUtils.isNotEmpty(saveList)) {
            illegalParkingConfigRepository.saveBatch(saveList);
        }
        return true;
    }

    @Override
    public List<IllegalParkingConfigVO> getConfig() {
        List<IllegalParkingConfigPO> illegalParkingConfigPOS = illegalParkingConfigRepository.lambdaQuery()
                .list();
        if (CollectionUtils.isEmpty(illegalParkingConfigPOS)) {
            return new ArrayList<>();
        }
        List<IllegalParkingConfigVO> result = new ArrayList<>();
        for (IllegalParkingConfigPO illegalParkingConfigPO : illegalParkingConfigPOS) {
            result.add(IllegalParkingConfigVO.builder()
                               .id(illegalParkingConfigPO.getId())
                               .timeCycle(illegalParkingConfigPO.getTimeCycle())
                               .illegalLevel(illegalParkingConfigPO.getIllegalLevel())
                               .score(illegalParkingConfigPO.getScore())
                               .build());
        }
        return result;
    }

    @Override
    public IllegalParkingConfigVO getIllegalParkingConfig(IllegalLevelEnum illegalLevelEnum) {
        IllegalParkingConfigPO one = illegalParkingConfigRepository.lambdaQuery()
                .eq(IllegalParkingConfigPO::getIllegalLevel, illegalLevelEnum)
                .one();
        if (Objects.isNull(one)) {
            return null;
        }
        return IllegalParkingConfigVO.builder()
                .id(one.getId())
                .timeCycle(one.getTimeCycle())
                .illegalLevel(one.getIllegalLevel())
                .score(one.getScore())
                .build();
    }

    @Override
    public String getIllegalParkingConfigTimeCycle() {
        IllegalParkingConfigPO one = illegalParkingConfigRepository.lambdaQuery()
                .last("limit 1")
                .one();
        if (Objects.nonNull(one)) {
            return one.getTimeCycle();
        }
        return "";
    }
}