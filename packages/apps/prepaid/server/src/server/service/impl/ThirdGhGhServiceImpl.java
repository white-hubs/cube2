package com.guhecloud.cube.srv.prepaid.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.prepaid.common.constants.Constants;
import com.guhecloud.cube.srv.prepaid.common.vo.AmmeterDTO;
import com.guhecloud.cube.srv.prepaid.common.vo.CollectRecordsDTO;
import com.guhecloud.cube.srv.prepaid.common.vo.PowerRechargeDTO;
import com.guhecloud.cube.srv.prepaid.common.vo.PowerUseDayDTO;
import com.guhecloud.cube.srv.prepaid.server.dao.PullDataRecordMapper;
import com.guhecloud.cube.srv.prepaid.server.po.AmmeterPO;
import com.guhecloud.cube.srv.prepaid.server.po.CollectRecordsPO;
import com.guhecloud.cube.srv.prepaid.server.po.PowerRechargePO;
import com.guhecloud.cube.srv.prepaid.server.po.PowerUseDayPO;
import com.guhecloud.cube.srv.prepaid.server.po.PullDataRecordPO;
import com.guhecloud.cube.srv.prepaid.server.service.ThirdGhService;
import com.guhecloud.cube.srv.prepaid.server.third.client.AmmeterClient;
import com.guhecloud.cube.srv.prepaid.server.third.client.CollectRecordsClient;
import com.guhecloud.cube.srv.prepaid.server.third.client.PowerRechargeClient;
import com.guhecloud.cube.srv.prepaid.server.third.client.PowerUseDayClient;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 第三方服务(广汇)
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdGhGhServiceImpl implements ThirdGhService {

    private final PullDataRecordMapper pullDataRecordMapper;
    private final AmmeterClient ammeterClient;
    private final AmmeterServiceImpl ammeterService;
    private final CollectRecordsClient collectRecordsClient;
    private final CollectRecordsServiceImpl collectRecordsService;
    private final PowerRechargeClient powerRechargeClient;
    private final PowerRechargeServiceImpl powerRechargeService;
    private final PowerUseDayClient powerUseDayClient;
    private final PowerUseDayServiceImpl powerUseDayService;

    /**
     * 全量更新电表信息
     *
     * @param tenant   租户
     * @param nowTime  当前时间
     * @param username 当前用户
     */
    @Override
    @Transactional
    public List<AmmeterDTO> wholePullAmmeter(Integer tenant, LocalDateTime nowTime, String username, String userId,
                                             ActorTypeEnum userType
                                            ) {
        // 插入拉取记录
        Long pullDateRecordId = IdWorker.getId();
        PullDataRecordPO pullDataRecordPO = new PullDataRecordPO(IdWorker.getId(), "AMMETER", nowTime, "WHOLE", null, "FAIL", null, null);
        pullDataRecordPO.setId(pullDateRecordId);
        pullDataRecordPO.setDeleted(false);
        pullDataRecordPO.setTenant(tenant);
        EntityAuditor.populateAudit(pullDataRecordPO, username, userId, userType, nowTime);
        pullDataRecordMapper.insert(pullDataRecordPO);

        List<AmmeterDTO> dtoList = ammeterClient.getAmmeterList(null);
        if (dtoList == null || dtoList.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, AmmeterPO> ammeterPOMap = new HashMap<>();
        List<AmmeterPO> poList = ammeterService.list();
        if (poList != null && !poList.isEmpty()) {
            for (AmmeterPO ammeterPO : poList) {
                if (ammeterPO.getMeterId() == null) {
                    continue;
                }
                ammeterPOMap.put(ammeterPO.getMeterId(), ammeterPO);
            }
        }
        List<AmmeterPO> insertList = new ArrayList<>();
        List<AmmeterPO> updateList = new ArrayList<>();
        for (AmmeterDTO ammeterDTO : dtoList) {
            AmmeterPO oldPo = ammeterPOMap.get(ammeterDTO.getMeterId());
            AmmeterPO po = new AmmeterPO();
            BeanUtils.copyProperties(ammeterDTO, po);
            po.setDeleted(false);
            if (oldPo == null) {
                po.setId(IdWorker.getId());
                po.setTenant(tenant);
                EntityAuditor.populateAudit(po, username, userId, userType, nowTime);
                insertList.add(po);
            } else {
                // 这里id一定要设置跟原来一样的，不然批量更新时匹配不上id（Mybatis-Plus自带的批量更新是根据id修改，除非自己拼批量sql更改修改条件
                po.setId(oldPo.getId());
                po.setTenant(oldPo.getTenant());
                EntityAuditor.populateCreateAudit(po, oldPo.getCreateBy(), oldPo.getCreatorId(), oldPo.getCreatorType(), oldPo.getCreateTime());
                EntityAuditor.populateUpdateAudit(po, username, userId, userType, nowTime);
                updateList.add(po);
            }
        }
        // 批量更新电表
        if (!insertList.isEmpty()) {
            ammeterService.saveBatch(insertList, Constants.SQL_BATH_NUM);
        }
        if (!updateList.isEmpty()) {
            ammeterService.updateBatchById(updateList, Constants.SQL_BATH_NUM);
        }
        // 更新拉取状态
        pullDataRecordMapper.update(null, Wrappers.<PullDataRecordPO>lambdaUpdate()
                                            .eq(PullDataRecordPO::getId, pullDateRecordId)
                                            .set(PullDataRecordPO::getPullResult, "SUCCESS")
                                            .set(PullDataRecordPO::getPullTotal, dtoList.size())
                                            .set(PullDataRecordPO::getUpdateTime, LocalDateTime.now())
                                   );
        return dtoList;
    }

    /**
     * 全量更电表抄表记录
     *
     * @param tenant      租户
     * @param nowTime     当前时间
     * @param username    当前用户
     * @param meterIdList 设备id集合
     */
    @Override
    @Transactional
    public void wholePullCollectRecords(Integer tenant, LocalDateTime nowTime, String username, String userId,
                                        ActorTypeEnum userType, List<String> meterIdList
                                       ) {
        // 插入拉取记录
        Long pullDateRecordId = IdWorker.getId();
        PullDataRecordPO pullDataRecordPO = new PullDataRecordPO(IdWorker.getId(), "COLLECT_RECORDS", nowTime, "WHOLE", null, "FAIL", null, null);
        pullDataRecordPO.setId(pullDateRecordId);
        pullDataRecordPO.setDeleted(false);
        pullDataRecordPO.setTenant(tenant);
        EntityAuditor.populateAudit(pullDataRecordPO, username, userId, userType, nowTime);
        pullDataRecordMapper.insert(pullDataRecordPO);

        if (meterIdList == null) {
            List<AmmeterDTO> ammeterDTOList = ammeterClient.getAmmeterList(null);
            if (ammeterDTOList != null) {
                meterIdList = ammeterDTOList.stream().map(AmmeterDTO::getMeterId).filter(Objects::nonNull).collect(Collectors.toList());
            }
        }
        if (meterIdList == null || meterIdList.isEmpty()) {
            return;
        }

        List<CollectRecordsPO> insertList = new ArrayList<>();
        for (String meterId : meterIdList) {
            // 这里没办法第三方接口只提供了单个查询的，只能在循环里面调用
            CollectRecordsDTO dto = collectRecordsClient.getCollectRecordsDTO(meterId);
            if (dto != null && dto.getMeterId() != null) {
                CollectRecordsPO po = new CollectRecordsPO();
                BeanUtils.copyProperties(dto, po);
                if (StringUtils.isNotBlank(dto.getCreateTime())) {
                    if (dto.getCreateTime().contains("T")) {
                        po.setCollectTime(DateUtils.parse(dto.getCreateTime().substring(0, Math.min(19, dto.getCreateTime()
                                .length())), "yyyy-MM-dd'T'HH:mm:ss"));
                    } else {
                        po.setCollectTime(DateUtils.parse(dto.getCreateTime()));
                    }
                }
                po.setId(IdWorker.getId());
                po.setDeleted(false);
                EntityAuditor.populateAudit(po, username, userId, userType, nowTime);
                insertList.add(po);
            }
        }

        if (!insertList.isEmpty()) {
            collectRecordsService.saveBatch(insertList, Constants.SQL_BATH_NUM);
        }

        // 更新拉取状态
        pullDataRecordMapper.update(null, Wrappers.<PullDataRecordPO>lambdaUpdate()
                                            .eq(PullDataRecordPO::getId, pullDateRecordId)
                                            .set(PullDataRecordPO::getPullResult, "SUCCESS")
                                            .set(PullDataRecordPO::getPullTotal, 1)
                                            .set(PullDataRecordPO::getUpdateTime, LocalDateTime.now())
                                   );

    }

    /**
     * 全量更新电表充值记录
     *
     * @param tenant      租户
     * @param nowTime     当前时间
     * @param username    当前用户
     * @param startDate   获取数据开始时间
     * @param endDate     获取数据结束时间
     * @param meterIdList 设备id集合
     */
    @Override
    @Transactional
    public void wholePullPowerRecharge(Integer tenant, LocalDateTime nowTime, String username, String userId,
                                       ActorTypeEnum userType, LocalDateTime startDate,
                                       LocalDateTime endDate,
                                       List<String> meterIdList
                                      ) {
        // 插入拉取记录
        Long pullDateRecordId = IdWorker.getId();
        PullDataRecordPO pullDataRecordPO = new PullDataRecordPO(IdWorker.getId(), "POWER_RECHARGE", nowTime, "WHOLE", null, "FAIL", null, null);
        pullDataRecordPO.setId(pullDateRecordId);
        pullDataRecordPO.setDeleted(false);
        pullDataRecordPO.setTenant(tenant);
        EntityAuditor.populateAudit(pullDataRecordPO, username, userId, userType, nowTime);
        pullDataRecordMapper.insert(pullDataRecordPO);

        if (meterIdList == null) {
            List<AmmeterDTO> ammeterDTOList = ammeterClient.getAmmeterList(null);
            if (ammeterDTOList != null) {
                meterIdList = ammeterDTOList.stream().map(AmmeterDTO::getMeterId).filter(Objects::nonNull).collect(Collectors.toList());
            }
        }
        if (meterIdList == null || meterIdList.isEmpty()) {
            return;
        }
        List<PowerRechargePO> insertList = new ArrayList<>();
        for (String meterId : meterIdList) {
            // 这里没办法第三方接口只提供了单个表查询的，只能在循环里面调用
            List<PowerRechargeDTO> powerRechargeDTOList = powerRechargeClient.getPowerRechargeList(meterId, startDate, endDate);
            if (powerRechargeDTOList != null) {
                for (PowerRechargeDTO powerRechargeDTO : powerRechargeDTOList) {
                    PowerRechargePO po = new PowerRechargePO();
                    BeanUtils.copyProperties(powerRechargeDTO, po);
                    if (StringUtils.isNotBlank(powerRechargeDTO.getCreateTime())) {
                        if (powerRechargeDTO.getCreateTime().contains("T")) {
                            po.setRechargeTime(DateUtils.parse(powerRechargeDTO.getCreateTime(), "yyyyMMdd'T'HH:mm:ss"));
                        } else {
                            po.setRechargeTime(DateUtils.parse(powerRechargeDTO.getCreateTime()));
                        }
                    }
                    po.setId(IdWorker.getId());
                    po.setDeleted(false);
                    EntityAuditor.populateAudit(po, username, userId, userType, nowTime);
                    po.setUpdaterType(userType);
                    insertList.add(po);
                }
            }
        }
        // 删除原来的
        powerRechargeService.remove(Wrappers.<PowerRechargePO>lambdaQuery()
                                            .ge(PowerRechargePO::getRechargeTime, startDate)
                                            .le(PowerRechargePO::getRechargeTime, endDate)
                                            .in(PowerRechargePO::getMeterId, meterIdList)
                                   );

        if (!insertList.isEmpty()) {
            powerRechargeService.saveBatch(insertList, Constants.SQL_BATH_NUM);
        }

        // 更新拉取状态
        pullDataRecordMapper.update(null, Wrappers.<PullDataRecordPO>lambdaUpdate()
                                            .eq(PullDataRecordPO::getId, pullDateRecordId)
                                            .set(PullDataRecordPO::getPullResult, "SUCCESS")
                                            .set(PullDataRecordPO::getPullTotal, 1)
                                            .set(PullDataRecordPO::getUpdateTime, LocalDateTime.now())
                                   );

    }

    /**
     * 全量更新电表日用能
     *
     * @param tenant      租户
     * @param nowTime     当前时间
     * @param username    当前用户
     * @param startDate   获取数据开始时间
     * @param endDate     获取数据结束时间
     * @param meterIdList 设备id集合
     */
    @Override
    public void wholePullPowerUseDay(Integer tenant, LocalDateTime nowTime, String username, String userId,
                                     ActorTypeEnum userType, LocalDateTime startDate, LocalDateTime endDate,
                                     List<String> meterIdList
                                    ) {
        // 插入拉取记录
        Long pullDateRecordId = IdWorker.getId();
        PullDataRecordPO pullDataRecordPO = new PullDataRecordPO(IdWorker.getId(), "POWER_USE_DAY", nowTime, "WHOLE", null, "FAIL", null, null);
        pullDataRecordPO.setId(pullDateRecordId);
        pullDataRecordPO.setDeleted(false);
        pullDataRecordPO.setTenant(tenant);
        EntityAuditor.populateAudit(pullDataRecordPO, username, userId, userType, nowTime);
        pullDataRecordMapper.insert(pullDataRecordPO);

        if (meterIdList == null) {
            List<AmmeterDTO> ammeterDTOList = ammeterClient.getAmmeterList(null);
            if (ammeterDTOList != null) {
                meterIdList = ammeterDTOList.stream().map(AmmeterDTO::getMeterId).filter(Objects::nonNull).collect(Collectors.toList());
            }
        }
        if (meterIdList == null || meterIdList.isEmpty()) {
            return;
        }
        List<PowerUseDayPO> insertList = new ArrayList<>();
        for (String meterId : meterIdList) {
            // 这里没办法第三方接口只提供了单个表查询的，只能在循环里面调用
            List<PowerUseDayDTO> powerUseDayList = powerUseDayClient.getPowerUseDayList(meterId, startDate, endDate);
            if (powerUseDayList != null) {
                for (PowerUseDayDTO powerUseDayDTO : powerUseDayList) {
                    PowerUseDayPO po = new PowerUseDayPO();
                    BeanUtils.copyProperties(powerUseDayDTO, po);
                    po.setUseMonth(DateUtils.beginOfMonth(powerUseDayDTO.getCreateTime()));
                    po.setUseDate(powerUseDayDTO.getCreateTime());
                    po.setId(IdWorker.getId());
                    po.setDeleted(false);
                    EntityAuditor.populateAudit(po, username, userId, userType, nowTime);
                    insertList.add(po);
                }
            }
        }

        // 删除原来的
        powerUseDayService.remove(Wrappers.<PowerUseDayPO>lambdaQuery()
                                          .ge(PowerUseDayPO::getUseDate, startDate)
                                          .le(PowerUseDayPO::getUseDate, endDate)
                                          .in(PowerUseDayPO::getMeterId, meterIdList)
                                 );

        if (!insertList.isEmpty()) {
            powerUseDayService.saveBatch(insertList, Constants.SQL_BATH_NUM);
        }

        // 更新拉取状态
        pullDataRecordMapper.update(null, Wrappers.<PullDataRecordPO>lambdaUpdate()
                                            .eq(PullDataRecordPO::getId, pullDateRecordId)
                                            .set(PullDataRecordPO::getPullResult, "SUCCESS")
                                            .set(PullDataRecordPO::getPullTotal, 1)
                                            .set(PullDataRecordPO::getUpdateTime, LocalDateTime.now())
                                   );
    }
}
