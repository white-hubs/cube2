package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.dto;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TaskDetailResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TaskObjCountResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.PerformInspectionVO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 单个巡检任务的离线快照数据。
 * <p>
 * App 导入该对象后即可离线展示任务、巡检对象以及执行页面所需内容。
 */
@Data
@Builder
public class InspectOfflineTaskSnapshotDTO {
    private Long taskId;
    private Long revision;
    private LocalDateTime snapshotTime;
    private TaskDetailResult detail;
    private TaskObjCountResult objectCount;
    private List<InspectionObjVO> objects;
    private List<PerformInspectionVO> inspections;
}
