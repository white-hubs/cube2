package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.param.FormDataParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttExchangeCheckDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway.AttClassExchangeQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.vo.AttKeyValueVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.commandgateway.command.ClassesChangeCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 换班申请
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttClassExchangeController {

    private final FlowClient flowClient;
    private final AttClassExchangeQueryService attClassExchangeQueryService;

    /**
     * 获取人员换班班次
     *
     * @param workType 换班方式
     * @param staffId  换班人员id
     * @param day      换班日期 yyyy-MM-dd
     *
     * @return 班次
     */
    @GetMapping("/att/exchange/classes")
    public List<AttKeyValueVO> getStaffChangeClasses(@RequestParam("workType") String workType, @RequestParam("staffId") Long staffId,
                                                     @RequestParam("day") String day
                                                    ) {

        return attClassExchangeQueryService.getStaffChangeClasses(workType, staffId, day);
    }

    /**
     * 校验换班班次是否符合规则
     *
     * @param param 对象
     *
     * @return 是否符合
     */
    @PostMapping("/att/exchange/check")
    public Boolean classesChangeCheck(@RequestBody FormDataParam param) {
        String biz = flowClient.formParse(param);
        AttExchangeCheckDTO checkDTO = JSONObject.parseObject(biz, AttExchangeCheckDTO.class);
        ClassesChangeCommand command = new ClassesChangeCommand();
        BeanUtils.copyProperties(checkDTO, command);
        return attClassExchangeQueryService.classesChangeCheck(checkDTO);
    }

}
