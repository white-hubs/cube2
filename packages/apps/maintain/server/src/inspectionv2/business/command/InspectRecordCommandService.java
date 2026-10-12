package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordCancelBatchCommand;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordItemCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordSubmitCommand;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordToBusinessCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectTaskRecordSubmitCommand;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;

import java.util.List;

public interface InspectRecordCommandService extends IService<InspectRecordPO> {

    void saveByTask(InspectTaskPO taskPO);

    void stopByTaskId(Long taskId);

    void submitRecord(Long id, List<InspectRecordItemCommandModel> commandModels);

    void submitTaskRecordBatch(List<InspectTaskRecordSubmitCommand> commandList);

    void submitRecord(String targetCategory, Long targetId, String targetName, List<InspectRecordItemCommandModel> commandModels,
                      String areaId, String areaName
                     );

    void toBusiness(Long id, InspectRecordToBusinessCommandModel commandModel);

    void expireByTaskId(Long taskId);

    Boolean submitTempRecordBatch(List<InspectRecordSubmitCommand> commandList);

    void taskRecordErrorSendMessage(List<Long> recordIds, String userName);

    void orderCompleteCallback(String processInstanceId);

    Boolean batchCancelInspect(InspectRecordCancelBatchCommand param);
}
