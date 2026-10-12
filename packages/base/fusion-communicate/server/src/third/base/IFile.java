package com.guhecloud.cube.biz.fusion.communicate.server.third.base;

import com.guhecloud.cube.biz.fusion.communicate.server.third.base.param.FileRecordQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.FusionFileVO;
import com.guhecloud.platform.base.model.Pagination;

public interface IFile {

    Pagination<FusionFileVO> getFilePages(FileRecordQueryParam queryParam);

    FusionFileVO saveFileById(String id);
}
