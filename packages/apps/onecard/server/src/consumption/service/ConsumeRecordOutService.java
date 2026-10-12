package com.guhecloud.cube.onecard.server.consumption.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.onecard.server.consumption.entity.ConsumeRecordOut;
import com.guhecloud.cube.onecard.server.consumption.param.ConsumeRecordOutParam;
import com.guhecloud.cube.onecard.server.consumption.param.OneCardJobParam;
import com.guhecloud.cube.onecard.server.consumption.vo.ConsumeRecordVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 14:34
 * @Description:
 */
public interface ConsumeRecordOutService extends IService<ConsumeRecordOut> {

    /**
     * 同步消费记录
     */
    void syncConsumptionRecord(OneCardJobParam oneCardJobParam);

    /**
     * 查询消费记录
     *
     * @param param
     *
     * @return
     */
    Pagination<ConsumeRecordVO> getConsumeRecordOut(ConsumeRecordOutParam param);
}
