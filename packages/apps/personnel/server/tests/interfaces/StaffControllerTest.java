package com.guhecloud.cube.srv.gen.personnel.management.server.interfaces;

import com.guhecloud.cube.srv.gen.personnel.management.server.PersonnelManagementServer;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.command.ChangeWorkingStatusCommand;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.WorkingStatus;
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

import java.util.Arrays;

@Slf4j
@SpringBootTest(classes = PersonnelManagementServer.class)
@RunWith(SpringRunner.class)
public class StaffControllerTest {
    @Autowired
    private StaffController staffController;

    @Before
    public void before() {
        SessionContextHolder.requestTenant(9578171);
        SecurityContextHolder.setPrincipal(Principal.builder().account("1").id(1L).type(Principal.Type.CLIENT).build());
    }

    @Test
    public void changeWorkingStatus() {
        ChangeWorkingStatusCommand statusCommand = ChangeWorkingStatusCommand.builder()
                .employeeId(2L)
                .workingStatus(WorkingStatus.ASK_LEAVE)
                .build();
        staffController.changeWorkingStatus(Arrays.asList(statusCommand));
    }
}
