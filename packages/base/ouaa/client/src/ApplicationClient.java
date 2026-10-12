package com.guhecloud.cube.biz.ouaa.client;

import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationGroupDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ouaa")
public interface ApplicationClient {
    //通过code获取应用
    @GetMapping("/applicationByCode")
    ApplicationDTO getApplicationByCode(@RequestParam("code") String code);

    //通过codes获取应用
    @PostMapping("/applicationsByCodes")
    List<ApplicationDTO> getApplicationsByCodes(@RequestBody List<String> codes);

    //获取应用列表
    @GetMapping("/inner/applications")
    List<ApplicationDTO> getApplications();

    /**
     * @Description 获取租户下的terminal
     * @Author lrj
     */
    @GetMapping("/getTenantTerminal")
    List<String> getTenantTerminal(@RequestParam("tenantTerminal") String tenantTerminal);

    /**
     * 获取租户下的应用
     *
     * @param code
     *
     * @return
     */
    @GetMapping("/authenticated/tenant-applications/{code}")
    List<ApplicationDTO> getTenantApplications(@PathVariable("code") Integer code);

    /**
     * 获取当前用户在当前端下的应用组，包含应用信息
     *
     * @return
     */
    @GetMapping("/context/terminal-application-groups")
    List<ApplicationGroupDTO> getAuthorizedApplicationGroups();

    /**
     * 通过用户ID 查询该用户拥有的应用
     *
     * @param userId
     *
     * @return
     */
    @GetMapping("/user/{id}/applications")
    List<ApplicationDTO> getApplicationsByUserId(@PathVariable("id") Long userId);
}
