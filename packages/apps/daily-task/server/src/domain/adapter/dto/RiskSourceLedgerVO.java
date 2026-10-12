package com.guhecloud.cube.biz.daily.task.domain.adapter.dto;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-30  16:58
 * @Description:
 * @Version: 1.0
 */
@Data
public class RiskSourceLedgerVO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 风险源编码
     */
    private String serialNumber;
    /**
     * 风险源名称
     */
    private String name;
    /**
     * 风险源类别code
     */
    private String typeCode;
    /**
     * 风险源类别名称
     */
    private String typeName;
    /**
     * 区域id
     */
    private String areaId;
    private String areaName;

    /**
     * 档位id
     */
    private Long stallId;
    private String stallName;
    /**
     * 现场点
     */
    private Long sitePointId;
    private String sitePointName;
    /**
     * 属性
     */
    private String attribute;
    /**
     * 数量
     */
    private Integer number;
    /**
     * 维护部门
     */
    private Long maintenanceDepId;
    /**
     * 维护部门
     */
    private String maintenanceDepName;
    /**
     * 状态
     */
    private String status;
    /**
     * 附件
     */
    private List<FileVO> files;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 使用部门id
     */
    private Long receiveDepId;
    /**
     * 使用部门名称
     */
    private String receiveDepName;
    /**
     * 使用人
     */
    private String receiveUserName;
    /**
     * 排序
     */
    private Integer level;
    /**
     * 使用人电话
     */
    private String receiveUserPhone;
    /**
     * 使用到期日期
     */
    private LocalDateTime usageDate;
    /**
     * 最近修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 模型编码
     */
    private String bimCode;
    /**
     * 配件信息
     */
    private String sceneType;
    /**
     * 生产厂家
     */
    private String produceFactory;
    /**
     * 生产厂家地址
     */
    private String produceAddress;
    /**
     * 配送公司
     */
    private String deliveryCompany;
    /**
     * 配送公司地址
     */
    private String deliveryAddress;
    /**
     * 维护人
     */
    private Long maintenanceUserId;
    /**
     * 维护人姓名
     */
    private String maintenanceUserName;
    /**
     * 规格型号
     */
    private String norms;
    /**
     * 强检日期
     */
    private LocalDateTime mandatoryTestingDate;
    /**
     * 经纬度
     */
    private String latAndLon;
    /**
     * 经纬度地点
     */
    private String location;
    private String address;
    /**
     * 标签ID
     */
    private List<Long> tagIds;
    /**
     * 标签名称
     */
    private String tagNames;

    private List<Long> departmentIds;
    private String departmentNames;
    private String typeId;

}
