package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.job.dto;

import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifyTaskDTO {
    private DisinfectionTask disinfectionTask;
    private MessageSendConfig messageSendConfig;
}
