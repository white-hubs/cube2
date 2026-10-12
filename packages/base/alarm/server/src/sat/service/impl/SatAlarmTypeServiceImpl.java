package com.guhecloud.cube.base.alarm.server.sat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.alarm.server.sat.dao.SatAlarmTypeMapper;
import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmType;
import com.guhecloud.cube.base.alarm.server.sat.param.SatAlarmTypeModifyParam;
import com.guhecloud.cube.base.alarm.server.sat.param.SatAlarmTypeQueryParam;
import com.guhecloud.cube.base.alarm.server.sat.service.SatAlarmTypeService;
import com.guhecloud.cube.base.alarm.server.sat.service.SatAlarmTypeTerminalService;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatAlarmTypeServiceImpl extends ServiceImpl<SatAlarmTypeMapper, SatAlarmType> implements SatAlarmTypeService {

    private final SatAlarmTypeTerminalService alarmTypeTerminalService;

    private final FileRefClient fileRefClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull SatAlarmTypeModifyParam param) {
        SatAlarmType satAlarmType = ObjectUtils.copying(param, SatAlarmType.class);
        this.baseMapper.insert(satAlarmType);
        if (!CollectionUtils.isEmpty(param.getTerminals())) {
            this.alarmTypeTerminalService.saveBatch(satAlarmType.getId(), param.getTerminals());
        }
        //保存
        if (Objects.nonNull(param.getFileId())) {
            this.fileRefClient.save(satAlarmType.getId(), param.getFileId());
        }
        return satAlarmType.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull SatAlarmTypeModifyParam param) {
        SatAlarmType satAlarmType = this.getById(id);
        Assert.isNotFound(satAlarmType, "报警类型不存在");
        //移除之前配置的访问端
        this.alarmTypeTerminalService.removeByAlarmTypeId(id);
        if (!CollectionUtils.isEmpty(param.getTerminals())) {
            //新增配置的访问端
            this.alarmTypeTerminalService.saveBatch(satAlarmType.getId(), param.getTerminals());
        }
        ObjectUtils.copying(param, satAlarmType);
        //保存附件
        if (Objects.nonNull(param.getFileId())) {
            this.fileRefClient.save(satAlarmType.getId(), param.getFileId());
        }
        this.baseMapper.updateById(satAlarmType);
    }

    @Override
    public SatAlarmType getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public SatAlarmType getByApplicationCode(@NonNull String applicationCode) {
        return this.baseMapper.selectOne(Wrappers.<SatAlarmType>lambdaQuery()
                                                 .eq(SatAlarmType::getApplicationCode, applicationCode));
    }

    @Override
    public List<SatAlarmType> listAlarmTypes(SatAlarmTypeQueryParam param) {
        return this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getKeyword()), SatAlarmType::getName, param)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
        this.alarmTypeTerminalService.removeByAlarmTypeId(id);
    }
}
