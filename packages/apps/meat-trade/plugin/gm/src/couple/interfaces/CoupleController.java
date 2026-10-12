package com.guhecloud.cube.gm.meattrade.plugin.couple.interfaces;

import com.guhecloud.cube.gm.meattrade.plugin.couple.application.querygateway.CoupleQueryService;
import com.guhecloud.cube.gm.meattrade.plugin.couple.application.querygateway.query.CoupleStatusQuery;
import com.guhecloud.cube.gm.meattrade.plugin.couple.interfaces.vo.CoupleValidTradeStatusVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CoupleController {

    private final CoupleQueryService coupleQueryService;

    @PostMapping("/hook-trade-status")
    public CoupleValidTradeStatusVO coupleValidTradeStatus(@Valid @RequestBody CoupleStatusQuery coupleStatusQuery) {
        return this.coupleQueryService.coupleValidTradeStatus(coupleStatusQuery);
    }
}
