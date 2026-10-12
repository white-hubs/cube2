package com.guhecloud.cube.property.management.client.property.param.property;

import com.guhecloud.cube.property.management.client.common.vo.KeyValueLongStringVO;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

@Data
public class OveragePageParam extends Paging {
    /**
     * 资产编码、名称
     */
    private String keyword;
    /**
     * 资产状态
     */
    private String status;
    /**
     * 资产类别
     */
    private Long typeId;
    /**
     * 维保到期时间
     */
    private String maintenanceData;
    /**
     * 维保到期(maintenance);
     * 逾龄资产(usage)
     */
    private String queryType;
    /**
     * key表示id,value表示编号
     * 有id时需要排除自己id的编号再校验是否有重复
     */
    private List<KeyValueLongStringVO> checkSerialNumberList;
}
