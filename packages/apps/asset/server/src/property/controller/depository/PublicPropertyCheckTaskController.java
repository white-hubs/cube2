package com.guhecloud.cube.srv.gen.property.management.server.property.controller.depository;

import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckTaskParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyCheckTaskPageVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公共盘点任务控制层
 *
 * @author lanchun
 * @date 2025/1/3 14:44
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicPropertyCheckTaskController {

    private final PropertyCheckTaskController propertyCheckTaskController;

    /**
     * 资产盘点任务列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/public/property/check/task/queryTaskByPropertyIdPage")
    public Pagination<PropertyCheckTaskPageVO> publicQueryTaskByPropertyId(PropertyCheckTaskParam param) {
        return propertyCheckTaskController.queryTaskByPropertyId(param);
    }
}
