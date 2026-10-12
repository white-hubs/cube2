package com.guhecloud.cube.biz.dispatch.server.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author Appet
 * @Description 调度过程中产生文件
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchFileQueryParam extends Paging {

    //查询时间段
    private Period period;
    //编号
    private String code;
    //文件类型 音频或视频  视频（video）语音（voice）
    private String fileType;
    //电话类型
    private String callType;

}