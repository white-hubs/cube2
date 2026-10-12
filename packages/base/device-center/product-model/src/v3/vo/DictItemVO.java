package com.guhecloud.cube.base.iot.product.model.v3.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 字典项视图对象
 * 返回给前端的字典项数据
 */
@Data
public class DictItemVO {

    /**
     * 字典项ID
     */
    private Long id;

    /**
     * 字典类型ID
     */
    private Long dictId;

    /**
     * 字典项编码(v3新增,用于跨环境关联)
     */
    private String itemCode;

    /**
     * 字典上级ID
     */
    private Long itemParentId;

    /**
     * 字典排序
     */
    private Integer itemSort;

    /**
     * 层级
     */
    private String itemLevel;

    /**
     * 字典标签(显示名称)
     */
    private String itemLabel;

    /**
     * 字典键值
     */
    private String itemValue;

    /**
     * 状态: 0正常 1停用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 子级字典项列表(用于树形结构)
     */
    private List<DictItemVO> children;

    /**
     * 关联文件列表(仅品牌字典使用)
     */
    private List<FileVO> fileVOs;
}
