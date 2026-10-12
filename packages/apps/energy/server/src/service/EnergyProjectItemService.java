package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProjectItem;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyProjectItemParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyProjectItemQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectItemVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ProjectItemVo;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：分项及区域仪表绑定服务层
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
public interface EnergyProjectItemService extends IService<EnergyProjectItem> {

    Long add(EnergyProjectItemParam param);

    void update(Long id, EnergyProjectItemParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    EnergyProjectItemVO getById(Long id);

    Pagination<EnergyProjectItemVO> getPage(EnergyProjectItemQueryParam param);

    List<ProjectItemVo> getProjectItems();

    List<EnergyProjectItem> getEnergyProjectItem(String bindType, String meterTable);

}