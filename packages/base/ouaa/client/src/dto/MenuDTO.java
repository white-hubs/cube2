package com.guhecloud.cube.biz.ouaa.client.dto;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.model.Tree;
import lombok.Data;

/**
 * 菜单数据模型
 *
 * @author Woody
 * @date 2021/6/30
 */
@Data
public class MenuDTO extends Tree.Simple<Long, MenuDTO> {

    /**
     * 菜单id
     */
    private Long id;

    /**
     * 访问端主键
     */
    private Long terminalId;

    /**
     * 应用主键
     */
    private Long applicationId;

    /**
     * 分类字典主键
     */
    private Long categoryId;

    /**
     * 分类字典名称
     */
    private String categoryName;

    /**
     * 菜单编号
     */
    private String code;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 排序数字
     */
    private Float sort;

    /**
     * 菜单备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 图标附件
     */
    private FileVO fileVO;
}
