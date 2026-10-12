package com.guhecloud.cube.property.management.client.property.param.property;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Data;

@Data
public class PropertyHistoryPageParam {
    /**
     * 资产id
     */
    private Long id;
    /**
     * 模型编码
     */
    private String bimCode;
    /**
     * 处理日期
     */
    private String handleTime;
    /**
     * 处理人部门
     */
    private String departmentId;
    /**
     * 处理方式
     */
    private String handleType;
    /**
     * 资产状态
     */
    private String status;
    /**
     * 资产类别
     */
    private Long typeId;
    /**
     * 资产编码、资产名称
     */
    private String keywords;
    private Integer pageNumber;
    private Integer pageSize;

    public void getPage() {
        if (ObjectUtils.isEmpty(pageNumber) || pageNumber < 1) {
            throw new RuntimeException("页码不能为空");
        }
        if (ObjectUtils.isEmpty(pageSize) || pageSize < 1) {
            throw new RuntimeException("页面大小不能小于1");
        }
    }
}
