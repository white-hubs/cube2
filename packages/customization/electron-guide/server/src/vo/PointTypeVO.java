package com.guhecloud.cube.srv.electron.guide.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 点位分类
 *
 * @author lanchun
 * @date 2024/9/3 11:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointTypeVO {

    private Long id;

    /**
     * 分类编号
     */
    private String code;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 点位附件
     */
    private String filePath;
}
