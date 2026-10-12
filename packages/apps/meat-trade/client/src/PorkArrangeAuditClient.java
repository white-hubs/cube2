package com.guhecloud.cube.biz.meattrade.client;

import com.guhecloud.cube.biz.meattrade.client.dto.PorkArrangeAuditVO;
import com.guhecloud.cube.biz.meattrade.client.param.PorkArrangeAuditQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.validation.Valid;

@FeignClient("meattrade")
public interface PorkArrangeAuditClient {

    @GetMapping("/pork-arrange-audit/page")
    Pagination<PorkArrangeAuditVO> pagePorkArrangeAudit(@Valid @SpringQueryMap PorkArrangeAuditQueryParam queryParam);
}
