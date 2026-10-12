package com.guhecloud.cube.srv.electron.guide.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.electron.guide.entity.GuideDeviceManage;
import com.guhecloud.cube.srv.electron.guide.param.GuideDeviceManageModifyParam;
import com.guhecloud.cube.srv.electron.guide.param.query.GuideDeviceManagePageQueryParam;
import com.guhecloud.cube.srv.electron.guide.vo.GuideDeviceManageDetailVO;
import com.guhecloud.cube.srv.electron.guide.vo.GuideDeviceManagePageVO;
import com.guhecloud.platform.base.model.Pagination;

public interface GuideDeviceManageService extends IService<GuideDeviceManage> {

    Long save(GuideDeviceManageModifyParam param);

    void update(Long id, GuideDeviceManageModifyParam param);

    void delete(Long id);

    GuideDeviceManageDetailVO getDetail(Long id);

    Pagination<GuideDeviceManagePageVO> getPage(GuideDeviceManagePageQueryParam param);
}
