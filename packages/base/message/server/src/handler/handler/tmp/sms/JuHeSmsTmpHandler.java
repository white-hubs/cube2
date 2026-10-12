package com.guhecloud.cube.base.message2.server.handler.handler.tmp.sms;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.ChangeMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.DeleteMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.IncreaseMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.vo.TmpVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JuHeSmsTmpHandler extends BaseSmsTmpHandler {

    protected JuHeSmsTmpHandler() {
        super(ChannelEnum.CHANNEL_SMS, ManufacturerCodeEnum.JU_HE_DATA);
    }

    @Override
    public TmpVO increaseTmp(IncreaseMessageTmp increaseMessageTmp) {
        return this.getTmpVO(increaseMessageTmp.getTmpRule(), increaseMessageTmp.getTmpConfig());
    }

    @Override
    public TmpVO changeTmp(ChangeMessageTmp changeMessageTmp) {
        this.removeTmp(DeleteMessageTmp.builder()
                               .accountInfo(changeMessageTmp.getAccountInfo())
                               .tmpConfig(changeMessageTmp.getTmpConfig())
                               .build());
        return this.increaseTmp(IncreaseMessageTmp.builder()
                                        .tmpName(changeMessageTmp.getTmpName())
                                        .content(changeMessageTmp.getContent())
                                        .accountInfo(changeMessageTmp.getAccountInfo())
                                        .build());
    }

    @Override
    public void removeTmp(DeleteMessageTmp deleteMessageTmp) {
        //do nothing
    }

    @Override
    protected String convertTemplateContent(String content) {
        return content;
    }
}