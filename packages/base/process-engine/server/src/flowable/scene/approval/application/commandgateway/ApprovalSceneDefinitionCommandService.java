package com.guhecloud.cube.base.flow.flowable.scene.approval.application.commandgateway;

import com.guhecloud.cube.base.flow.flowable.definition.application.commandgateway.ProcessDefinitionCommandService;
import com.guhecloud.cube.base.flow.flowable.definition.application.dto.ProcessDefinitionDTO;
import com.guhecloud.cube.base.flow.flowable.definition.domain.factory.ApprovalSceneDefinitionFactory;
import com.guhecloud.cube.base.flow.flowable.definition.domain.repository.ApprovalSceneDefinitionRepository;
import com.guhecloud.cube.base.flow.flowable.scene.approval.application.commandgateway.command.DeployApprovalSceneDefinitionCommand;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * @author pengjian
 * @date 2022/8/30 16:02
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(rollbackFor = Exception.class)
public class ApprovalSceneDefinitionCommandService {
    private final ProcessDefinitionCommandService processDefinitionCommandService;

    private final ApprovalSceneDefinitionRepository approvalSceneDefinitionRepository;

    public ProcessDefinitionDTO deploy(DeployApprovalSceneDefinitionCommand deployApprovalSceneDefinitionCommand) {
        try {
            ProcessDefinitionDTO processDefinitionDTO = processDefinitionCommandService.deploy(
                    deployApprovalSceneDefinitionCommand
                                                                                              );
            // 解决多次部署问题
            approvalSceneDefinitionRepository.save(
                    ApprovalSceneDefinitionFactory.create(deployApprovalSceneDefinitionCommand, processDefinitionDTO.getKey())
                                                  );
            return processDefinitionDTO;
        } catch (IOException e) {
            log.error("流程部署错误:{}", e.getMessage());
            throw new ServiceException("流程部署错误");
        }
    }

}
