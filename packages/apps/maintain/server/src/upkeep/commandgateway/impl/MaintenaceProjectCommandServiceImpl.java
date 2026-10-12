package com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.MaintenaceProjectCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenanceProjectCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.dao.MaintenanceProjectMapper;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceProject;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenaceProjectCommandServiceImpl extends ServiceImpl<MaintenanceProjectMapper, MaintenanceProject> implements MaintenaceProjectCommandService {

    private final MaintenanceProjectMapper maintenanceProjectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(List<MaintenanceProjectCommand> commands) {
        if (ObjectUtils.isNotEmpty(commands)) {
            commands.forEach(command -> {
                if (ObjectUtils.isNotNull(command.getId())) {
                    MaintenanceProject project = maintenanceProjectMapper.selectById(command.getId());
                    project.setMaintenanceContent(command.getMaintenanceContent());
                    project.setDeleted(command.getDeleted());
                    maintenanceProjectMapper.updateById(project);
                } else {
                    MaintenanceProject copying = ObjectUtils.copying(command, MaintenanceProject.class);
                    copying.setCode("XJD" + IdWorker.getId());
                    copying.setTenant(SessionContextHolder.getRequester().getTenant());
                    this.save(copying);
                }
            });
        }
    }

}
