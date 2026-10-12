package com.guhecloud.cube.base.message2.server.sat.param.validator;

import com.guhecloud.cube.base.message2.server.sat.param.SatUserMsgIgnoredConfigParam;
import com.guhecloud.platform.base.standard.exception.InvalidArgumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 用户消息忽略配置校验器
 *
 * @author lanchun
 * @date 2024/9/2 10:36
 */
@Slf4j
@Component
public class SatUserMsgIgnoredConfigValidator {

    public void verifyUserMsgIgnoredConfig(SatUserMsgIgnoredConfigParam param) {
        if (Objects.equals(Boolean.FALSE, param.getEnabled())) {
            return;
        }
        if (Objects.equals(Boolean.TRUE, param.getTomorrows())) {
            return;
        }
        //跨日的情况如果结束时间小于开始时间,则为非法参数
        if (param.getEndTime().compareTo(param.getStartTime()) <= 0) {
            throw new InvalidArgumentException("结束时间不能小于等于开始时间");
        }
    }
}
