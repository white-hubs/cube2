package com.guhecloud.cube.biz.meattrade.server.base.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.base.dao.WeightLossMapper;
import com.guhecloud.cube.biz.meattrade.server.base.entity.WeightLoss;
import com.guhecloud.cube.biz.meattrade.server.base.param.WeightLossCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.WeightLossUpdateParam;
import com.guhecloud.cube.biz.meattrade.server.base.service.WeightLossService;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.cloud.logger.constant.OperationType;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WeightLossServiceImpl extends ServiceImpl<WeightLossMapper, WeightLoss> implements WeightLossService {

    @Override
    @LogRecord(success = "新增重量损耗设置", fail = "新增重量损耗设置失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "WEIGHT_LOSS_CONFIG", subTypeName = "重量损耗设置",
               operationType = OperationType.ADD,
               extra = "{{#param}}",
               bizNo = ""
    )
    public void save(@NonNull WeightLossCreateParam param) {
        WeightLoss weightLoss = ObjectUtils.copying(param, WeightLoss.class);
        // 校验是否存在交叉信息
        this.checkWeightLoss(weightLoss);
        this.save(weightLoss);
    }

    @Override
    @LogRecord(success = "编辑重量损耗设置", fail = "编辑重量损耗设置失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "WEIGHT_LOSS_CONFIG", subTypeName = "重量损耗设置",
               operationType = OperationType.UPDATE,
               extra = "{{#param}}",
               bizNo = "{{#param.id}}"
    )
    public void update(@NonNull WeightLossUpdateParam param) {
        WeightLoss weightLoss = this.getById(param.getId());
        Assert.isNotFound(weightLoss, "不存在重量损耗信息");
        ObjectUtils.copying(param, weightLoss);
        this.checkWeightLoss(weightLoss);
        this.updateById(weightLoss);
    }

    @Override
    public WeightLoss getByTime(long time) {
        WeightLoss loss = this.lambdaQuery()
                .le(WeightLoss::getStartTime, time)//大于等于开始时间
                .gt(WeightLoss::getEndTime, time)//小于结束时间
                .eq(WeightLoss::getEnabled, true)
                .one();
        if (loss == null) {//如果没有匹配到
            loss = this.lambdaQuery().orderByDesc(WeightLoss::getEndTime).last("limit 0,1").one();
            if (loss != null && time >= loss.getEndTime()) {
                return loss;
            } else {
                return null;
            }
        }
        return loss;
    }

    @Override
    public Page<WeightLoss> page(Paging paging) {
        return this.lambdaQuery()
                .orderByAsc(WeightLoss::getStartTime)
                .page(PagingUtils.convert(paging));
    }

    @Override
    @LogRecord(success = "删除重量损耗设置", fail = "删除重量损耗设置失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "WEIGHT_LOSS_CONFIG", subTypeName = "重量损耗设置",
               operationType = OperationType.DELETE,
               bizNo = "{{#id}}"
    )
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    private void checkWeightLoss(@NonNull WeightLoss weightLoss) {
        List<WeightLoss> weightLossList = this.list();
        if (Objects.nonNull(weightLoss.getId())) {
            // 编辑操作，去掉本身
            weightLossList = weightLossList.stream().filter(wl -> !weightLoss.getId().equals(wl.getId())).collect(Collectors.toList());
        }
        for (WeightLoss loss : weightLossList) {
            if (weightLoss.getStartTime() > loss.getStartTime() && weightLoss.getStartTime() < loss.getEndTime()) {
                throw new ServiceException(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()), "该时间段已被设置!");
            } else if (weightLoss.getStartTime() <= loss.getStartTime() && weightLoss.getEndTime() > loss.getStartTime()) {
                throw new ServiceException(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()), "该时间段已被设置!");
            }
            if (weightLoss.getEndTime() > loss.getStartTime() && weightLoss.getEndTime() < loss.getEndTime()) {
                throw new ServiceException(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()), "该时间段已被设置!");
            }
        }
    }
}
