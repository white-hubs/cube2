package com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway;

import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.ThirdFusionFileService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway.command.IncreaseThirdFusionFileCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.domain.po.ThirdFusionFilePO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdFusionFileCommandService {

    private final ThirdFusionFileService thirdFusionFileService;

    public void increaseThirdFusionFile(IncreaseThirdFusionFileCommand increaseThirdFusionFileCommand) {
        ThirdFusionFilePO fusionFilePO = ObjectUtils.copying(increaseThirdFusionFileCommand, ThirdFusionFilePO.class);
        this.thirdFusionFileService.saveThirdFusionFile(fusionFilePO);
    }
}
