package com.guhecloud.cube.biz.ouaa.client.dto;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/3
 */
@Data
public class TenantDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 租住编号
     */
    private Integer code;

    /**
     * 租户名称
     */
    private String name;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 所在地
     */
    private String location;

    /**
     * 楼栋数
     */
    private Integer building;

    /**
     * 占地面积
     */
    private BigDecimal areaCovered;

    /**
     * 企业logo
     */
    private Long logoId;

    /**
     * 宣传图
     */
    private String file;

    /**
     * 租户备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否是管理员
     */
    private Boolean admin;

    /**
     * 是否最近登录
     */
    private Boolean recentLogin;

    /**
     * 是否为主企业
     */
    private Boolean mainEnterprise;

    /**
     * 占地总建筑面积
     */
    private BigDecimal floorArea;

    List<FileVO> files;
    private LocalDateTime createTime;
}
