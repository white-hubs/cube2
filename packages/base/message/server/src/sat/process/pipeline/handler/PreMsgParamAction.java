package com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler;

import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgTmp;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgSendParam;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.BusinessProcess;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.ProcessContext;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler.context.MsgSendContext;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgTmpService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 消息参数检验
 *
 * @author lanchun
 * @date 2023/5/8 15:12
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PreMsgParamAction implements BusinessProcess<MsgSendContext> {

    private final SatMsgTmpService satMsgTmpService;

    @Override
    public void process(ProcessContext<MsgSendContext> context) {
        SatMsgSendParam param = context.getProcessModel().getSatMsgSendParam();
        String sceneCode = param.getMsgSceneCode();
        //模板列表信息
        List<SatMsgTmp> satMsgTmpList = this.satMsgTmpService.listSatMsgTmpBySceneCodes(Collections.singleton(sceneCode));
        Assert.isNotFound(CollectionUtils.isEmpty(satMsgTmpList), "发送失败,未配置消息模板");
    }
}
