package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dao.HiddenDangerTypeMapper;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerType;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.HiddenDangerTypeQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.query.HiddenDangerTypeCollectionQuery;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author lzy
 * @date 2022/4/26
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerTypeQueryServiceImpl extends ServiceImpl<HiddenDangerTypeMapper, HiddenDangerType> implements HiddenDangerTypeQueryService {

    @Override
    public List<HiddenDangerType> listHiddenDangerType(@NonNull HiddenDangerTypeCollectionQuery query) {
        return this.baseMapper.selectList(Wrappers.<HiddenDangerType>lambdaQuery()
                                                  .eq(Objects.nonNull(query.getHiddenDangerCategoryId()), HiddenDangerType::getHiddenDangerCategoryId, query.getHiddenDangerCategoryId())
                                                  .eq(query.getHiddenDangerTypeId() !=
                                                      null, HiddenDangerType::getId, query.getHiddenDangerTypeId())
                                         );
    }
}
