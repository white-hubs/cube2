package com.guhecloud.cube.base.message2.server.handler.handler.tmp.site;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.BaseMsgChannelManufacturerTmpHandler;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.site.dto.SiteTerminal;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.ChangeMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.DeleteMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.IncreaseMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.vo.TmpVO;
import com.guhecloud.cube.biz.ouaa.client.TerminalClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TerminalDTO;
import com.guhecloud.cube.biz.ouaa.client.param.TerminalQueryParam;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.model.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.stream.Collectors;

@Slf4j
@Component
public class SiteTmpHandler extends BaseMsgChannelManufacturerTmpHandler {

    private final TerminalClient terminalClient;

    protected SiteTmpHandler(TerminalClient terminalClient) {
        super(ChannelEnum.CHANNEL_SITE, ManufacturerCodeEnum.GUHE_YUN);
        this.terminalClient = terminalClient;
    }

    @Override
    public TmpVO increaseTmp(IncreaseMessageTmp increaseMessageTmp) {
        TerminalQueryParam terminalQueryParam = new TerminalQueryParam();
        terminalQueryParam.setPageNumber(1);
        terminalQueryParam.setPageSize(1000);
        Pagination<TerminalDTO> terminalPage = terminalClient.listByPage(terminalQueryParam);
        if (!CollectionUtils.isEmpty(terminalPage.getRecords())) {
            SiteTerminal siteTerminal = new SiteTerminal();
            siteTerminal.setTerminals(terminalPage.getRecords().stream().map(TerminalDTO::getCode)
                                              .collect(Collectors.toList()));
            return getTmpVO(increaseMessageTmp.getTmpRule(), JsonUtils.serialize(siteTerminal));
        }
        return getTmpVO(increaseMessageTmp.getTmpRule(), increaseMessageTmp.getTmpConfig());
    }

    @Override
    public TmpVO changeTmp(ChangeMessageTmp changeMessageTmp) {
        return getTmpVO(changeMessageTmp.getTmpRule(), changeMessageTmp.getTmpConfig());
    }

    @Override
    public void removeTmp(DeleteMessageTmp deleteMessageTmp) {
        //暂不实现
        throw new UnsupportedOperationException();
    }
}
