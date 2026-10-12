package com.guhecloud.cube.deliver.project.merchant.dto.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallSafetyMarkService;
import com.guhecloud.cube.deliver.project.merchant.dto.SpaceDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 档位总分值（安全生产值+评分）
 * </p>
 *
 * @author bq
 * @since 2022-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StallScoreDTO {

    /**
     * 档位ID
     */
    private String id;

    /**
     * 档位编号
     */
    private String code;

    /**
     * 档位名称
     */
    private String name;

    /**
     * 一级区域ID
     */
    private String areaId1;

    /**
     * 二级区域ID
     */
    private String areaId2;

    /**
     * 三级区域ID
     */
    private String areaId3;

    /**
     * 区域名称
     */
    private List<String> areaNames;

    /**
     * 所属商户ID
     */
    private String companyId;

    /**
     * 所属商户名称
     */
    private String companyName;

    /**
     * 经营信息
     */
    private String introduction;

    /**
     * 所属人
     */
    private String owner;

    /**
     * 所属人联系方式
     */
    private String mobile;

    /**
     * 安全生产总分
     */
    private Integer defaultScore  = StallSafetyMarkService.STORE_DEFAULT_MARK;

    /**
     * 安全生产总得分
     */
    private Integer score = StallSafetyMarkService.STORE_DEFAULT_MARK;

    /**
     * 安全生产扣减分值
     */
    private int deductScore;

    /**
     * 商品质量平均分
     */
    private Float zScoreAvg = 5f;

    /**
     * 诚实守信平均分
     */
    private Float sScoreAvg = 5f;

    /**
     * 服务态度平均分
     */
    private Float tScoreAvg = 5f;

    /**
     * 环境卫生平均分
     */
    private Float wScoreAvg = 5f;

    /**
     * 总评分
     */
    private int totalScore;

    /**
     * 诚信经营值
     */
    private float honestyScore = 5f;

    /**
     * 总平均评分
     */
    private float totalScoreAvg = 5;

    /**
     * 巡检异常数
     */
    private int inspTotal = 0;

    /**
     * 评价次数
     */
    private int total = 0;

    /**
     * 租赁开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime beginTime;

    /**
     * 租赁到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endTime;

    /**
     * 档位信息
     */
    private SpaceDTO spaceDTO;

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
        float totalScoreAvg = this.totalScore / (float) this.total / 4;
        float floatValue = new BigDecimal(totalScoreAvg).setScale(1, RoundingMode.HALF_UP).floatValue();
        this.totalScoreAvg = floatValue;
    }

    public void setScore(Integer score) {
        this.score = score;
        if (score != null) {
            this.deductScore = this.defaultScore - score;
        }
    }

}
