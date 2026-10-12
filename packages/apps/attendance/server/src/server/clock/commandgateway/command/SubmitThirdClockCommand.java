package com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2023/03/21
 * @description 提交第三方打卡
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitThirdClockCommand {

    private String deviceCode;

    private String userId;

    private LocalDateTime clockTime;

}