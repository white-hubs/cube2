package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectRecordToBusinessQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordToBusinessMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.facade.InspectRecordToBusinessFacade;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordToBusinessPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordToBusinessVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectRecordToBusinessQueryServiceImpl extends ServiceImpl<InspectRecordToBusinessMapper, InspectRecordToBusinessPO> implements InspectRecordToBusinessQueryService {

    private final InspectRecordToBusinessFacade facade;

    @Override
    public List<InspectRecordToBusinessVO> getListByRecordId(Long recordId) {
        List<InspectRecordToBusinessPO> pos = this.lambdaQuery().eq(InspectRecordToBusinessPO::getRecordId, recordId).list();
        List<InspectRecordToBusinessVO> vos = ObjectUtils.copying(pos, InspectRecordToBusinessVO.class);
        //  查询隐患排查和故障报修 业务 状态
        vos.stream().forEach(facade::extendRecordToBusiness);

        return vos;
    }

    /**
     * 根据巡检记录id集合批量获取转工单信息
     *
     * @param recordIdList 记录id集合
     *
     * @return map key:记录id，value:转工单信息集合
     */
    @Override
    public Map<Long, List<InspectRecordToBusinessVO>> getMapByRecordIdList(List<Long> recordIdList) {
        List<InspectRecordToBusinessVO> recordToBusinessVOList = this.baseMapper.getRecordToBusinessByRecordIdList(recordIdList);
        if (CollectionUtils.isEmpty(recordToBusinessVOList)) {
            return new HashMap<>();
        }
        // 扩充信息
        facade.extBusinessOrder(recordToBusinessVOList);
        return recordToBusinessVOList.stream().collect(Collectors.groupingBy(InspectRecordToBusinessVO::getRecordId));
    }
}
