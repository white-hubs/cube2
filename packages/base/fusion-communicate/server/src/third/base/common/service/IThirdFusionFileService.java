package com.guhecloud.cube.biz.fusion.communicate.server.third.base.common.service;

import com.guhecloud.cube.biz.fusion.communicate.server.third.account.facade.param.command.IncreaseThirdFusionFileFacadeCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.facade.param.query.ThirdFusionFileFacadeQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo.ThirdFusionFileVO;
import com.guhecloud.platform.base.model.Pagination;

public interface IThirdFusionFileService {

    void saveThirdFusionFile(IncreaseThirdFusionFileFacadeCommand increaseThirdFusionFileFacadeCommand);

    Pagination<ThirdFusionFileVO> getThirdFusionFilePage(ThirdFusionFileFacadeQueryParam param);
}
