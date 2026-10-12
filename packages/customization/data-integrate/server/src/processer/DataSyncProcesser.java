package com.guhecloud.cube.base.dataintegrate.server.processer;

import com.guhecloud.cube.base.dataintegrate.server.model.ContextParam;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public abstract class DataSyncProcesser<T> {

    public abstract ContextParam getContextParam(String param);

    //获取需要同步的数据
    public abstract List<T> getSyncData(ContextParam param);

    //清洗数据
    public abstract List<T> cleanData(List<T> dataList, ContextParam param);

    //数据入库
    public abstract void storeData(List<T> dataList, ContextParam param);

    public void dataSync(String param) {
        log.info("风险源采集开始:{}", param);
        String[] params = param.split(",");
        ContextParam contextParam = this.getContextParam(params[0]);
        if (contextParam.getStatus() != null && contextParam.getStatus()) {
            if (params.length > 1) {
                contextParam.setLastTime(DateUtils.parse(params[1]));
            }
        }
        List<T> syncData = this.getSyncData(contextParam);
        log.info("风险源采集结束,本次共采集:{}条", syncData.size());
        List<T> cleanedData = this.cleanData(syncData, contextParam);
        this.storeData(cleanedData, contextParam);
    }

    protected String getPeriod(ContextParam param, LocalDateTime lastDateTime) {
        var startTime = DateUtils.format(DateUtils.beginOfDay(
                ObjectUtils.isNotEmpty(param.getLastTime()) ? param.getLastTime() : LocalDateTime.now()), "yyyy-MM-dd HH:mm:ss");
        if (lastDateTime != null) {
            startTime = DateUtils.format(lastDateTime, "yyyy-MM-dd HH:mm:ss");
        }
        var endTime = DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss");
        return startTime + "," + endTime;
    }
}
