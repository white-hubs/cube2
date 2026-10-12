package com.guhecloud.cube.biz.smartcard.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.smartcard.server.entity.EventRecord;
import com.guhecloud.cube.biz.smartcard.server.queryparam.AccessEventQueryParam;
import com.guhecloud.cube.biz.smartcard.server.vo.CommonStatVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("smartcard_EventRecord_mapper")
public interface EventRecordMapper extends BaseMapper<EventRecord> {
    List<CommonStatVO> getEventStatByName(@Param("param") AccessEventQueryParam param);
}
