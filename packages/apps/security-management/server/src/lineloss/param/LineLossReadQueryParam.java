package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * 线损采集记录查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LineLossReadQueryParam extends Paging {
    /**
     * 开始时间
     */
    private String startTime;
    
    /**
     * 结束时间
     */
    private String endTime;
    
    /**
     * 设备 ID 集合
     */
    private Set<Long> deviceIdSet;
    
    /**
     * 属性码
     */
    private String attributeCode;
}
