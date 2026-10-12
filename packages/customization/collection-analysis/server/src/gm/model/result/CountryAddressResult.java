package com.guhecloud.cube.biz.collection.analysis.gm.model.result;

import lombok.Data;

import java.util.List;

@Data
public class CountryAddressResult {
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
    /**
     * 子集
     */
    private List<CountryAddressResult> childList;
}
