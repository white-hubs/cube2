package com.guhecloud.cube.srv.gen.adapter.information.server.huifeng.controller;

import com.guhecloud.cube.srv.gen.adapter.information.server.huifeng.qo.InformationParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.huifeng.service.impl.InformationServiceImpl;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.PLayRecordVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/huifeng")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InformationAdapterController {

    private final InformationServiceImpl informationService;

    @PostMapping("/getPayLogs")
    public List<PLayRecordVo> getPayLogs(InformationParam informationParam) {
        return informationService.getPayLogs(informationParam);
    }

}
