package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetDTO;
import lombok.Data;

import java.util.List;

/**
 * 任务接受人
 */
@Data
public class TaskReceiveUsersParam {
    private String receiveIds;
    private List<InspectTargetDTO> taskTargetDTOList;
}
