package com.guhecloud.cube.base.message2.server.sat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgTmp;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgTmpModifyParam;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgTmpQueryParam;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgTmpSettingConfigParam;
import com.guhecloud.cube.base.message2.server.sat.vo.SatMsgTmpPageVO;

import java.util.Collection;
import java.util.List;

public interface SatMsgTmpService extends IService<SatMsgTmp> {

    Long save(SatMsgTmpModifyParam param);

    void update(Long id, SatMsgTmpModifyParam param);

    void settingConfig(Long id, SatMsgTmpSettingConfigParam param);

    void refreshTemplate();

    List<SatMsgTmp> listSatMsgTmpBySceneCodes(Collection<String> sceneCodes);

    List<SatMsgTmp> listSatMsgTmpByApplicationCodes(Collection<String> applicationCodes);

    List<SatMsgTmp> listSatMsgTmp(String sceneCode, Collection<ChannelEnum> channelCodes);

    List<SatMsgTmp> listSatMsgTmp(ChannelEnum channelCode);

    Page<SatMsgTmpPageVO> getSatMsgTmpPage(SatMsgTmpQueryParam param);

    List<SatMsgTmpPageVO> getSatMsgTemplates(SatMsgTmpQueryParam param);

    void remove(Long id);

    void refreshAppTemplate(Collection<String> appCodes, Integer tenant);
}
