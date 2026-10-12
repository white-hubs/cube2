package com.guhecloud.platform.dcm.support;

import com.guhecloud.platform.dcm.Identifiable;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.Collection;

public interface IDcmRciConverter<DE extends Identifiable> {

    /**
     * 将一个Rci 转换为一组Dcm对象，为后续的差异比较应用做准备
     */
    Collection<? extends DE> convertRci2Dcm(RequestedConfigItem rci);

    /**
     * 将一组Dcm 转换为一到多个Rci, 用于标准Dcm Api 界面的返回
     */
    Collection<RequestedConfigItem> convertDcm2Rci(Collection<? extends DE> dcmEntities);
}
