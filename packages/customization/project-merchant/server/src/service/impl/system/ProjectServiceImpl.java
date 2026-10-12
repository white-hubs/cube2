package com.guhecloud.cube.deliver.project.merchant.service.impl.system;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.entity.Project;
import com.guhecloud.cube.deliver.project.merchant.mapper.system.ProjectMapper;
import com.guhecloud.cube.deliver.project.merchant.service.system.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

}
