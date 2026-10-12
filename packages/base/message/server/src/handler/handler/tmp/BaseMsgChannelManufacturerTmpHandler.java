package com.guhecloud.cube.base.message2.server.handler.handler.tmp;

import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.vo.TmpVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class BaseMsgChannelManufacturerTmpHandler implements MsgChannelManufacturerTmpHandler {

    @Autowired
    private MsgChannelManufacturerTmpHandlerHolder msgChannelManufacturerTmpHandlerHolder;

    private ChannelEnum channelCode;

    private ManufacturerCodeEnum manufacturerCode;

    protected BaseMsgChannelManufacturerTmpHandler(ChannelEnum channelCode, ManufacturerCodeEnum manufacturerCode) {
        this.channelCode = channelCode;
        this.manufacturerCode = manufacturerCode;
    }

    @PostConstruct
    private void initHandler() {
        this.msgChannelManufacturerTmpHandlerHolder.putHandler(channelCode, manufacturerCode, this);
    }

    protected TmpVO getTmpVO(String tmpRule, String tmpConfig) {
        try {
            //新增时校验模板配置
            if (StringUtils.isEmpty(tmpConfig) || this.validateTmpConfig(tmpRule, tmpConfig)) {
                return TmpVO.builder()
                        .succeed(true)
                        .tmpConfig(tmpConfig)
                        .build();
            } else {
                throw new ServiceException("模板参数配置异常");
            }
        } catch (Exception e) {
            log.error("模板参数配置异常", e);
            throw new ServiceException("模板参数配置异常");
        }
    }

    protected boolean validateTmpConfig(String rule, String config) {
        try {
            if (StringUtils.isBlank(rule)) {
                return true;
            }
            //新增时校验模板配置
            ProcessingReport processingReport = JsonSchemaFactory.byDefault().getJsonSchema(JsonLoader.fromString(rule))
                    .validate(JsonLoader.fromString(config));
            return processingReport.isSuccess();
        } catch (Exception e) {
            log.error("模板参数JSON解析验证异常", e);
        }
        return false;
    }
}
