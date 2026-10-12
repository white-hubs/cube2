package com.guhecloud.cube.biz.mdata.client.rocketmq.message;

import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzy
 * @date 2022/1/15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddMallMessage {

    public final static String TOPIC = "add-mall";

    /**
     * 批发商信息
     */
    private SupplierDTO supplier;
}
