package com.guhecloud.cube.srv.gen.park.management.server.querygateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.park.management.server.dao.TypeDataMapper;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.TypeDataVO;
import com.guhecloud.cube.srv.gen.park.management.server.po.TypeDataPO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.TypeDataQueryService;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.entity.BasicEntity;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("UnnecessaryTypeArgument")
public class TypeDataQueryServiceImpl extends ServiceImpl<TypeDataMapper, TypeDataPO> implements TypeDataQueryService {

    @Override
    public List<TypeDataVO> getListByTag(String tag, String keyword) {
        List<TypeDataPO> pos = this.<TypeDataPO>lambdaQuery()
                .eq(TypeDataPO::getTag, tag)
                .like(StringUtils.isNotEmpty(keyword), TypeDataPO::getName, keyword)
                .orderByDesc(BasicEntity::getCreateTime)
                .list();

        return ObjectUtils.copying(pos, TypeDataVO.class);
    }
}
