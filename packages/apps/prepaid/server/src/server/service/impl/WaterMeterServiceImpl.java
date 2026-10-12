package com.guhecloud.cube.srv.prepaid.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.prepaid.common.constants.Constants;
import com.guhecloud.cube.srv.prepaid.common.vo.WaterMeterDTO;
import com.guhecloud.cube.srv.prepaid.server.dao.PullDataRecordMapper;
import com.guhecloud.cube.srv.prepaid.server.dao.WaterMeterMapper;
import com.guhecloud.cube.srv.prepaid.server.param.WaterMeterPageParam;
import com.guhecloud.cube.srv.prepaid.server.po.PullDataRecordPO;
import com.guhecloud.cube.srv.prepaid.server.po.WaterMeterPO;
import com.guhecloud.cube.srv.prepaid.server.result.WaterMeterPageResult;
import com.guhecloud.cube.srv.prepaid.server.service.WaterMeterService;
import com.guhecloud.cube.srv.prepaid.server.third.client.WaterMeterClient;
import com.guhecloud.cube.srv.prepaid.server.utils.PageResponse;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 水表服务
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WaterMeterServiceImpl extends ServiceImpl<WaterMeterMapper, WaterMeterPO> implements WaterMeterService {

    private final PullDataRecordMapper pullDataRecordMapper;
    private final WaterMeterClient waterMeterClient;

    /**
     * 水表分页列表
     *
     * @param param 条件入参对象
     *
     * @return 分页列表
     */
    @Override
    public Pagination<WaterMeterPageResult> getWaterMeterPage(WaterMeterPageParam param) {
        List<WaterMeterDTO> dtoList = waterMeterClient.getWaterMeterList(param.getMeterId());
        // 条件
        if (param.getOweStatus() != null || param.getUnConnect() != null) {
            Stream<WaterMeterDTO> stream = dtoList.stream();
            if (param.getOweStatus() != null) {
                String oweStatus;
                if (Boolean.TRUE.equals(param.getOweStatus())) {
                    oweStatus = "是";
                } else {
                    oweStatus = "否";
                }
                stream = stream.filter(dto -> StringUtils.isNotBlank(dto.getOweStatus()) && oweStatus.equals(dto.getOweStatus()));
            }
            if (param.getUnConnect() != null) {
                stream = stream.filter(dto -> dto.getUnConnect() != null && param.getUnConnect().equals(dto.getUnConnect()));
            }
            dtoList = stream.collect(Collectors.toList());
        }
        Pagination<WaterMeterDTO> pagination = PageResponse.doPage(dtoList, param.getPageNumber(), param.getPageSize());

        return new Pagination<>(pagination.getTotal(), pagination.getPages(), pagination.getRecords().stream().map(po -> {
            WaterMeterPageResult waterMeterPageResult = new WaterMeterPageResult();
            BeanUtils.copyProperties(po, waterMeterPageResult);
            waterMeterPageResult.setOweStatus("是".equals(po.getOweStatus()));
            return waterMeterPageResult;
        }).collect(Collectors.toList()));
    }

    /**
     * 同步水表信息
     *
     * @param dtoList
     * @param tenant
     * @param nowTime
     * @param username
     * @param userId
     * @param userType
     */
    @Override
    public void wholePull(List<WaterMeterDTO> dtoList, Integer tenant, LocalDateTime nowTime, String username, String userId,
                          ActorTypeEnum userType
                         ) {
        if (dtoList == null || dtoList.isEmpty()) {
            return;
        }

        Map<String, WaterMeterPO> waterMeterPOMap = new HashMap<>();
        List<WaterMeterPO> poList = this.list();
        if (poList != null && !poList.isEmpty()) {
            for (WaterMeterPO waterMeterPO : poList) {
                if (waterMeterPO.getMeterId() == null) {
                    continue;
                }
                waterMeterPOMap.put(waterMeterPO.getMeterId(), waterMeterPO);
            }
        }
        List<WaterMeterPO> insertList = new ArrayList<>();
        List<WaterMeterPO> updateList = new ArrayList<>();
        for (WaterMeterDTO waterMeterDTO : dtoList) {
            WaterMeterPO oldPo = waterMeterPOMap.get(waterMeterDTO.getMeterId());
            WaterMeterPO po = new WaterMeterPO();
            BeanUtils.copyProperties(waterMeterDTO, po);
            po.setDeleted(false);
            if (oldPo == null) {
                po.setId(IdWorker.getId());
                po.setTenant(tenant);
                EntityAuditor.populateAudit(po, username, userId, userType, nowTime);
                insertList.add(po);
            } else {
                po.setId(oldPo.getId());
                po.setTenant(oldPo.getTenant());
                EntityAuditor.populateCreateAudit(po, oldPo.getCreateBy(), oldPo.getCreatorId(), oldPo.getCreatorType(), oldPo.getCreateTime());
                EntityAuditor.populateUpdateAudit(po, username, userId, userType, nowTime);
                updateList.add(po);
            }
        }

        if (!insertList.isEmpty()) {
            this.saveBatch(insertList, Constants.SQL_BATH_NUM);
        }
        if (!updateList.isEmpty()) {
            this.updateBatchById(updateList, Constants.SQL_BATH_NUM);
        }

        // 插入拉取记录
        PullDataRecordPO pullDataRecordPO = new PullDataRecordPO(IdWorker.getId(), "AMMETER", nowTime, "WHOLE", (long) dtoList.size(), "SUCCESS", null, null);
        pullDataRecordPO.setDeleted(false);
        pullDataRecordPO.setTenant(tenant);
        EntityAuditor.populateAudit(pullDataRecordPO, username, userId, userType, nowTime);
        pullDataRecordMapper.insert(pullDataRecordPO);
    }
}
