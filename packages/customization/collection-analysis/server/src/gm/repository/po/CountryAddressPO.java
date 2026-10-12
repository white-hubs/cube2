package com.guhecloud.cube.biz.collection.analysis.gm.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 国家区域(三级)
 */
@Data
@TableName("country_address")
@EqualsAndHashCode(callSuper = true)
public class CountryAddressPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long pid;
    /**
     * 层级
     */
    private Integer deep;
    /**
     * 名称
     */
    private String name;
    /**
     * 拼音首字母
     */
    private String pinyinPrefix;
    /**
     * 拼音
     */
    private String pinyin;
    /**
     * 行政编码
     */
    private Long extId;
    /**
     * 新政名称
     */
    private String extName;
}
