package com.guhecloud.cube.base.message2.server.handler.handler.send.app;

import com.aliyun.sdk.service.push20160801.AsyncClient;
import com.aliyun.sdk.service.push20160801.models.MassPushRequest;
import com.aliyun.sdk.service.push20160801.models.MassPushResponse;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.client.alibaba.AliYunAppPushClient;
import com.guhecloud.cube.base.message2.server.handler.domain.push.AliYunPushAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.ManufacturerSender;
import com.guhecloud.cube.base.message2.server.handler.handler.send.app.dto.AliYunPushResponseDTO;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component

public class AliYunAppSender implements ManufacturerSender {

    @Override
    public String getChannelCode() {
        return ChannelEnum.CHANNEL_APP.name();
    }

    @Override
    public String getManufacturerCode() {
        return ManufacturerCodeEnum.ALI_YUN.name();
    }

    @Override
    public MessageSendResponse send(MessageInfo messageInfo) {
        AliYunPushAccountConfig aliYunPushAccountConfig = AliYunPushAccountConfig.getAliConfig(messageInfo.getSendAccountInfo());
        MassPushRequest.PushTask pushTask = getPushTask(messageInfo, aliYunPushAccountConfig);
        try (AsyncClient client = AliYunAppPushClient.createClient(aliYunPushAccountConfig.getAccessKeyId(), aliYunPushAccountConfig.getAccessKeySecret());) {
            //安卓推送
            CompletableFuture<MassPushResponse> massAndroidPushResponseCompletableFuture = client.massPush(MassPushRequest.builder()
                                                                                                                   .appKey(aliYunPushAccountConfig.getAndroidAppKey())
                                                                                                                   .pushTask(Collections.singletonList(pushTask))
                                                                                                                   .build());
            //ios推送
            CompletableFuture<MassPushResponse> massIosPushResponseCompletableFuture = client.massPush(MassPushRequest.builder()
                                                                                                               .appKey(aliYunPushAccountConfig.getIosAppKey())
                                                                                                               .pushTask(Collections.singletonList(pushTask))
                                                                                                               .build());
            MassPushResponse massAndroidPushResponse = massAndroidPushResponseCompletableFuture.get(5, TimeUnit.SECONDS);
            MassPushResponse massIosPushResponse = massIosPushResponseCompletableFuture.get(5, TimeUnit.SECONDS);
            AliYunPushResponseDTO aliYunPushResponseDTO = AliYunPushResponseDTO.builder()
                    .androidPushResponse(massAndroidPushResponse)
                    .iosPushResponse(massIosPushResponse)
                    .build();
            return MessageSendResponse
                    .builder()
                    .id(messageInfo.getId())
                    .succeed(true)
                    .message(JsonUtils.serialize(aliYunPushResponseDTO))
                    .build();
        } catch (Exception e) {
            log.error("阿里云移动APP发送失败,任务主键:{},失败原因:{}", messageInfo.getId(), e.getMessage());
            return MessageSendResponse
                    .builder()
                    .id(messageInfo.getId())
                    .succeed(false)
                    .failReceivers(messageInfo.getReceiver().stream().map(phone -> new MessageSendResponse.Receiver(phone, e.getMessage()))
                                           .collect(Collectors.toList()))
                    .build();
        }
    }

    private MassPushRequest.PushTask getPushTask(MessageInfo messageInfo, AliYunPushAccountConfig aliYunPushAccountConfig) {
        String androidOpenType = "APPLICATION";
        String androidOpenUrl = null;
        if (StringUtils.isNotBlank(aliYunPushAccountConfig.getDeepLinkSchema())) {
            androidOpenType = "URL";
            androidOpenUrl = aliYunPushAccountConfig.getDeepLinkSchema();
        }
        return buildDefaultTask(messageInfo, androidOpenType, androidOpenUrl);

    }

    private MassPushRequest.PushTask buildDefaultTask(MessageInfo messageInfo, String androidOpenType, String androidOpenUrl) {
        HashMap<String, Object> extras = new HashMap<>(1);
        extras.put("customMsgId", messageInfo.getId());
        String extParameters = JsonUtils.serialize(extras);
        return MassPushRequest.PushTask.builder()
                //推送目标
                .deviceType("ALL")// 设备类型deviceType, iOS设备: "iOS"; Android设备: "ANDROID"; 全部: "ALL", 这是默认值
                .target("ALIAS")//推送目标: DEVICE:推送给设备; ACCOUNT:推送给指定账号,TAG:推送给自定义标签; ALIAS: 按别名推送; ALL: 全推
                .targetValue(messageInfo.getReceiver().stream().collect(Collectors.joining(",")))//别名
                //推送配置
                .pushType("MESSAGE") // MESSAGE:表示消息(默认), NOTICE:表示通知
                .title(messageInfo.getTitle())//消息的标题
                .body(messageInfo.getContent())// 消息的内容
                .trim(true)//是否自动对过长标题、内容进行截断。 注：仅支持明确限制标题、内容的厂商通道，对APNs、华为、荣耀通道等不限制标题、内容，只限制总请求体大小的不适用
                //推送配置IOS
                //                .iOSBadge(5)//iOS应用图标右上角角标
                //                .iOSBadgeAutoIncrement()//角标自增功能由推送服务端维护每个设备的角标计数，需要用户使用V1.9.5以上版本的SDK，并且需要用户主动同步角标数字到服务端。
                .iOSMusic("default")// iOS通知声音
                .iOSApnsEnv("PRODUCT")//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。'DEV': 表示开发环境 'PRODUCT': 表示生产环境
                .iOSRemind(true)//  消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。注意：**离线消息转通知仅适用于`生产环境`**
                .iOSRemindBody(messageInfo.getContent())// iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=`PRODUCT` && iOSRemind为true时有效
                .iOSExtParameters(extParameters)//通知的扩展属性(注意 : 该参数要以json map的格式传入,否则会解析出错)
                //推送配置Android
                .androidOpenType(androidOpenType)// 点击通知后动作 'APPLICATION': 打开应用 'ACTIVITY': 打开应用AndroidActivity 'URL': 打开URL 'NONE': 无跳转
                .androidNotifyType("SOUND")// 通知的提醒方式 ‘VIBRATE': 振动  'SOUND': 声音 'DEFAULT': 声音和振动 'NONE': 不做处理，用户自定义
                .androidOpenUrl(androidOpenUrl)//打开链接地址
                .androidRemind(true)//推送类型为消息时设备不在线，则这条推送会使用辅助弹窗功能。默认值为false，仅当PushType=MESSAGE时生效。
                // 如果消息转通知推送成功，收到通知是展示的数据为服务端设置的AndroidPopupTitle和AndroidPopupBody参数值，
                // 点击通知在辅助弹窗的onSysNoticeOpened方法中获取到的数据是服务端设置的Title和Body参数值
                .androidMusic("default")
                //                .sendChannels("accs,huawei,honor,vivo,xiaomi,oppo") // 指定下发的推送通道，若不填可从任何可行的通道下发
                .androidActivity("MainActivity") // Android收到推送后打开对应的ACTIVITY,仅当`AndroidOpenType="ACTIVITY"`有效
                .androidPopupActivity("MainActivity") //设置该参数后启动辅助弹窗功能, 此处指定通知点击后跳转的Activity（辅助弹窗的前提条件：1. 集成第三方辅助通道；2. StoreOffline参数设为true）
                .androidPopupTitle(messageInfo.getTitle()) //设置辅助弹窗通知的标题
                .androidPopupBody(messageInfo.getContent()) //设置辅助弹窗通知的内容
                .androidNotificationBarType(50) //Android自定义通知栏样式，取值：1-100
                .androidNotificationBarPriority(2) //Android通知在通知栏展示时排列位置的优先级 -2 -1 0 1 2
                .androidExtParameters(extParameters) //设定通知的扩展属性。(注意 : 该参数要以 json map 的格式传入,否则会解析出错)
                //推送控制
                //                .pushTime()//延后推送。可选，如果不设置表示立即推送
                .storeOffline(true)// 离线消息是否保存,若保存, 在推送时候，用户即使不在线，下一次上线则会收到
                //                .expireTime()//过期时间
                .build();
    }
}
