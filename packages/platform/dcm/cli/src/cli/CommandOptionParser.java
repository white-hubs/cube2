package com.guhecloud.platform.dcm.cli;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
import com.guhecloud.platform.base.util.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CommandOptionParser {
    private static final String DEFAULT_NACOS_USER = "nacos";
    private static final String DEFAULT_NACOS_PASSWD = "nacos";
    private static final String DEFAULT_NACOS_NAMESPACE = "";

    // sbtfat: path or jar with spring boot fat jar structure,
    // libpath: search all jars in the path for contents
    // basedir: search contents from base dir
    public static final String FM_SBT_FAT = "SBTFAT";
    public static final String FM_LIB_PATH = "LIBPATH";
    public static final String FM_BASE_DIR = "BASEDIR";
    private static final String DEFAULT_FILE_MODE = FM_BASE_DIR;
    public static final Options OPTIONS = generateOption();

    private static Options generateOption() {
        Options options = new Options();
        options.addOption(Option.builder("k").longOpt("use_auth").desc("使用ouaa auth,需要另外指定auth token url和相关配置").build());
        options.addOption(Option.builder("f").longOpt("file").hasArg().desc("单资源文模式的文件名称").build());
        options.addOption(Option.builder("fp").longOpt("file_path").hasArg().desc("多资源模下的资源文件目录或jar路径").build());
        options.addOption(Option.builder("fm").longOpt("file_mode").hasArg().desc("指定jar包/目录样式").build());
        options.addOption(Option.builder("s").longOpt("services").valueSeparator(',').hasArgs().desc("逗号分割的服务"
                                                                                                     + "(provider)列表").build());
        options.addOption(Option.builder("a").longOpt("all_builtin_services").desc("使用(预设的)所有服务").build());
        options.addOption(Option.builder("t").longOpt("target_server_url").hasArg().desc("指定目标服务器基地址").build());
        options.addOption(Option.builder("o").longOpt("output_path").hasArg().desc("用于提取资源的输出目录").build());
        options.addOption(Option.builder("m").longOpt("map").hasArg().desc("用于修正服务访问地址的ServiceMap文件").build());
        options.addOption(Option.builder("c").longOpt("config").hasArg().desc("配置文件").build());
        options.addOption(Option.builder("r").longOpt("resource_pattern").hasArg().desc("抽取资源时的资源路径正则表达模式").build());
        return options;
    }

    public static CommandOption parseFromCommandLine(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(OPTIONS, args);

        CommandOption configFileOption;
        if (StringUtils.isNotEmpty(line.getOptionValue("c"))) {
            // parse configFile as hocon, and using parsed key/values to update retOpt
            try {
                configFileOption = parseHoconFile(line.getOptionValue("c"));
            } catch (RuntimeException configException) {
                log.warn("config file parse error, use cli options only");
                configFileOption = new CommandOption();
            }
        } else {
            configFileOption = new CommandOption();
        }
        regulateOptionWithCommand(configFileOption, line);
        configFileOption.setCommands(line.getArgList());
        return configFileOption;
    }

    private static String getOptionString(Config option, String key, String defaultValue) {
        try {
            return option.getString(key);
        } catch (ConfigException.Missing missing) {
            return defaultValue;
        }
    }

    private static List<String> getOptionStringList(Config option, String key, List<String> defaultValue) {
        try {
            return option.getStringList(key);
        } catch (ConfigException.Missing missing) {
            return defaultValue;
        }
    }

    private static boolean getOptionBoolean(Config option, String key, boolean defaultValue) {
        try {
            return option.getBoolean(key);
        } catch (ConfigException.Missing missing) {
            return defaultValue;
        }
    }

    public static CommandOption parseHoconFile(String filePath) {
        // Load the HOCON configuration file
        File configFile = new File(filePath);
        Config config = ConfigFactory.parseFile(configFile);
        // Create a new CommandOption instance
        CommandOption option = new CommandOption();
        // Populate the CommandOption fields using the configuration values
        option.file = getOptionString(config, "file", null);
        option.filePath = getOptionString(config, "filePath", null);
        option.fileMode = getOptionString(config, "fileMode", null);
        option.servicesList = getOptionStringList(config, "services", null);
        option.allBuiltinService = getOptionBoolean(config, "allBuiltinServices", false);
        option.useAuth = getOptionBoolean(config, "useAuth", false);
        option.outputPath = getOptionString(config, "outputPath", null);
        option.targetServerUrl = getOptionString(config, "targetServerUrl", null);
        option.resourcePattern = getOptionString(config, "resourcePattern", null);
        option.mapFile = getOptionString(config, "mapFile", null);
        return option;
    }

    /**
     * 命令行参数优先于配置文件，本方法处理相关逻辑
     */
    private static void regulateOptionWithCommand(CommandOption option, CommandLine line) throws ParseException {

        option.file = StringUtils.defaultIfEmpty(line.getOptionValue("f"), option.file);
        // Override file options if filePath is provided
        String fpath = StringUtils.defaultIfEmpty(line.getOptionValue("fp"), option.filePath);
        if (StringUtils.isNotEmpty(fpath)) {
            if (fpath.toLowerCase().endsWith(".jar")) {
                // jar file 仅支持spring boot fat jar
                option.fileMode = FM_SBT_FAT;
            } else {
                String fmode = StringUtils.defaultIfEmpty(line.getOptionValue("fm"), option.fileMode);
                option.fileMode = StringUtils.defaultIfEmpty(fmode, DEFAULT_FILE_MODE).trim().toUpperCase();
            }
        }
        option.filePath = fpath;
        option.useAuth = line.hasOption("k") || option.useAuth;

        // Override servicesList and allServices options
        boolean allServices = line.hasOption("a");
        if (allServices) {
            option.allBuiltinService = true;
        }
        if (!option.allBuiltinService) {
            List<String> servicesList = line.hasOption("s") ? Arrays.asList(line.getOptionValues("s")) :
                                        Collections.emptyList();
            if (!servicesList.isEmpty()) {
                option.servicesList = servicesList;
            }
        }
        option.mapFile = StringUtils.defaultString(StringUtils.defaultIfBlank(line.getOptionValue("m"),
                                                                              option.mapFile));
        option.targetServerUrl = StringUtils.defaultIfEmpty(line.getOptionValue("t"), option.targetServerUrl);
        option.outputPath = StringUtils.defaultIfEmpty(
                StringUtils.defaultIfEmpty(line.getOptionValue("o"), option.outputPath), ".");
        option.resourcePattern = StringUtils.defaultIfEmpty(line.getOptionValue("r"), option.resourcePattern);
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class CommandOption {
        // 暂时没有使用
        // private String nacosServerUrl;
        // private String nacosUser;
        // private String nacosPassword;
        // private String nacosNamespace;

        private String file;  //
        private String filePath;
        private String fileMode;

        private List<String> servicesList;
        private boolean allBuiltinService;
        private String targetServerUrl;
        private String outputPath;
        private String configFile;
        private String mapFile;
        private String resourcePattern;

        private boolean useAuth;

        private List<String> commands;

        // 懒加载的service mapping.
        @JsonIgnore
        private Map<String, String> serviceMapping;

        @JsonIgnore
        private boolean initialized;

        public synchronized Map<String, String> fetchServiceMapping() {
            if (!initialized) {
                if (StringUtils.isBlank(mapFile)) {
                    serviceMapping = Collections.emptyMap();
                } else {
                    try {
                        File configFile = new File(mapFile);
                        Config config = ConfigFactory.parseFile(configFile);
                        serviceMapping = config.entrySet().stream().collect(
                                Collectors.toMap(entry -> entry.getKey().trim().toUpperCase(),
                                                 entry -> String.valueOf(entry.getValue().unwrapped().toString()))
                                                                           );
                    } catch (ConfigException re) {
                        log.info("Parse service mapping failed: {}", re.getMessage());
                        serviceMapping = Collections.emptyMap();
                    }
                }
                initialized = true;
            }
            return serviceMapping;
        }
    }
}
