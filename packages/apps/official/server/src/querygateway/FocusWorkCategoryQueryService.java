package com.guhecloud.cube.srv.gen.official.management.server.querygateway;

import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.official.management.server.dao.FocusWorkCategoryMapper;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkCategoryPO;
import com.guhecloud.cube.srv.gen.official.management.server.querygateway.query.FocusWorkCategoryParam;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FocusWorkCategoryQueryService extends ServiceImpl<FocusWorkCategoryMapper, FocusWorkCategoryPO> {

    public Page<FocusWorkCategoryPO> pageFocusWorkCategory(FocusWorkCategoryParam param) {
        return this.page(PagingUtils.convert(param), Wrappers.<FocusWorkCategoryPO>lambdaQuery()
                .like(StringUtils.isNotBlank(param.getName()), FocusWorkCategoryPO::getName, param.getName()));

    }

    public List<FocusWorkCategoryPO> listFocusWorkCategory(FocusWorkCategoryParam param) {
        return this.list(Wrappers.<FocusWorkCategoryPO>lambdaQuery()
                                 .like(StringUtils.isNotBlank(param.getName()), FocusWorkCategoryPO::getName, param.getName()));

    }
}
