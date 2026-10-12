package com.guhecloud.cube.biz.daily.task.commons.param;

import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordItemPO;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordPO;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordTargetItemPO;
import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordTargetPO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * 日常任务记录提交后置参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecordSubmitAfterParam {
    /**
     * 日常任务记录
     */
    private DailyRecordPO dailyRecordPO;
    /**
     * 日常任务记录项
     */
    private ArrayList<DailyRecordItemPO> recordItemList;
    /**
     * 日常任务记录对象
     */
    private ArrayList<DailyRecordTargetPO> recordTargetList;
    /**
     * 日常任务记录对象项
     */
    private ArrayList<DailyRecordTargetItemPO> recordTargetItemList;
    /**
     * 文件引用创建参数
     */
    private ArrayList<FileRefCreateParam> fileRefCreateParams;
}
