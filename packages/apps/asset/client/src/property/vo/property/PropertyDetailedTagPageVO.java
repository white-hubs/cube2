package com.guhecloud.cube.property.management.client.property.vo.property;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDetailedTagPageVO {
    private Long id;
    /**
     * 资产编号
     */
    private String serialNumber;
    /**
     * 资产名称
     */
    private String name;
    /**
     * 资产类别
     */
    private String typeId;
    private String typeName;
    /**
     * 规格型号
     */
    private String norms;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 原值
     */
    private BigDecimal price;
    private BigDecimal priceTotal;

    /**
     * 来源
     */
    private String sourceName;
    private Long sourceId;
    /**
     * 购入日期
     */
    private LocalDateTime purchaseDate;
    /**
     * 入库日期
     */
    private LocalDateTime startTime;
    /**
     * 维保到期时间
     */
    private LocalDateTime maintenanceDate;
    /**
     * 剩余维保时间
     */
    private Long maintenanceTime;

    /**
     * 资产逾龄时间
     */
    private LocalDateTime usageDate;
    /**
     * 剩余使用期限
     */
    private Long usageTime;
    /**
     * 资产状态
     */
    private String status;
    private String statusName;
    /**
     * 管理员
     */
    private Long adminId;
    private String adminName;
    private String areaId;
    /**
     * 区域
     */
    private List<AreaDTO> area;
    /**
     * 使用部门
     */
    private Long receiveDepId;
    private String receiveDepName;
    /**
     * 使用人
     */
    private Long receiveUserId;
    private String receiveUserName;
    /**
     * 创建人
     */
    private String createBy;
    private String createByName;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 资产图片
     */
    private List<FileVO> imgs;
    /**
     * 资产附件
     */
    private List<FileVO> file;
    private String imgsStr;
    private String fileStr;
    private Integer number;
    /**
     * 存放地点
     */
    private String storageLocation;
    private String areaName;
    /**
     * 财务编码
     */
    private String financeNumber;
    /**
     * 维护部门
     */
    private Long maintenanceDepId;
    private String maintenanceDepName;
    private String deviceNumber;
    /**
     * 经纬度
     */
    private String latAndLon;
    /**
     * 经纬度地点
     */
    private String location;
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

}
