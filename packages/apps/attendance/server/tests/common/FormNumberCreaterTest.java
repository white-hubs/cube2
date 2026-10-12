package com.guhecloud.cube.srv.gen.attendance.management.server.common;

import com.guhecloud.cube.srv.gen.attendance.management.AttendanceManagementServer;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.tool.FormNumberCreater;
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
@SpringBootTest(classes = AttendanceManagementServer.class)
@RunWith(SpringRunner.class)
public class FormNumberCreaterTest {
    @Autowired
    private FormNumberCreater formNumberCreater;

    @Before
    public void before() {
        SessionContextHolder.requestTenant(9578171);
        SecurityContextHolder.setPrincipal(Principal.builder().account("1").id(1L).type(Principal.Type.CLIENT).build());
    }

    @Test
    public void create() {
        String qj = formNumberCreater.create("QJ");
    }
}
