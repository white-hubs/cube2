package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.dto.ApplicationDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.ApplicationMarketDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationBatchModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationQueryParam;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationVO;
import com.guhecloud.cube.biz.ouaa.server.vo.BaseInfo;
import com.guhecloud.platform.base.model.Pagination;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface ApplicationService extends IService<Application> {

    Long save(ApplicationModifyParam param);

    void update(Long id, ApplicationModifyParam param);

    void remove(Long id);

    Application getById(Serializable id);

    Application getByCode(String code);

    List<Application> listByIds(Collection<? extends Serializable> idList);

    List<Application> listByIds(Collection<? extends Serializable> idList, String keyword);

    Page<Application> listByPage(ApplicationQueryParam param);

    ApplicationDTO getApplicationByCode(String code);

    List<ApplicationDTO> getApplicationsByCodes(List<String> codes);

    List<ApplicationDTO> getApplications();

    ApplicationVO getApplicationDetailById(Long id);

    Pagination<ApplicationPageVO> getApplicationPage(ApplicationQueryParam param);

    List<BaseInfo> getList(ApplicationQueryParam param);

    List<ApplicationMarketDTO> getApplicationMarketList(ApplicationQueryParam param);

    void batchUpdate(ApplicationBatchModifyParam param);

    List<ApplicationMarketDTO> getRecommendApplication(ApplicationQueryParam param);

    Pagination<ApplicationPageVO> getMyApplication(ApplicationQueryParam param);
}
