package com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.querygateway;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.ThirdFusionFileService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.querygateway.query.ThirdFusionFileQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.po.ThirdFusionFilePO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo.ThirdFusionFileVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdFusionFileQueryService {

    private final ThirdFusionFileService thirdFusionFileService;

    public Pagination<ThirdFusionFileVO> getThirdFusionFilePage(@Valid ThirdFusionFileQueryParam param) {
        Page<ThirdFusionFilePO> fusionFilePOPage = this.thirdFusionFileService.queryThirdFusionFilePage(param);
        return PagingUtils.convert(fusionFilePOPage, ThirdFusionFileVO.class);
    }
}
