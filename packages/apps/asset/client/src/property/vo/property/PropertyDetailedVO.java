package com.guhecloud.cube.property.management.client.property.vo.property;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PropertyDetailedVO {
    private Long id;
    /**
     * 资产名称
     */
    private String name;
    /**
     * 资产编码
     */
    private String serialNumber;
    /**
     * 资产类别id
     */
    private Long typeId;
    /**
     * 资产类别
     */
    private String typeName;
    private List<Long> typeTree;
    /**
     * 购入日期
     */
    private LocalDateTime purchaseDate;
    /**
     * 维保期限
     */
    private Integer maintenanceTime;
    /**
     * 使用期限
     */
    private Long usageTime;
    /**
     * 规格型号
     */
    private String norms;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 数量
     */
    private Integer number;
    /**
     * 原值
     */
    private BigDecimal price;
    /**
     * 管理员
     */
    private String adminName;
    private Long adminId;
    /**
     * 区域
     */
    private List<AreaDTO> area;
    /**
     * 来源
     */
    private String sourceId;
    private String sourceName;
    /**
     * 存放地点
     */
    private String storageLocation;
    /**
     * 入库日期
     */
    private LocalDateTime startTime;
    /**
     * 资产图片
     */
    private List<FileVO> imgs;
    private String imgsStr;
    /**
     * 资产附件
     */
    private List<FileVO> file;
    private String fileStr;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 供应商
     */
    private String supplier;
    /**
     * 联系人
     */
    private String supplierUser;
    /**
     * 联系方式
     */
    private String supplierPhone;
    /**
     * 维保说明
     */
    private String maintenanceRemarks;

    /**
     * 入库单号
     */
    private String depositoryNumbers;
    /**
     * 审批状态
     */
    private String status;
    /**
     * 设备编码
     */
    private String deviceNumber;
    /**
     * 接收部门名称
     */
    private String receiveDepName;
    private String receiveDepId;
    /**
     * 接收人
     */
    private Long receiveUserId;
    /**
     * 接收人
     */
    private String receiveUserName;
    /**
     * 盘点状态
     */
    private String checkStatus;
    /**
     * 盘点操作人
     */
    private Long checkUserId;
    private String checkUserName;
    private String createBy;
    private String creatorId;
    private ActorTypeEnum creatorType;
    private String createByName;
    private LocalDateTime checkTime;
    /**
     * 是否允许手动选择
     */
    private Boolean chooseType;
    /**
     * 使用人电话
     */
    private String receiveUserPhone;
    /**
     * 异常类型
     */
    private Long abnormalType;
    private String abnormalTypeName;

    /**
     * 异常说明
     */
    private String abnormalRemarks;
    /**
     * 异常图片
     */
    private String abnormalFiles;
    /**
     * 是否逾龄
     */
    private Boolean usage = false;
    /**
     * 资产逾龄时间
     */
    private LocalDateTime usageDate;
    /**
     * 财务编码
     */
    private String financeNumber;
    /**
     * 维护部门
     */
    private Long maintenanceDepId;
    private String maintenanceDepName;
    /**
     * 经纬度
     */
    private String latAndLon;
    /**
     * 经纬度地点
     */
    private String location;
}
