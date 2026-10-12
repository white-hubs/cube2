package com.guhecloud.cube.base.message2.server.handler.handler.send.email;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.domain.email.SmtpAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.ManufacturerSender;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SmtpEmailSender implements ManufacturerSender {

    @Override
    public String getChannelCode() {
        return ChannelEnum.CHANNEL_EMAIL.name();
    }

    @Override
    public String getManufacturerCode() {
        return ManufacturerCodeEnum.SMTP.name();
    }

    @Override
    public MessageSendResponse send(MessageInfo messageInfo) {
        try {
            log.info("smtp message send:{}", messageInfo);
            SmtpAccountConfig smtpAccountConfig = SmtpAccountConfig.getSmtpAccountConfig(messageInfo.getSendAccountInfo());
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true"); // 开启TLS
            props.put("mail.smtp.host", smtpAccountConfig.getSmtpHost()); // 邮箱的SMTP服务器地址
            props.put("mail.smtp.port", smtpAccountConfig.getSmtpPort()); // 邮箱的SMTP服务器端口号

            //发送邮件阻塞问题解决设置 smtp 超时时间
            props.put("mail.smtp.connectiontimeout", "5000");// 设置接收超时时间
            props.put("mail.smtp.timeout", "5000");// 设置读取超时时间
            props.put("mail.smtp.writetimeout", "5000");// 设置写入超时时间
            // 认证信息
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpAccountConfig.getSendEmail(), smtpAccountConfig.getSendEmailPassword());
                }
            });
            log.info("smtp message send session:{}", session);
            // 将Set转换为InternetAddress数组
            InternetAddress[] addresses = new InternetAddress[messageInfo.getReceiver().size()];
            int i = 0;
            for (String recipient : messageInfo.getReceiver()) {
                addresses[i++] = new InternetAddress(recipient);
            }
            log.info("smtp message send address:{}", new InternetAddress(smtpAccountConfig.getSendEmail()));
            log.info("smtp message recipients address:{}", (Object) addresses);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpAccountConfig.getSendEmail())); // 发件人
            message.setRecipients(Message.RecipientType.TO, addresses); // 收件人
            message.setSubject(messageInfo.getTitle()); // 主题
            message.setText(messageInfo.getContent()); // 内容
            log.info("smtp message send body:{}", message);
            Transport.send(message); // 发送邮件
            log.info("smtp message send success");
            return MessageSendResponse
                    .builder()
                    .id(messageInfo.getId())
                    .succeed(true)
                    .build();
        } catch (Exception e) {
            log.error("smtp邮件发送失败,任务主键:{},失败原因:{}", messageInfo.getId(), e.getMessage());
            return MessageSendResponse
                    .builder()
                    .id(messageInfo.getId())
                    .succeed(false)
                    .failReceivers(messageInfo.getReceiver().stream().map(phone -> new MessageSendResponse.Receiver(phone, e.getMessage()))
                                           .collect(Collectors.toList()))
                    .build();
        }
    }
}
