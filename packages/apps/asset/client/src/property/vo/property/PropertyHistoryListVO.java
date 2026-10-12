package com.guhecloud.cube.property.management.client.property.vo.property;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PropertyHistoryListVO {
    private Long propertyId;
    /**
     * 处理时间
     */
    private LocalDateTime handleTime;
    /**
     * 处理方式名称
     */
    private String handleTypeName;
    /**
     * 处理人
     */
    private String createByName;
    private String createBy;
    private String creatorId;
    private ActorTypeEnum creatorType;
    /**
     * 处理内容
     */
    private String remarks;
    /**
     * 资产名称
     */
    private String name;
    /**
     * 照片
     */
    private String imgs;
    private List<FileVO> img;
    /**
     * 资产编码
     */
    private String serialNumber;
    /**
     * 资产类型
     */
    private String typeName;
    /**
     * 状态
     */
    private String status;
    /**
     * 金额
     */
    private String price;
    /**
     * 使用人
     */
    private String receiveUserName;
    /**
     * 使用部门
     */
    private String receiveDepName;
    /**
     * 购入日期
     */
    private String purchaseDate;

}
