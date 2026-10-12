package com.guhecloud.cube.biz.ouaa.client.dto;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

/**
 * 应用信息
 *
 * @author Woody
 * @date 2021/7/14
 */
@Data
public class ApplicationDTO {
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
     * 应用组id
     */
    private Long applicationGroupId;

    /**
     * 应用组名称
     */
    private String applicationGroupName;

    /**
     * 菜单列表
     */
    private List<MenuDTO> menuVOS;

    /**
     * 图标信息
     */
    private FileVO fileVO;
}
