package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.DeviceStatisticParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * 隐患排查2.0公共接口
 *
 * @author wql
 * @describe 隐患排查2.0
 * @date 2023/11/8 20:39
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicHiddenDangerNewController {

    private final HiddenDangerNewController hiddenDangerNewController;

    /**
     * 对象隐患单统计
     *
     * @param param 统计范围
     *
     * @return 隐患单id列表
     */
    @GetMapping("/public/hidden/order/statistic")
    public Set<String> objHiddenOrderStatistics(DeviceStatisticParam param) {
        return hiddenDangerNewController.objHiddenOrderStatistics(param);
    }
}
