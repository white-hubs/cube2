package com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("cnp_area")
public class CnpArea {

    private Long id;

    /**
     * 区域编码
     */
    private String code;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 区域父级ID
     */
    private Long pid;

}
