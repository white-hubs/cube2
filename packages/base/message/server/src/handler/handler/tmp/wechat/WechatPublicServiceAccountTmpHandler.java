package com.guhecloud.cube.base.message2.server.handler.handler.tmp.wechat;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.BaseMsgChannelManufacturerTmpHandler;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.ChangeMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.DeleteMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.IncreaseMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.vo.TmpVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WechatPublicServiceAccountTmpHandler extends BaseMsgChannelManufacturerTmpHandler {

    private final RedisTemplate<String, String> redisTemplate;

    protected WechatPublicServiceAccountTmpHandler(RedisTemplate<String, String> redisTemplate) {
        super(ChannelEnum.CHANNEL_WE_PUBLIC_SERVICE_ACCOUNT, ManufacturerCodeEnum.WE_CHAT);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public TmpVO increaseTmp(IncreaseMessageTmp increaseMessageTmp) {
        return this.getTmpVO(increaseMessageTmp.getTmpRule(), increaseMessageTmp.getTmpConfig());
    }

    @Override
    public TmpVO changeTmp(ChangeMessageTmp changeMessageTmp) {
        this.redisTemplate.delete("wechatPublicServiceAccountAccessToken:" + SessionContextHolder.getRequester().getTenant());
        return this.getTmpVO(changeMessageTmp.getTmpRule(), changeMessageTmp.getTmpConfig());
    }

    @Override
    public void removeTmp(DeleteMessageTmp deleteMessageTmp) {
        //do nothing
    }
}
