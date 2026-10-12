package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo;

import lombok.Data;

/**
 * @author lzy
 * @date 2022/4/26
 */
@Data
public class HiddenDangerTypeVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 隐患分类主键
     */
    private Long hiddenDangerCategoryId;

    /**
     * 隐患分类名称
     */
    private String hiddenDangerCategoryName;

}
