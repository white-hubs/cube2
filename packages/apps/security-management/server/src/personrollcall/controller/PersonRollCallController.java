package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.param.PersonRollCallPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.service.PersonRollCallService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo.PersonRollCallPageVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 人员点名信息 PERSONEL_COUNT
 */
@Slf4j
@RestController
@RequestMapping("/person/roll/call")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PersonRollCallController {
    private final PersonRollCallService personRollCallService;

    /**
     * 人员点名信息分页
     */
    @GetMapping("/page")
    public Pagination<PersonRollCallPageVO> getPage(PersonRollCallPageParam param) {
        return personRollCallService.getPage(param);
    }
}
