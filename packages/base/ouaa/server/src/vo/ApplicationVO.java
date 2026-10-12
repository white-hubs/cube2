package com.guhecloud.cube.biz.ouaa.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationExtendInfo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 应用信息
 *
 * @author Woody
 * @date 2021/7/14
 */
@Data
public class ApplicationVO {
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
     * 是否公开访问
     */
    private Boolean publicAccess;
    /**
     * 应用组id
     */
    private Long applicationGroupId;

    /**
     * 应用组名称
     */
    private String applicationGroupName;

    /**
     * 依赖应用
     */
    private List<BaseInfo> dependAppList;

    /**
     * 应用端
     */
    private List<BaseInfo> terminalList;

    /**
     * 应用扩展信息
     */
    private ApplicationExtendInfo applicationExtendInfo;

    /**
     * 最新版本
     */
    private String version;

    /**
     * 浏览次数
     */
    private Integer viewCount;
    /**
     * 菜单列表
     */
    private List<MenuVO> menuVOS;

    /**
     * 图标信息
     */
    private FileVO fileVO;

    /**
     * 应用发布时间
     */
    private LocalDateTime createTime;

    /**
     * 应用更新时间
     */
    private LocalDateTime updateTime;
}
