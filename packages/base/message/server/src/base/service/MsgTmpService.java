package com.guhecloud.cube.base.message2.server.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.message2.server.base.entity.MsgTmp;
import com.guhecloud.cube.base.message2.server.base.param.MsgTmpCopyParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgTmpModifyParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgTmpQueryParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgTmpUpdateParam;
import com.guhecloud.cube.base.message2.server.base.vo.MsgTmpPageVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.Collection;
import java.util.List;

public interface MsgTmpService extends IService<MsgTmp> {

    Long save(MsgTmpModifyParam param);

    void update(Long id, MsgTmpUpdateParam param);

    Pagination<MsgTmpPageVO> getMsgTmpPage(MsgTmpQueryParam param);

    void copyChannelMsgTmp(MsgTmpCopyParam param);

    List<MsgTmpPageVO> getMsgTmpListByApplicationCodes(Collection<String> applicationCodes);

    MsgTmp getById(Long id);

    void remove(Long id);

}
