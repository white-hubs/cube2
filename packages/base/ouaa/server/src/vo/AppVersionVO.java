package com.guhecloud.cube.biz.ouaa.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.AppTypeEnum;
import lombok.Data;

@Data
public class AppVersionVO {

    private Long id;

    /**
     * 版本名称
     */
    private String name;

    /**
     * 终端编号
     */
    private String terminal;

    /**
     * 类型
     */
    private AppTypeEnum type;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 文件密钥
     */
    private String apkSecret;

    /**
     * 版本序号
     */
    private Integer sort;

    /**
     * 是否强制更新
     */
    private Boolean forceUpdated;

    /**
     * 版本备注
     */
    private String remark;

    /**
     * 附件信息
     */
    private FileVO fileVO;
}
