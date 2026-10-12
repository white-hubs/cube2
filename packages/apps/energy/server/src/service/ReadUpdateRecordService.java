package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.ReadUpdateRecord;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.ReadUpdateRecordParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.ReadUpdateRecordQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ReadUpdateRecordVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：抄表修改记录服务层
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
public interface ReadUpdateRecordService extends IService<ReadUpdateRecord> {

    Long add(ReadUpdateRecordParam param);

    void update(Long id, ReadUpdateRecordParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    ReadUpdateRecordVO getById(Long id);

    Pagination<ReadUpdateRecordVO> getPage(ReadUpdateRecordQueryParam param);

}