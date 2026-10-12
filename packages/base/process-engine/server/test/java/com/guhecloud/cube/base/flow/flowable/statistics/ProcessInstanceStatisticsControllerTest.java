package com.guhecloud.cube.base.flow.flowable.statistics;

import com.guhecloud.cube.base.flow.FlowServer;
import com.guhecloud.cube.base.flow.flowable.statistics.interfaces.ProcessInstanceStatisticsController;
import com.guhecloud.cube.base.flow.flowable.statistics.vo.ProcessInstanceNumberOfAllVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest(classes = FlowServer.class)
@RunWith(SpringRunner.class)
public class ProcessInstanceStatisticsControllerTest {

    @Autowired
    private ProcessInstanceStatisticsController processInstanceStatisticsController;

    @Before
    public void before() {
        SessionContextHolder.requestTenant(9578171);
        SecurityContextHolder.setPrincipal(Principal.builder().account("1516963014832627714").id(1516963014832627714L).type(Principal.Type.CLIENT).build());
    }

    @Test
    public void userHandle() {
        ProcessInstanceNumberOfAllVO processInstanceNumberOfAllVO = processInstanceStatisticsController.userHandle(null, "ATTENDANCE_MANAGEMENT", null);
        log.info(processInstanceNumberOfAllVO.toString());
    }
}
