package com.guhecloud.cube.biz.mdata.client;

import com.guhecloud.cube.biz.mdata.client.param.BizLogRecordParam;
import com.guhecloud.cube.biz.mdata.client.vo.BizLogRecordDTO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author appet
 * @date 2023/08/01
 */
@FeignClient(name = "mdata")
public interface BizLogClient {
    //查询业务日志
    @GetMapping("/biz-log-record/page")
    Pagination<BizLogRecordDTO> queryBizLogRecord(@SpringQueryMap BizLogRecordParam param);

}
