package com.guhecloud.cube.onecard.server.consumption.controller;

import com.guhecloud.cube.onecard.server.consumption.enumeration.RiXinDaInterfaceEnum;
import com.guhecloud.cube.onecard.server.consumption.param.ConsumeRecordOutParam;
import com.guhecloud.cube.onecard.server.consumption.service.ConsumeRecordOutService;
import com.guhecloud.cube.onecard.server.consumption.vo.ConsumeRecordVO;
import com.guhecloud.cube.onecard.server.consumption.vo.TransactionTypeVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消费记录查询
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 17:43
 * @Description:
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/consume-record-out")
public class ConsumeRecordOutController {

    private final ConsumeRecordOutService consumeRecordOutService;

    /**
     * 查询消费记录
     *
     * @param param
     *
     * @return
     */

    @RequestMapping("/page")
    public Pagination<ConsumeRecordVO> getConsumeRecordOut(ConsumeRecordOutParam param) {
        return consumeRecordOutService.getConsumeRecordOut(param);
    }

    /**
     * 记录类型下拉框
     *
     * @return
     */
    @GetMapping("/record-type")
    public List<TransactionTypeVo> getTransactionType() {
        return Arrays.stream(RiXinDaInterfaceEnum.values())
                .map(e -> TransactionTypeVo.builder()
                        .name(e.getDesc())
                        .value(e.name())
                        .build()).collect(Collectors.toList());
    }

}
