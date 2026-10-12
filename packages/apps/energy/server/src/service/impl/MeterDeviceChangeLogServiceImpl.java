package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyProjectItemMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.MeterDeviceChangeLogMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.MeterDeviceMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProjectItem;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDevice;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDeviceChangeLog;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.DateEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterDeviceChangeLogParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterDeviceChangeLogQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.MeterDeviceChangeLogService;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.InfluxdbUtil;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DataVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterDeviceChangeLogVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ReadMeterLogVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 描述：仪表设备更换记录服务层
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeterDeviceChangeLogServiceImpl extends ServiceImpl<MeterDeviceChangeLogMapper, MeterDeviceChangeLog> implements MeterDeviceChangeLogService {
    private final EnergyProjectItemMapper energyProjectItemMapper;
    private final MeterDeviceMapper meterDeviceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(MeterDeviceChangeLogParam param) {
        MeterDevice oldMeter = meterDeviceMapper.selectOne(Wrappers.<MeterDevice>lambdaQuery()
                                                                   .eq(MeterDevice::getDeviceId, param.getDeviceId()).last(" limit 1 "));
        if (oldMeter == null) {
            throw new ServiceException("设备不存在，请检查是否被删除！");
        }
        MeterDeviceChangeLog meterDeviceChangeLog = new MeterDeviceChangeLog();
        meterDeviceChangeLog.setDeviceId(param.getDeviceId());
        meterDeviceChangeLog.setInitValue(param.getInitValue());
        //操作人姓名
        meterDeviceChangeLog.setOptUserName(SecurityContextHolder.getPrincipal().getName());
        meterDeviceChangeLog.setOptTime(LocalDateTime.now());
        //换表操作
        BigDecimal changedDeviceValue = this.changeDevice(meterDeviceChangeLog);
        meterDeviceChangeLog.setChangedDeviceValue(changedDeviceValue);

        meterDeviceChangeLog.setInitValue(oldMeter.getInitValue());
        // 修改新表的初始读数
        meterDeviceMapper.update(null, Wrappers.<MeterDevice>lambdaUpdate()
                                         .eq(MeterDevice::getDeviceId, param.getDeviceId())
                                         .set(MeterDevice::getInitValue, param.getInitValue())
                                );
        this.baseMapper.insert(meterDeviceChangeLog);
        return meterDeviceChangeLog.getId();
    }

    //更换设备 需要覆盖：deviceHour\projectHour\areaHour\readMeterLogTable 这几张表中的设备当前读数

    /**
     * @param changeDevice 里面有新设备的当前读数(新设备的初始读数)
     *
     * @return 旧设备的最新读数
     */
    private BigDecimal changeDevice(MeterDeviceChangeLog changeDevice) {
        //得到新设备的原始读数
        BigDecimal initValue = changeDevice.getInitValue();
        //更新deviceHour
        BigDecimal latestValue = this.updateDeviceHour(initValue, changeDevice.getDeviceId());
        //更新projectHour
        this.updateProjectHour(initValue, changeDevice.getDeviceId());
        //更新areaHour
        this.updateAreaHour(initValue, changeDevice.getDeviceId());
        //更新抄表记录
        this.updateReadMeterLog(initValue, changeDevice.getDeviceId());

        return latestValue;
    }

    //更新抄表记录
    private void updateReadMeterLog(BigDecimal initValue, Long deviceId) {
        String influxdbQuery = InfluxdbUtil.getLatestReadMeterLogTableQuery(deviceId.toString());
        //查询库中最新的记录
        List<ReadMeterLogVO> preReadMeterLogList = InfluxdbUtil.execQueryForList(influxdbQuery, ReadMeterLogVO.class);
        if (CollectionUtils.isNotEmpty(preReadMeterLogList)) {
            preReadMeterLogList.stream().forEach(d -> {
                //设置当前读数
                d.setReadValue(initValue);
                Point pointForMeterLog = d.createPointForMeterLog(d);
                InfluxdbUtil.writePoint(pointForMeterLog);
            });
        }
    }

    //更新areaHour表中的设备
    private void updateAreaHour(BigDecimal initValue, Long deviceId) {
        //通过设备ID 查出设备在哪些分项下
        List<EnergyProjectItem> deviceList = energyProjectItemMapper.selectList(Wrappers.<EnergyProjectItem>lambdaQuery()
                                                                                        .eq(EnergyProjectItem::getDeviceId, deviceId)
                                                                                        .eq(EnergyProjectItem::getBindType, "AREA"));
        if (CollectionUtils.isNotEmpty(deviceList)) {
            deviceList.stream().forEach(d -> {
                DataVo areaLatestDataInDB = InfluxdbUtil.getAreaLatestDataInDB(LocalDateTime.now(), DateEnum.Hour, deviceId, d.getProjectId());
                if (areaLatestDataInDB != null) {
                    //查询区域分项详细信息
                    Point projectHourPoint = this.createAreaHourPoint(areaLatestDataInDB, initValue);
                    InfluxdbUtil.writePoint(projectHourPoint);
                }
            });
        }
    }

    //更新projectHour表中的设备
    private void updateProjectHour(BigDecimal initValue, Long deviceId) {
        //通过设备ID 查出设备在哪些分项下
        List<EnergyProjectItem> deviceList = energyProjectItemMapper.selectList(Wrappers.<EnergyProjectItem>lambdaQuery()
                                                                                        .eq(EnergyProjectItem::getDeviceId, deviceId)
                                                                                        .eq(EnergyProjectItem::getBindType, "ITEM"));
        if (CollectionUtils.isNotEmpty(deviceList)) {
            deviceList.stream().forEach(d -> {
                DataVo itemLatestDataInDB = InfluxdbUtil.getItemLatestDataInDB(LocalDateTime.now(), DateEnum.Hour, deviceId, d.getProjectId());
                if (itemLatestDataInDB != null) {
                    Point projectHourPoint = this.createProjectHourPoint(itemLatestDataInDB, initValue);
                    InfluxdbUtil.writePoint(projectHourPoint);
                }
            });
        }
    }

    //更新deviceHour表中的设备
    private BigDecimal updateDeviceHour(BigDecimal initValue, Long deviceId) {
        DataVo deviceLatestDataInDB = InfluxdbUtil.getDeviceLatestDataInDB(LocalDateTime.now(), DateEnum.Hour, deviceId.toString());
        if (deviceLatestDataInDB != null) {
            BigDecimal latestValue = deviceLatestDataInDB.getReadeValue();
            Point deviceHourPoint = this.createDeviceHourPoint(deviceLatestDataInDB, initValue);
            InfluxdbUtil.writePoint(deviceHourPoint);
            return latestValue;
        }
        return null;
    }

    private Point createDeviceHourPoint(DataVo data, BigDecimal readValue) {
        return Point.measurement("deviceHour")
                .addTag("deviceId", data.getDeviceId())//设备id
                .addTag("tenant", data.getTenant())
                .addTag("instrumentType", data.getInstrumentType())
                .addField("deviceName", data.getDeviceName())//设备名称
                .addField("readeValue", readValue.setScale(2, RoundingMode.HALF_UP))
                .addField("value", data.getValue())
                .addField("pvalue", data.getPvalue())
                .addField("fvalue", data.getFvalue())
                .addField("gvalue", data.getGvalue())
                .addField("jvalue", data.getJvalue())
                .addField("totalValue", data.getTotalValue())
                .time(data.getTime(), WritePrecision.S);
    }

    private Point createProjectHourPoint(DataVo data, BigDecimal readValue) {
        return Point.measurement("projectHour")
                .addTag("deviceId", data.getDeviceId())//设备id
                .addTag("projectId", data.getProjectId())
                .addTag("tenant", data.getTenant())
                .addTag("instrumentType", data.getInstrumentType())
                .addField("projectName", data.getProjectName())
                .addField("value", data.getValue())
                .addField("readeValue", readValue.setScale(2, RoundingMode.HALF_UP))
                .addField("totalValue", data.getTotalValue())
                .time(data.getTime(), WritePrecision.S);
    }

    private Point createAreaHourPoint(DataVo data, BigDecimal readValue) {
        return Point.measurement("areaHour")
                .addTag("deviceId", data.getDeviceId())//设备id
                //                .addTag("sequence",data.getSequence())
                .addTag("areaId", data.getAreaId())
                .addTag("tenant", data.getTenant())
                .addTag("instrumentType", data.getInstrumentType())
                .addField("areaName", data.getAreaName())
                .addField("value", data.getValue())
                .addField("readeValue", readValue.setScale(2, RoundingMode.HALF_UP))
                //                .addField("pvalue", data.getPvalue())
                //                .addField("fvalue", data.getFvalue())
                //                .addField("gvalue", data.getGvalue())
                //                .addField("jvalue", data.getJvalue())
                .addField("totalValue", data.getTotalValue())
                .time(data.getTime(), WritePrecision.S);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, MeterDeviceChangeLogParam param) {
        MeterDeviceChangeLog meterDeviceChangeLog = this.baseMapper.selectById(id);
        Assert.isNotFound(meterDeviceChangeLog, "未查询到相关信息");
        ObjectUtils.copying(param, meterDeviceChangeLog);
        this.baseMapper.updateById(meterDeviceChangeLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.stream().forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public MeterDeviceChangeLogVO getById(Long id) {
        MeterDeviceChangeLog meterDeviceChangeLog = this.baseMapper.selectById(id);
        return ObjectUtils.copying(meterDeviceChangeLog, MeterDeviceChangeLogVO.class);
    }

    @Override
    public Pagination<MeterDeviceChangeLogVO> getPage(MeterDeviceChangeLogQueryParam param) {
        //请自行添加查询条件
        Page<MeterDeviceChangeLog> page = this.lambdaQuery()
                .eq(param.getDeviceId() != null, MeterDeviceChangeLog::getDeviceId, param.getDeviceId())
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, MeterDeviceChangeLogVO.class);
    }
}