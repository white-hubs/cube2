package com.guhecloud.cube.base.device.center.core.dto;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

@Data
public class DeviceBizTypeDTO {
    private Long id;

    /**
     * 标签编号(租户下唯一),eg: A004
     */
    private String typeCode;

    /**
     * 标签名称
     */
    private String typeName;

    /**
     * 扩展信息配置
     */
    private String expand;

    /**
     * 是否系统保留(0 否 1是)
     */
    private Integer sysOwn;

    /**
     * 图标
     */
    private String icon;

    /**
     * 类型图片
     */
    private Long picture;
    /**
     * 类型图片
     */
    private List<FileVO> pictureList;
}
