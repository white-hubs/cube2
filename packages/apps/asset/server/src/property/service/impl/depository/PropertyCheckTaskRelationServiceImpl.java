package com.guhecloud.cube.srv.gen.property.management.server.property.service.impl.depository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyCheckTaskRelationMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyCheckTaskRelation;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyCheckTaskRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 盘点任务中间表服务实现类
 *
 * @author : lijie
 * @date : 2023-3-20
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyCheckTaskRelationServiceImpl extends ServiceImpl<PropertyCheckTaskRelationMapper, PropertyCheckTaskRelation> implements PropertyCheckTaskRelationService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertList(List<PropertyCheckTaskRelation> taskRelationList) {
        log.info("盘点任务关联资产");
        return this.saveBatch(taskRelationList);
    }
}
