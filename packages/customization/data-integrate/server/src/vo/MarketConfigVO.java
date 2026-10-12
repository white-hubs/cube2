package com.guhecloud.cube.base.dataintegrate.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

/**
 * @author Appet
 * @Description 采集市场配置
 * @date 2023-09-09 15:08:36
 */
@Data
public class MarketConfigVO {

    //
    private Long id;
    //市场名称
    private String marketName;
    //市场编号
    private String marketCode;
    //执行器编号
    private String executorCode;
    //执行器端口
    private Integer executorPort;
    //市场方租户号
    private Integer marketTenant;
    //请求地址
    private String url;
    //登录账号
    private String account;
    //登录密码
    private String password;

    private String coordinate;

    //地址
    private String address;
    //创建时间
    private String foundTime;

    private List<FileVO> files;
    /**
     * 市场面积
     */
    private Double marketArea;
    /**
     * 是否开启
     */
    private Boolean status;
}