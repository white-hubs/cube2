package com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.message;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.interfaces.param.BaiDuClockInParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DuMuMachineClockInMessage {
    public static final String TOPIC = "ATT_DU_MU_CLOCK_IN";
    /**
     * 打卡信息
     */
    private BaiDuClockInParam clockInfo;
}
