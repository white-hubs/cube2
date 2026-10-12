package com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2022/7/20
 * @description 二维码样式
 */
@Data
public class QrcodeStyleVO {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 内容配置json
     */
    private String configJson;

    /**
     * 宽度
     */
    private String width;

    /**
     * 高度
     */
    private String height;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
