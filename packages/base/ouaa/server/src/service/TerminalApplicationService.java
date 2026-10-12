package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.TerminalApplication;
import com.guhecloud.cube.biz.ouaa.server.vo.BaseInfo;

import java.util.List;

public interface TerminalApplicationService extends IService<TerminalApplication> {

    Long save(Long terminalId, Long applicationId);

    void savePath(Long terminalId, Long applicationId, String path);

    void remove(Long terminalId, Long applicationId);

    void removeByTerminal(Long terminalId);

    void removeByApplication(Long applicationId);

    List<TerminalApplication> listByTerminal(Long terminalId);

    void saveOrUpdateBatch(Long id, List<Long> terminalIds);

    List<BaseInfo> getTerminalList(Long id);
}
