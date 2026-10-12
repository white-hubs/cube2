package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonRollCallPageParam extends Paging {

    /**
     * 点名任务记录编号
     */
    private String recordCode;

    /**
     * 姓名
     */
    private String personName;

    /**
     * 人员编号
     */
    private String indexCode;

    /**
     * 状态：0-点到，1-特殊情况
     */
    private Integer status;

    /**
     * 点名设备名称
     */
    private String deviceName;

    /**
     * 场所编号（保留字段，当前版本无数据）
     */
    private String areaName;
}
