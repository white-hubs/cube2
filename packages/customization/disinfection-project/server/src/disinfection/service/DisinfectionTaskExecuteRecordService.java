package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionTaskExecuteRecord;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.ExecuteRecordPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.KeyPlaceExecuteRecordVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface DisinfectionTaskExecuteRecordService extends IService<DisinfectionTaskExecuteRecord> {

    List<DisinfectionTaskExecuteRecord> listByTaskId(long taskId);

    Pagination<KeyPlaceExecuteRecordVO> KeyPlaceExecuteRecordPage(ExecuteRecordPageParam param);

    Integer keyPlaceExecuteCount(Long keyPlaceId);
}