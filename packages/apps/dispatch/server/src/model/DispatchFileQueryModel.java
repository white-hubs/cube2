package com.guhecloud.cube.biz.dispatch.server.model;

import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Appet
 * @Description 调度文件查询
 * @date 2022-07-26 10:08:36
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchFileQueryModel extends Paging {

    //编号
    private String code;
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //文件类型 音频或视频
    private String fileType;
    //电话类型
    private String callType;

}