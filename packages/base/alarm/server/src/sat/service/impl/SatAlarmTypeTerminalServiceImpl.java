package com.guhecloud.cube.base.alarm.server.sat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.alarm.server.sat.dao.SatAlarmTypeTerminalMapper;
import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmTypeTerminal;
import com.guhecloud.cube.base.alarm.server.sat.param.SatAlarmTypeTerminalModifyParam;
import com.guhecloud.cube.base.alarm.server.sat.service.SatAlarmTypeTerminalService;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatAlarmTypeTerminalServiceImpl extends ServiceImpl<SatAlarmTypeTerminalMapper, SatAlarmTypeTerminal> implements SatAlarmTypeTerminalService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(@NonNull Long alarmTypeId, @NonNull List<SatAlarmTypeTerminalModifyParam> terminals) {
        List<SatAlarmTypeTerminal> satAlarmTypeTerminals = new ArrayList<>();
        terminals.stream().forEach(terminal -> {
            SatAlarmTypeTerminal satAlarmTypeTerminal = new SatAlarmTypeTerminal();
            satAlarmTypeTerminal.setAlarmTypeId(alarmTypeId);
            satAlarmTypeTerminal.setTerminal(terminal.getTerminal());
            satAlarmTypeTerminal.setTerminalName(terminal.getTerminalName());
            satAlarmTypeTerminals.add(satAlarmTypeTerminal);
        });
        this.saveBatch(satAlarmTypeTerminals);
    }

    @Override
    public List<SatAlarmTypeTerminal> listAlarmTypeTerminalsByAlarmTypeId(@NonNull Long alarmTypeId) {
        return this.lambdaQuery().eq(SatAlarmTypeTerminal::getAlarmTypeId, alarmTypeId).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByAlarmTypeId(@NonNull Long alarmTypeId) {
        List<SatAlarmTypeTerminal> satAlarmTypeTerminals = this.listAlarmTypeTerminalsByAlarmTypeId(alarmTypeId);
        if (!CollectionUtils.isEmpty(satAlarmTypeTerminals)) {
            DatabaseContextHolder.softDelete(this, satAlarmTypeTerminals);
        }
    }
}
