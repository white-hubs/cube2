package com.guhecloud.cube.deliver.project.merchant.entity.store;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 档位评价总表
 * </p>
 *
 * @author benhuabao
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stall_evaluation")
public class StallEvaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 档位ID
     */
    private String stallId;

    /**
     * 评价次数
     */
    private int total;

    /**
     * 商品质量总评分
     */
    private int zScore;

    /**
     * 商品质量平均分
     */
    private float zScoreAvg;

    /**
     * 诚实守信总评分
     */
    private int sScore;

    /**
     * 诚实守信平均分
     */
    private float sScoreAvg;

    /**
     * 服务态度总评分
     */
    private int tScore;

    /**
     * 服务态度平均分
     */
    private float tScoreAvg;

    /**
     * 环境卫生总评分
     */
    private int wScore;

    /**
     * 环境卫生平均分
     */
    private float wScoreAvg;

    /**
     * 总评分
     */
    private int totalScore;

    /**
     * 诚信经营值
     */
    private int honestyScore;

}
