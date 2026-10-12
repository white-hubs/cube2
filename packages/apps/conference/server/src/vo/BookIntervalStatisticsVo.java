package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookIntervalStatisticsVo {
    private Long conferenceId;
    private String name;
    private String periodTime;
    private BookRoomEnum bookRoomEnum;
    private int num;
    //使用率
    private BigDecimal usageRate;
    //未使用率
    private BigDecimal unusedRate;
}
