package com.guhecloud.cube.srv.gen.official.management.server.commandgateway;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.FlowFormDataMapper;
import com.guhecloud.cube.srv.gen.official.management.server.po.FlowFormDataPO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author appet
 * @date 2023/02/21
 * @description 流程审批结束处理
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FlowFormDataService {
    private final FlowFormDataMapper flowFormDataMapper;
    private final UserClient userClient;

    @Transactional(rollbackFor = Exception.class)
    public void addFlowFormDataAfterFlowCompleted(FlowFormDataPO flowFrom) {
        UserDTO user = userClient.getUser(flowFrom.getApplicantId());
        if (user != null) {
            flowFrom.setApplicantDeptId(user.getOrganization().getId());
            flowFrom.setApplicantDeptName(user.getOrganization().getName());
            flowFrom.setApplicantName(user.getName());
        }
        flowFormDataMapper.insert(flowFrom);
    }
}
