package com.guhecloud.cube.deliver.project.merchant.controller.message;//package com.guhecloud.cube.srv.gen.maintain.electron.server.controller.assembly;

import com.alibaba.fastjson2.JSONArray;
import com.guhecloud.cube.deliver.project.merchant.service.message.ProjectMerchantMessageService;
import com.guhecloud.cube.deliver.project.merchant.dto.message.ProjectMerchantAssemblyNoticeParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 消息配置
 *
 * @author : lijie
 * @date : 2023-5-24
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectMerchantMessageController {
    private final ProjectMerchantMessageService projectMerchantMessageService;

    /**
     * 通过业务类型查询消息配置
     *
     * @return 消息配置
     */
    @GetMapping("/queryMessget")
    public JSONArray queryCondition(String businessType, String moduleType) {
        return projectMerchantMessageService.queryCondition(businessType,moduleType);
    }

    /**
     * 新增消息配置
     *
     * @param param 消息配置
     * @return 新增结果
     */
    @PostMapping("/messget/insert")
    public Boolean insert(@RequestBody @Valid List<ProjectMerchantAssemblyNoticeParam> param) {
        return projectMerchantMessageService.insert(param);
    }
}
