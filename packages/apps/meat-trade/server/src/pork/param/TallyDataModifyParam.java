package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.cube.biz.meattrade.server.pork.entity.Tally;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 理货机新增理货片数
 *
 * @author lzy
 * @date 2021/12/8
 */
@Data
public class TallyDataModifyParam {

    /**
     * 每次请求唯一id
     */
    private String guid;

    /**
     * 理货机编号
     */
    private String deviceId;

    /**
     * 理货机台号
     */
    private String terminalNumber;

    /**
     * 理货批次号
     */
    @NotNull(message = "理货批次号不能为空")
    private String tallyId;

    /**
     * 理货状态（理货开始：0，理货上报：1，理货暂停：2，理货结束：3）
     */
    private Integer eventType;

    /**
     * 理货片数集合
     */
    //    @NotEmpty(message = "理货片数不能为空")
    private List<Tally> datas;

    /**
     * 当前理货开始时间（暂停后再恢复取恢复时间）
     */
    private LocalDateTime startTime;

    /**
     * 当前理货结束时间（暂停事件为暂停时间），上报消息可不提供
     */
    private LocalDateTime endTime;

}
