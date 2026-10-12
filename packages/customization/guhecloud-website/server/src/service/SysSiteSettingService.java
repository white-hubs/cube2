package com.guhecloud.cube.customized.guhecloudwebsite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSiteSetting;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteSettingParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteSettingQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSiteSettingVO;

import java.util.List;

public interface SysSiteSettingService extends IService<SysSiteSetting> {

    void update(SysSiteSettingParam param);
    SysSiteSettingVO detail();

}
