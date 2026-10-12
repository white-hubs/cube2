package com.guhecloud.cube.srv.gen.safety.personnel.server.org.interfaces;

import com.guhecloud.cube.srv.gen.safety.personnel.server.org.service.OrgNodeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo.HomeOrgArchitectureVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 安全生产统一智管平台首页接口。
 */
@RestController("safetyPersonnelHomeController")
@RequestMapping
@RequiredArgsConstructor
public class HomeController {

    private final OrgNodeService orgNodeService;

    /**
     * 查询首页安全组织架构。
     *
     * @return 首页安全组织架构；无组织配置时根节点为空。
     */
    @GetMapping("/home/org-architecture")
    public HomeOrgArchitectureVO orgArchitecture() {
        return orgNodeService.homeArchitecture();
    }
}
