package com.guhecloud.cube.biz.ouaa.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 应用分页对象
 *
 * @author lanchun
 * @date 2021/10/13 14:09
 */
@Data
public class ApplicationPageVO {

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
     * 是否展示
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
     * 最新版本
     */
    private String version;
    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 路径
     */
    private String path;

    /**
     * 应用图标信息
     */
    private FileVO fileVO;

    /**
     * 菜单列表
     */
    private List<MenuVO> menuVOS;

    /**
     * 访问时间
     */
    private LocalDateTime lastVisitTime;

    /**
     * 别名配置
     */
    private String alias;
}
