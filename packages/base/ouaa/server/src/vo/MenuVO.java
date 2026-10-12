package com.guhecloud.cube.biz.ouaa.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.model.Tree;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 菜单数据模型
 *
 * @author Woody
 * @date 2021/6/30
 */
@Data
public class MenuVO extends Tree.Simple<Long, MenuVO> {

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
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

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

    /**
     * 菜单功能
     */
    private List<MenuFunctionVO> functions;

    /**
     * 数据权限
     */
    private List<DataPermissionVO> dataPermissions;

    /**
     * 别名配置
     */
    private Map<String, String> alias;
}
