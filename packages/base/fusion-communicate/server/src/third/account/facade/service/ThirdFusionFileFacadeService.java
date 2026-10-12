package com.guhecloud.cube.biz.fusion.communicate.server.third.account.facade.service;

import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway.ThirdFusionFileCommandService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway.command.IncreaseThirdFusionFileCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.querygateway.ThirdFusionFileQueryService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.querygateway.query.ThirdFusionFileQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.facade.param.command.IncreaseThirdFusionFileFacadeCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.facade.param.query.ThirdFusionFileFacadeQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo.ThirdFusionFileVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.common.service.IThirdFusionFileService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdFusionFileFacadeService implements IThirdFusionFileService {

    private final ThirdFusionFileCommandService thirdFusionFileCommandService;

    private final ThirdFusionFileQueryService thirdFusionFileQueryService;

    @Override
    public void saveThirdFusionFile(IncreaseThirdFusionFileFacadeCommand increaseThirdFusionFileFacadeCommand) {
        IncreaseThirdFusionFileCommand increaseThirdFusionFileCommand = ObjectUtils
                .copying(increaseThirdFusionFileFacadeCommand, IncreaseThirdFusionFileCommand.class);
        this.thirdFusionFileCommandService.increaseThirdFusionFile(increaseThirdFusionFileCommand);
    }

    @Override
    public Pagination<ThirdFusionFileVO> getThirdFusionFilePage(ThirdFusionFileFacadeQueryParam param) {
        ThirdFusionFileQueryParam thirdFusionFileQueryParam = ObjectUtils.copying(param, ThirdFusionFileQueryParam.class);
        return this.thirdFusionFileQueryService.getThirdFusionFilePage(thirdFusionFileQueryParam);
    }
}
