package com.guhecloud.cube.biz.ouaa.client.dto;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 应用分页对象
 *
 * @author lanchun
 * @date 2021/10/13 14:09
 */
@Data
public class ApplicationPageDTO {

    /**
     * 应用主键
     */
    private Long id;

    /**
     * 应用编号
     */
    private String code;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用图标
     */
    private String icon;

    /**
     * 应用备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 应用组主键
     */
    private Long applicationGroupId;

    /**
     * 应用组名称
     */
    private String applicationGroupName;

    /**
     * 应用创建时间
     */
    private LocalDateTime createTime;

    /**
     * 应用修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 路径
     */
    private String path;

    /**
     * 应用图标信息
     */
    private FileVO fileVO;
}
