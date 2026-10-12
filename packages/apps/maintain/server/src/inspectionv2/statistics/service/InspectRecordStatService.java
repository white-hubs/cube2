package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordListVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.dao.InspectRecordStatMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.param.InspectRecordItemTagCodeStatParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.param.InspectRecordPageParam;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectRecordStatService {

    private final InspectRecordStatMapper inspectRecordStatMapper;

    /**
     * 根据获取标签巡检记录数
     *
     * @param param
     *
     * @return
     */
    public Integer getInspectRecordItemTagCodeStat(InspectRecordItemTagCodeStatParam param) {
        return inspectRecordStatMapper.selectInspectRecordItemTagCodeStat(param);
    }

    /**
     * 根据标签获取巡检记录列表
     *
     * @param param
     *
     * @return
     */
    public Page<InspectRecordListVO> getInspectRecordPageByTagCode(InspectRecordPageParam param) {
        return inspectRecordStatMapper.selectInspectRecordPageByTagCode(PagingUtils.convert(param), param);
    }
}
