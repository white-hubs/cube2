package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

@Data
public class DetailedVO {
    private Long id;
    /**
     * 附件
     */
    private String fileIds;
    private List<FileVO> file;
    /**
     * 名称
     */
    private String name;
    /**
     * 编码
     */
    private String code;
    /**
     * 类型
     */
    private Long categoryId;
    private String categoryName;
    /**
     * 仓库
     */

    /**
     * 品牌
     */
    private String brand;
    /**
     * 单位
     */
    private String unit;
    /**
     * 处理时间
     */
    /**
     * 处理人
     */
    /**
     * 初始数量
     */
    /**
     * 初始单价
     */
    /**
     * 初始金额
     */
    /**
     * 入库数量
     */
    /**
     * 入库单价
     */
    /**
     * 入库金额
     */
    /**
     * 出库数量
     */
    /**
     * 出库单价
     */
    /**
     * 出库金额
     */
    /**
     * 结余数量
     */
    /**
     * 结余单价
     */
    /**
     * 结余金额
     */
}
