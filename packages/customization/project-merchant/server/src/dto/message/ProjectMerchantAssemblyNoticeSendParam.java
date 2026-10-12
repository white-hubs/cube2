package com.guhecloud.cube.deliver.project.merchant.dto.message;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 查询通知条件
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMerchantAssemblyNoticeSendParam {
    /**
     * 条件id
     */
    private Long conditionId;
    /**
     * 消息模板入参
     */
    @NotEmpty
    private List<KeyValueVO> tempParamList;
    /**
     * 消息接收人ids
     */
    private List<Long> userId;
}
