package com.guhecloud.cube.srv.gen.official.management.server.querygateway;

import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.official.management.server.dao.OfficialSealMapper;
import com.guhecloud.cube.srv.gen.official.management.server.po.OfficialSealPO;
import com.guhecloud.cube.srv.gen.official.management.server.querygateway.query.OfficialSealParam;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OfficialSealQueryService extends ServiceImpl<OfficialSealMapper, OfficialSealPO> {

    public Page<OfficialSealPO> pageOfficialSeal(OfficialSealParam param) {
        return this.page(PagingUtils.convert(param), Wrappers.<OfficialSealPO>lambdaQuery()
                .like(StringUtils.isNotBlank(param.getSealName()), OfficialSealPO::getSealName, param.getSealName()));

    }

    public List<OfficialSealPO> listOfficialSeal(OfficialSealParam param) {
        return this.list(Wrappers.<OfficialSealPO>lambdaQuery()
                                 .like(StringUtils.isNotBlank(param.getSealName()), OfficialSealPO::getSealName, param.getSealName()));

    }
}
