package com.guhecloud.cube.biz.fusion.communicate.server.service;

import com.guhecloud.cube.biz.fusion.communicate.server.third.base.param.FileRecordQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.strategy.FusionCommunicateStrategy;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.FusionFileVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FusionCommunicateFileService {

    private final FusionCommunicateStrategy fusionCommunicateStrategy;

    public FusionCommunicateFileService(FusionCommunicateStrategy fusionCommunicateStrategy) {
        this.fusionCommunicateStrategy = fusionCommunicateStrategy;
    }

    public Pagination<FusionFileVO> getFilePages(FileRecordQueryParam queryParam) {
        return this.fusionCommunicateStrategy.getFusionCommunicateFile().getFilePages(queryParam);
    }

    public FusionFileVO saveFileById(String id) {
        return this.fusionCommunicateStrategy.getFusionCommunicateFile().saveFileById(id);
    }
}
