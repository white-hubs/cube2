package com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.MaintenaceProjectCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenanceProjectCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.DeviceCategoryMaintenanceVO;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenanceProjectVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.MaintenaceProjectService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query.MaintenanceProjectQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lrj
 * @Description 维保配置项
 * @date 2022-4-29 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenanceProjectController {
    private final MaintenaceProjectCommandService maintenaceProjectCommandService;

    private final MaintenaceProjectService maintenaceProjectService;

    /**
     * @Description 保存维保项
     * @Author lrj
     */
    @PostMapping("/save-maintenance-project")
    public void save(@RequestBody List<MaintenanceProjectCommand> commands) {
        maintenaceProjectCommandService.save(commands);
    }

    /**
     * @Description 维保项列表
     * @Author lrj
     */
    @GetMapping("/select-maintenance-project")
    public Pagination<DeviceCategoryMaintenanceVO> selectMaintenanceProject(MaintenanceProjectQueryParam param) {
        return maintenaceProjectService.selectMaintenaceProject(param);
    }

    /**
     * @Description 品类维保项列表
     * @Author lrj
     */
    @GetMapping("/select-maintenance-projectDetail")
    public List<MaintenanceProjectVo> selectMaintenanceProjectDetail(MaintenanceProjectQueryParam param) {
        return maintenaceProjectService.selectMaintenaceProjectDetail(param);
    }

}