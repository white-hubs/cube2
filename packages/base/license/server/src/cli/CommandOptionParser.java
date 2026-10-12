package com.guhecloud.cube.base.license.server.cli;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.time.LocalDateTime;

@Slf4j
public class CommandOptionParser {

    public static final Options OPTIONS = generateOption();

    private static Options generateOption() {
        Options options = new Options();
        options.addOption(Option.builder("s").longOpt("subject").hasArg().desc("项目名称").build());
        options.addOption(Option.builder("pa").longOpt("private_alias").hasArg().desc("私钥别名").build());
        options.addOption(Option.builder("kp").longOpt("key_pass").hasArg().desc("密钥密码").build());
        options.addOption(Option.builder("sp").longOpt("store_pass").hasArg().desc("存储密码").build());
        options.addOption(Option.builder("lcp").longOpt("license_path").hasArgs().desc("生成证书的路径").build());
        options.addOption(Option.builder("pk").longOpt("private_keys_store_path").hasArg().desc("私钥文件路径").build());
        options.addOption(Option.builder("it").longOpt("issued_time").hasArg().desc("证书生效时间(yyyy-MM-dd HH:mm:ss) 格式").build());
        options.addOption(Option.builder("et").longOpt("expiry_time").hasArg().desc("证书失效时间(yyyy-MM-dd HH:mm:ss) 格式").build());
        return options;
    }

    public static CommandOption parseFromCommandLine(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(OPTIONS, args);
        CommandOption configFileOption = new CommandOption();
        regulateOptionWithCommand(configFileOption, line);
        return configFileOption;
    }

    private static String getOptionString(Config option, String key, String defaultValue) {
        try {
            return option.getString(key);
        } catch (ConfigException.Missing missing) {
            return defaultValue;
        }
    }

    private static LocalDateTime getOptionDate(Config option, String key, LocalDateTime defaultValue) {
        try {
            String dateStr = option.getString(key);
            return DateUtils.parse(dateStr);
        } catch (ConfigException.Missing missing) {
            return defaultValue;
        }
    }

    public static CommandOption parseHoconFile(String filePath) {
        File configFile = new File(filePath);
        Config config = ConfigFactory.parseFile(configFile);
        CommandOption option = new CommandOption();
        option.subject = getOptionString(config, "subject", null);
        option.privateAlias = getOptionString(config, "privateAlias", null);
        option.keyPass = getOptionString(config, "keyPass", null);
        option.storePass = getOptionString(config, "storePass", null);
        option.licensePath = getOptionString(config, "licensePath", null);
        option.privateKeysStorePath = getOptionString(config, "privateKeysStorePath", null);
        option.issuedTime = getOptionDate(config, "issuedTime", null);
        option.expiryTime = getOptionDate(config, "expiryTime", null);
        return option;
    }

    /**
     * 命令行参数优先于配置文件，本方法处理相关逻辑
     */
    private static void regulateOptionWithCommand(CommandOption option, CommandLine line) throws ParseException {
        option.subject = ObjectUtils.getOrElse(StringUtils::isNotEmpty, line.getOptionValue("s"), option.subject);
        option.privateAlias = ObjectUtils.getOrElse(StringUtils::isNotEmpty, line.getOptionValue("pa"), option.privateAlias);
        option.keyPass = ObjectUtils.getOrElse(StringUtils::isNotEmpty, line.getOptionValue("kp"), option.keyPass);
        option.storePass = ObjectUtils.getOrElse(StringUtils::isNotEmpty, line.getOptionValue("sp"), option.storePass);
        option.licensePath = ObjectUtils.getOrElse(StringUtils::isNotEmpty, line.getOptionValue("lcp"), option.licensePath);
        option.privateKeysStorePath = ObjectUtils.getOrElse(StringUtils::isNotEmpty, line.getOptionValue("pk"), option.privateKeysStorePath);
        String issuedTimeStr = ObjectUtils.getOrElse(StringUtils::isNotEmpty, line.getOptionValue("it"), null);
        if (StringUtils.isNotEmpty(issuedTimeStr)) {
            option.issuedTime = DateUtils.parse(issuedTimeStr);
        }
        String expiryTimeStr = ObjectUtils.getOrElse(StringUtils::isNotEmpty, line.getOptionValue("et"), null);
        if (StringUtils.isNotEmpty(expiryTimeStr)) {
            option.expiryTime = DateUtils.parse(expiryTimeStr);
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class CommandOption {

        private String subject;
        private String privateAlias;
        private String keyPass;

        private String storePass;
        private String licensePath;
        private String privateKeysStorePath;
        private LocalDateTime issuedTime;
        private LocalDateTime expiryTime;
    }
}
