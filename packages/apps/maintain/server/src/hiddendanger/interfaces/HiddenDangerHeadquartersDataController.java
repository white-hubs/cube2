package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerNewService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HeadquartersDataGatherVo;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.query.HeadquartersDataGatherQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description 深农安全园区总部数据
 * @Author lrj
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerHeadquartersDataController {

    private final HiddenDangerNewService hiddenDangerNewService;

    @PreAuthorize("hasScope('hidden_danger')")
    @GetMapping("/hidden-danger-headquarters-ather")
    public List<HeadquartersDataGatherVo> headquartersDataGather(HeadquartersDataGatherQuery param) {
        return hiddenDangerNewService.headquartersDataGather(param.getPeriod());
    }
}
