package com.guhecloud.cube.biz.ouaa.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 卡片数据模型
 *
 * @author lanchun
 * @date 2022/5/23 15:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardVO {

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
     * 卡片编号
     */
    private String code;

    /**
     * 卡片名称
     */
    private String name;

    /**
     * 排序数字
     */
    private Integer sort;

    /**
     * 卡片备注
     */
    private String remark;

    /**
     * 卡片大小
     */
    private String cardSize;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否默认卡片
     */
    private Boolean defaultCard;

    /**
     * 附件
     */
    private FileVO fileVO;
}
