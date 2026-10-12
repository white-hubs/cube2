package com.guhecloud.cube.gm.meattrade.plugin.couple.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.gm.meattrade.plugin.couple.application.CoupleService;
import com.guhecloud.cube.gm.meattrade.plugin.couple.application.querygateway.query.CoupleStatusQuery;
import com.guhecloud.cube.gm.meattrade.plugin.couple.domain.entity.Couple;
import com.guhecloud.cube.gm.meattrade.plugin.couple.domain.enumeration.CoupleStatusEnum;
import com.guhecloud.cube.gm.meattrade.plugin.couple.domain.po.CouplePO;
import com.guhecloud.cube.gm.meattrade.plugin.couple.domain.valueobj.CoupleStatus;
import com.guhecloud.cube.gm.meattrade.plugin.couple.interfaces.vo.CoupleValidTradeStatusVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CoupleQueryService {

    private final CoupleService coupleService;

    public CoupleValidTradeStatusVO coupleValidTradeStatus(CoupleStatusQuery query) {
        List<CouplePO> couples = this.coupleService.list(Wrappers.<CouplePO>lambdaQuery()
                                                                 .in(CouplePO::getCode, query.getTagIds()));
        if (CollectionUtils.isEmpty(couples)) {
            return CoupleValidTradeStatusVO.builder()
                    .pass(true)
                    .passedIds(query.getTagIds())
                    .build();
        }
        boolean noTrade = couples.stream().anyMatch(couplePO -> CoupleStatusEnum.NON_TRADE == couplePO.getStatus());
        Set<String> tradeCompletedCouples = couples.stream().filter(couplePO -> CoupleStatusEnum.TRADE_COMPLETED == couplePO.getStatus())
                .map(CouplePO::getCode)
                .collect(Collectors.toSet());
        return CoupleValidTradeStatusVO.builder()
                .pass(!noTrade)
                .passedIds(tradeCompletedCouples)
                .build();
    }

    public List<Couple> getCouples(Collection<String> codes) {
        List<CouplePO> couplePOS = this.coupleService.list(Wrappers.<CouplePO>lambdaQuery()
                                                                   .in(CouplePO::getCode, codes));
        List<Couple> couples = new ArrayList<>();
        couplePOS.forEach(couplePO -> {
            Couple couple = new Couple(
                    couplePO.getId(),
                    couplePO.getCode(),
                    new CoupleStatus(couplePO.getStatus()),
                    couplePO.getReportTime());
            couples.add(couple);
        });
        return couples;
    }
}
