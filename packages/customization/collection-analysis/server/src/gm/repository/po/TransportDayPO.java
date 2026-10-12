package com.guhecloud.cube.biz.collection.analysis.gm.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-17  11:44
 * @Description:
 * @Version: 1.0
 */
@Data
@TableName("transport_day")
public class TransportDayPO {
    private Long id;
    /**
     * 转运托数
     */
    private Integer transportCount;
    /**
     * 转运件数
     */
    private Integer pieceCount;
    /**
     * 车次数
     */
    private Integer carCount;
    /**
     * 转运日期
     */
    private LocalDateTime transportDate;
}
