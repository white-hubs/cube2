package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.AlarmTypeMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmTypeQuery;
import com.guhecloud.cube.srv.gen.safety.management.po.AlarmTypePO;
import com.guhecloud.cube.srv.gen.safety.management.service.AlarmTypeService;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service(value = "safetyAlarmTypeService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmTypeServiceImpl extends ServiceImpl<AlarmTypeMapper, AlarmTypePO> implements AlarmTypeService {

    @Override
    public Page<AlarmTypePO> getPage(AlarmTypeQuery query) {
        return this.baseMapper.selectPage(PagingUtils.convert(query), Wrappers.<AlarmTypePO>lambdaQuery()
                .and(StringUtils.isNotEmpty(query.getKeyword()), q -> q
                        .like(AlarmTypePO::getAlarmTypeName, query.getKeyword())
                        .or()
                        .like(AlarmTypePO::getApplicationName, query.getKeyword()))
                .orderByDesc(AlarmTypePO::getCreateTime));
    }

    @Override
    public List<AlarmTypePO> getAlarmTypes(AlarmTypeQuery query) {
        return this.baseMapper.selectList(Wrappers.<AlarmTypePO>lambdaQuery()
                                                  .and(StringUtils.isNotEmpty(query.getKeyword()), q -> q
                                                          .like(AlarmTypePO::getAlarmTypeName, query.getKeyword())
                                                          .or()
                                                          .like(AlarmTypePO::getApplicationName, query.getKeyword()))
                                                  .orderByDesc(AlarmTypePO::getCreateTime));
    }
}
