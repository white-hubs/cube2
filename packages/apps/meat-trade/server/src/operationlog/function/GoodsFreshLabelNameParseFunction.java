package com.guhecloud.cube.biz.meattrade.server.operationlog.function;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import com.mzt.logapi.service.IParseFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 冷热鲜标识转换
 *
 * @author lanchun
 * @date 2023/8/9 9:55
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsFreshLabelNameParseFunction implements IParseFunction {

    @Override
    public String functionName() {
        return "FRESH_LABEL_NAME";
    }

    @Override
    public String apply(Object value) {
        MeatGoodsFreshLabelEnum meatGoodsFreshLabelEnum = (MeatGoodsFreshLabelEnum) value;
        return Objects.nonNull(meatGoodsFreshLabelEnum) ? meatGoodsFreshLabelEnum.getDescription() : null;
    }
}
