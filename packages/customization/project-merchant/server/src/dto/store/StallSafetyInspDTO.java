package com.guhecloud.cube.deliver.project.merchant.dto.store;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallSafetyInsp;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 档位安全生产巡检扣分记录
 * </p>
 *
 * @author zwb
 * @since 2021-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StallSafetyInspDTO extends ConvertionDTO<StallSafetyInspDTO, StallSafetyInsp> {

    public StallSafetyInspDTO() {
    }

    public StallSafetyInspDTO(StallSafetyInsp insp) {
        super();
        map(insp);
    }

    public StallSafetyInspDTO(List<StallSafetyInsp> insps) {
        super();
        map(insps);
    }

    /**
     * 主键
     */
    private String id;

    /**
     * 档位ID
     */
    private String stallId;

    /**
     * 任务名
     */
    private String taskName;

    /**
     * 记录ID
     */
    private String recordId;

    /**
     * 记录编号
     */
    private String recordCode;

    /**
     * 巡检记录是否存在
     */
    private Integer recordExist;

    /**
     * 标准名
     */
    private String standardName;

    /**
     * 项ID
     */
    private String itemId;

    /**
     * 项名称
     */
    private String itemName;

    /**
     * 项结果名
     */
    private String itemResultName;

    /**
     * 项结果扣分
     */
    private Integer itemResultMark;

    /**
     * 巡检时间
     */
    private LocalDateTime time;

    /**
     * 巡检人ID
     */
    private String userId;

    /**
     * 巡检人名
     */
    private String username;

    /**
     * 巡检人昵称
     */
    private String nickname;

    private LocalDateTime createTime;

}
