package com.guhecloud.platform.dcm.command;

import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.dcm.auth.OuaaTokenStorage;
import com.guhecloud.platform.dcm.cli.CommandOptionParser;
import com.guhecloud.platform.dcm.cli.CommandOptionParser.CommandOption;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.cloud.openfeign.FeignClientBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public abstract class AbstractCliCommand implements ICliCommand {
    public static final String DEFAULT_RESOURCE_PATTERN = "^/dcm/dcm-[^/]*.ya?ml";

    private final String command;
    private final CommandOption option;

    private static FeignClientBuilder fcb;
    private static List<String> defaultProviders;

    private static final Object lock = new Object();

    protected AbstractCliCommand(String command, CommandOption option) {
        this.command = command;
        this.option = option;
    }

    protected static boolean isValidTenant(String x) {
        try {
            Long.parseUnsignedLong(x);
            return true;
        } catch (RuntimeException ign) {
            return false;
        }
    }

    protected String convertProvider2HostNameCompatiable(String name) {
        Objects.requireNonNull(name, "provider cannot be null");
        return name.replace("_", "-");
    }

    protected List<String> getDefaultProviders() {
        return defaultProviders == null ? Collections.emptyList() : defaultProviders;
    }

    protected List<String> getProviders() {
        return getOption().isAllBuiltinService() ?
               getDefaultProviders() : getOption().getServicesList();
    }

    public static ICliCommand createCommand(String command, CommandOption option) {
        synchronized (lock) {
            if (fcb == null) {
                fcb = new FeignClientBuilder(ApplicationContextHolder.getApplicationContext());
            }
            if (defaultProviders == null) {
                defaultProviders = ApplicationContextHolder.getApplicationContext()
                        .getBean("defaultProviders", List.class);
            }
        }
        if ("local_extract".equalsIgnoreCase(command)) {
            return new LocalExtractResourceCommand(option);
        } else if ("apply".equalsIgnoreCase(command)) {
            return new ApplyCommand(option, fcb);
        } else if ("extract".equalsIgnoreCase(command)) {
            return new ExtractCommand(option, fcb);
        } else if ("rt_extract".equalsIgnoreCase(command)) {
            return new RtExtractCommand(option, fcb);
        } else if ("diff".equalsIgnoreCase(command)) {
            return new DiffCommand(option, fcb);
        }
        return null;
    }

    public static boolean waitingForOuaaToken(long waitMillis) {
        log.debug("begin waiting for ouaa token valid");
        OuaaTokenStorage runner = ApplicationContextHolder.getBean(OuaaTokenStorage.class);
        String token = runner.waitToken(waitMillis);
        if (token == null || token.isEmpty()) {
            log.warn("token invalid after waiting {} millis", waitMillis);
            return false;
        } else {
            log.info("token got from ouaa");
            return true;
        }
    }

    protected CommandOption getOption() {
        return option;
    }

    private boolean isValidUrl(String url) {
        return url != null && (url.startsWith("http://") || url.startsWith("https://"));
    }

    protected String getTargetUrl(String provider) {
        // 目前的简化规则是： targetUrl + '/' + provider + 作为基地址 如果换url 构建风格需要更换client.
        String targetServerUrl = getOption().getTargetServerUrl();
        String targetBaseUrl = targetServerUrl == null ? "" :
                               targetServerUrl.endsWith("/") ? targetServerUrl : targetServerUrl + "/";
        Map<String, String> serviceMapping = getOption().fetchServiceMapping();
        String providerKey = provider.trim().toUpperCase();
        String convertedProviderParts = serviceMapping.getOrDefault(providerKey, providerKey.toLowerCase());
        if (isValidUrl(convertedProviderParts)) {
            return convertedProviderParts;
        } else {
            String url = targetBaseUrl + convertedProviderParts;
            if (!isValidUrl(url)) {
                throw new IllegalArgumentException("no valid target url for provider " + provider
                                                   + ", please set correct target server url or define service mapping");
            }
            return url;
        }
    }

    protected void dumpResponse(Response response, String outFileName) {
        try {
            Path targetFile = Paths.get(outFileName);
            Path targetDir = targetFile.getParent();
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }
            Files.copy(response.body().asInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
            log.info("response saved to: {}", targetFile);
        } catch (IOException ioe) {
            log.warn("copy contents error: {}", ioe.getMessage());
        }
    }

    protected String buildOutFileName(CommandOptionParser.CommandOption option, String prefix, boolean isSystem, String tenant,
                                      Response response
                                     ) {
        Collection<String> dispositionHeaders = response.headers().getOrDefault("Content-Disposition", Collections.emptyList());
        String extractedFileName = null;
        for (String dispositionHeader : dispositionHeaders) {
            if ((extractedFileName = extractFilenameFromContentDisposition(dispositionHeader)) != null) {
                break;
            }
        }
        if (extractedFileName == null) {
            extractedFileName = prefix + (isSystem ? "-system" : "-tenant" + "-" + tenant) + "-" + System.currentTimeMillis() + ".yaml";
        }
        String basePath = StringUtils.isNotEmpty(option.getOutputPath()) ? option.getOutputPath() : ".";
        if (!basePath.endsWith("/")) {
            basePath += "/";
        }
        return basePath + extractedFileName;
    }

    protected static String extractFilenameFromContentDisposition(String contentDisposition) {
        String filename = null;
        Pattern pattern = Pattern.compile("filename=\"(.+?)\"");
        Matcher matcher = pattern.matcher(contentDisposition);

        if (matcher.find()) {
            filename = matcher.group(1);
        }
        return filename;
    }

}
