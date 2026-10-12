package com.guhecloud.cube.biz.fusion.communicate.server.third.base.common;

import com.guhecloud.cube.biz.fusion.communicate.server.third.account.facade.param.query.ThirdFusionFileFacadeQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo.ThirdFusionFileVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.IFusionCommunicateFile;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.common.service.IThirdFusionFileService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.param.FileRecordQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.FusionFileVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;

public abstract class AbstractCommonFusionCommunicateFile implements IFusionCommunicateFile {

    private final IThirdFusionFileService thirdFusionFileService;

    protected AbstractCommonFusionCommunicateFile(IThirdFusionFileService thirdFusionFileService) {
        this.thirdFusionFileService = thirdFusionFileService;
    }

    @Override
    public Pagination<FusionFileVO> getFilePages(FileRecordQueryParam queryParam) {
        ThirdFusionFileFacadeQueryParam thirdFusionFileFacadeQueryParam = ObjectUtils
                .copying(queryParam, ThirdFusionFileFacadeQueryParam.class);
        Pagination<ThirdFusionFileVO> thirdFusionFilePage = this.thirdFusionFileService
                .getThirdFusionFilePage(thirdFusionFileFacadeQueryParam);
        return PagingUtils.convert(thirdFusionFilePage, ObjectUtils
                .copying(thirdFusionFilePage.getRecords(), FusionFileVO.class));
    }

}
