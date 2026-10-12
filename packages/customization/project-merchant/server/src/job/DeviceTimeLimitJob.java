//package com.guhecloud.cube.deliver.project.merchant.job;
//
//
//import com.guhecloud.cube.deliver.project.merchant.dto.DeviceNoticeConfigDTO;
//import com.guhecloud.cube.deliver.project.merchant.dto.DeviceNoticeDTO;
//import com.guhecloud.cube.deliver.project.merchant.dto.DeviceNoticeSupDTO;
//import com.guhecloud.cube.deliver.project.merchant.entity.Device;
//import com.xxl.job.core.biz.model.ReturnT;
//import com.xxl.job.core.handler.annotation.XxlJob;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
//public class DeviceTimeLimitJob {
//
//    private final DeviceService deviceService;
//    private final DeviceNoticeService noticeService;
//    private final SiteMsgApi siteMsgApi;
//    private final WeChatMsgApi weChatMsgApi;
//    private final SmsApi smsApi;
//
////    private final static String DEVICE_LIMIT_TYPE = "device-limit";
//
//    @XxlJob("sendDeviceTimeLimitMsg")
//    public ReturnT<String> sendDeviceTimeLimitMsg(String param) throws Exception {
//        DeviceNoticeSupDTO noticeDTO = noticeService.getList(param, DeviceNoticeService.BUSINESS_TYPE);
//        // 查询通知配置
//        List<DeviceNoticeDTO> noticeDTOS = noticeDTO.getNoticeDTOS();
//        if (noticeDTOS == null || noticeDTOS.size() < 1) return ReturnT.SUCCESS;
//        noticeDTOS.forEach(notice -> {
//            // 还有多少天到期需要通知的
//            Integer days = notice.getDays();
//            // 查询对应的设备名称，逗号分隔
//            String deviceNames = deviceService.lambdaQuery().eq(Device::getProjectId, noticeDTO.getProjectId()).eq(Device::getTimeLimit, days).list().stream().map(Device::getName).collect(Collectors.joining("，"));
//            // 得到需要通知的人员IDS
//            List<DeviceNoticeConfigDTO> noticeConfigDTOS = notice.getNoticeConfigDTOS();
//            if (StringUtils.isNotEmpty(deviceNames) && noticeConfigDTOS != null && noticeConfigDTOS.size() > 0) {
//                Set<String> noticeUserIds = new HashSet<>();
//                noticeConfigDTOS.stream().forEach(config -> noticeUserIds.addAll(config.getNoticeUserIds()));
//                this.sendNotice(notice.getProjectId(), days, deviceNames + "等", noticeUserIds, notice.isSite(), notice.isSms(), notice.isWx());
//            }
//        });
//        return ReturnT.SUCCESS;
//    }
//
//    @XxlJob("updateDeviceTimeLimit")
//    public ReturnT<String> updateDeviceTimeLimit(String param) throws Exception {
//        List<Device> devices = deviceService.list();
//        devices.forEach(device -> {
//            if (device.getExpireDate() != null) {
//                deviceService.lambdaUpdate().eq(Device::getId, device.getId()).set(Device::getTimeLimit, DateUtils.getIntervalByDays(LocalDateTime.now(), device.getExpireDate())).update();
//            }
//        });
//        return ReturnT.SUCCESS;
//    }
//
//    private void sendNotice(String projectId, Integer days, String deviceNames, Set<String> noticeUserIds, boolean site, boolean sms, boolean wx) {
//        if (noticeUserIds.size() < 1) return;
//        // 通知内容
//        String content = "";
//        String smsTemplateId = "";
//        if (days < 0) {
//            content = "设备已经过期" + (days * -1) + "天";
//            smsTemplateId = JSmsTemplateEnum.T_DEVICE_LIMIT_3.getSmsTempId();
//        } else if (days == 0) {
//            content = "设备今天到期";
//            smsTemplateId = JSmsTemplateEnum.T_DEVICE_LIMIT_2.getSmsTempId();
//        } else {
//            content = "设备距离到期还有" + days + "天";
//            smsTemplateId = JSmsTemplateEnum.T_DEVICE_LIMIT_1.getSmsTempId();
//        }
//        // 根据通知方式通知
//        if (site) {
//            this.sendSiteMsg(projectId, noticeUserIds, deviceNames, content);
//        }
//        if (sms) {
//            this.sendSmsMsg(projectId, noticeUserIds, deviceNames, days, smsTemplateId);
//        }
//        if (wx) {
//            this.sendWeChatMsg(projectId, noticeUserIds, deviceNames, content);
//        }
//    }
//
//    private void sendWeChatMsg(String projectId, Set<String> noticeUserIds, String deviceNames, String content) {
//        try {
//            weChatMsgApi.send(projectId,
//                    DeviceNoticeService.BUSINESS_TYPE,
//                    new ArrayList<>(noticeUserIds),
//                    "你有" + deviceNames + content + ",请及时处理",
//                    true
//                    );
//        } catch (Exception e) {
//            log.error("设备到期通知，微信消息发送失败");
//        }
//    }
//
//    private void sendSiteMsg(String projectId, Set<String> noticeUserIds, String deviceNames, String content) {
//        try {
//            siteMsgApi.send(projectId,
//                    DeviceNoticeService.BUSINESS_TYPE,
//                    "",
//                    new ArrayList<>(noticeUserIds),
//                    "设备到期通知",
//                    "你有" + deviceNames + content + ",请及时处理",
//                    7 * 24,
//                    true
//                    );
//        } catch (Exception e) {
//            log.error("设备到期通知，站内消息发送失败");
//        }
//    }
//
//    private void sendSmsMsg(String projectId, Set<String> noticeUserIds, String deviceNames, Integer days, String templateId) {
//        try {
//            if (days != 0) {
//                smsApi.send(projectId,
//                        DeviceNoticeService.BUSINESS_TYPE,
//                        true,
//                        "",
//                        new ArrayList<>(noticeUserIds),
//                        templateId,
//                        deviceNames,
//                        days < 0 ? String.valueOf(days * -1) : String.valueOf(days)
//                );
//            } else {
//                smsApi.send(projectId,
//                        DeviceNoticeService.BUSINESS_TYPE,
//                        true,
//                        "",
//                        new ArrayList<>(noticeUserIds),
//                        templateId,
//                        deviceNames
//                        );
//            }
//        } catch (Exception e) {
//            log.error("设备到期通知，短信消息发送失败");
//        }
//    }
//
//
//}
