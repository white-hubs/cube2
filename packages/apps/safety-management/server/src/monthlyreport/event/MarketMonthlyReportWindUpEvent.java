package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.event;

import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MarketMonthlyReportPO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MarketMonthlyReportWindUpEvent extends ApplicationEvent {

    private MarketMonthlyReportPO marketMonthlyReportPO;

    public MarketMonthlyReportWindUpEvent(Object source, MarketMonthlyReportPO marketMonthlyReportPO) {
        super(source);
        this.marketMonthlyReportPO = marketMonthlyReportPO;
    }
}
