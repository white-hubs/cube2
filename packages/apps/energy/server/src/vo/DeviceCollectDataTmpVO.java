package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.DateEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.FPGTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.InfluxdbUtil;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.MeterUsageCalculator;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_DATE_FORMAT;
import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_MONTH_FORMAT;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCollectDataTmpVO {
    private BigDecimal lastReadeValue = new BigDecimal("0.00");//上次读数
    private BigDecimal collectionNum = new BigDecimal("0.00");//本次采集量
    private BigDecimal pcollectionNum = new BigDecimal("0.00");//平本次采集量
    private BigDecimal fcollectionNum = new BigDecimal("0.00");//峰本次采集量
    private BigDecimal gcollectionNum = new BigDecimal("0.00");//谷本次采集量
    private BigDecimal jcollectionNum = new BigDecimal("0.00");//尖本次采集量
    private BigDecimal totalValue = new BigDecimal("0.00");//累计采集量
    private BigDecimal readeValue = new BigDecimal("0.00");
    private MeterUsageCalculator.Result usageResult;

    public DeviceCollectDataTmpVO(BigDecimal readeValue) {
        this.readeValue = readeValue;
    }

    /**
     * 填充模板数据
     * 如果本次采集时间在当前聚合时间内，则填充上次的数据，以便计算
     * preCollectTime 是上次聚合的那个时间
     */
    public MeterUsageCalculator.Result fillTmpData(LocalDateTime currentTime,
                                                   Instant preCollectTime,
                                                   DataVo lastData,
                                                   BigDecimal currentVal,
                                                   DateEnum dateEnum,
                                                   String fpgj,
                                                   BigDecimal rolloverValue,
                                                   BigDecimal maxStepUsage
                                                  ) {
        String currentTimeStr = InfluxdbUtil.convertTimeToFormatStr(currentTime, dateEnum);
        String preCollectTimeStr = InfluxdbUtil.getFormatTimeByInstant(preCollectTime, dateEnum);
        //赋值上次读数
        lastReadeValue = lastReadeValue.add(lastData.getReadeValue());
        //本次读数
        readeValue = readeValue.add(currentVal);
        //累计值
        totalValue = totalValue.add(lastData.getTotalValue() == null ? new BigDecimal("0.00") : lastData.getTotalValue());//累计量
        if (currentTimeStr.equals(preCollectTimeStr)) {
            //还在同一时刻，初始化当前的用量
            collectionNum = collectionNum.add(lastData.getValue());
            fcollectionNum = fcollectionNum.add(lastData.getFvalue() != null ? lastData.getFvalue() : BigDecimal.ZERO);
            pcollectionNum = pcollectionNum.add(lastData.getPvalue() != null ? lastData.getPvalue() : BigDecimal.ZERO);
            gcollectionNum = gcollectionNum.add(lastData.getGvalue() != null ? lastData.getGvalue() : BigDecimal.ZERO);
            jcollectionNum = jcollectionNum.add(lastData.getJvalue() != null ? lastData.getJvalue() : BigDecimal.ZERO);
        }
        //计算本次采集的用量
        usageResult = MeterUsageCalculator.calculate(lastReadeValue, readeValue, rolloverValue, maxStepUsage);
        BigDecimal usedVal = usageResult.getUsage();
        collectionNum = collectionNum.add(usedVal);
        //根据峰平谷配置 将数据累计到相应的变量上
        if (FPGTypeEnum.JV.name().equals(fpgj)) {
            jcollectionNum = jcollectionNum.add(usedVal);
        } else if (FPGTypeEnum.FV.name().equals(fpgj)) {
            fcollectionNum = fcollectionNum.add(usedVal);
        } else if (FPGTypeEnum.PV.name().equals(fpgj)) {
            pcollectionNum = pcollectionNum.add(usedVal);
        } else if (FPGTypeEnum.GV.name().equals(fpgj)) {
            gcollectionNum = gcollectionNum.add(usedVal);
        }
        //处理累计量
        //小时表累计的一天的用量
        //天表累计的月用量 一次类推
        LocalDateTime preDateTime = DateUtils.parse(preCollectTime.toEpochMilli());
        String startTime = null, endTime = null;
        if (dateEnum.toString().equals("Hour")) {
            startTime = DateUtils.format(currentTime, "yyyy-MM-dd");
            endTime = DateUtils.formatDate(preDateTime, DEFAULT_DATE_FORMAT);
        } else if (dateEnum.toString().equals("Day")) {
            DateUtils.formatDate(currentTime, DEFAULT_MONTH_FORMAT);
            DateUtils.formatDate(preDateTime, DEFAULT_MONTH_FORMAT);
        } else if (dateEnum.toString().equals("Month")) {
            DateUtils.formatDate(currentTime, "yyyy");
            DateUtils.formatDate(preDateTime, "yyyy");
        } else if (dateEnum.toString().equals("Year")) {
            DateUtils.formatDate(currentTime, "yyyy");
            DateUtils.formatDate(preDateTime, "yyyy");
        }
        if (startTime != null && endTime != null && !startTime.equals(endTime)) {
            totalValue = totalValue.multiply(BigDecimal.ZERO);//置为0
        }
        return usageResult;
    }

    //检查数据是否异常
    public Boolean checkDataExp() {
        return usageResult != null && usageResult.isAbnormal();
    }
}
