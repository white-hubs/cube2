package com.guhecloud.platform.dcm.command;

import com.guhecloud.platform.dcm.cli.CommandOptionParser;
import com.guhecloud.platform.dcm.client.DcmConfigFeignClient;
import com.guhecloud.platform.dcm.param.DcmDeclarationParam;
import com.guhecloud.platform.dcm.param.DcmRequirementParam;
import com.guhecloud.platform.dcm.support.BaseDirectResourceExtractor;
import com.guhecloud.platform.dcm.support.LibsModeJarResourceExtractor;
import com.guhecloud.platform.dcm.support.ResourceExtractor;
import com.guhecloud.platform.dcm.support.SpringBootFatJarResourceExtractor;
import com.guhecloud.platform.dcm.utils.DcmTools;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
abstract class BaseResourceIteratorRemoteCliCommand extends AbstractCliCommand {
    protected final FeignClientBuilder fcb;

    public BaseResourceIteratorRemoteCliCommand(String cmd, CommandOptionParser.CommandOption option, FeignClientBuilder fcb) {
        super(cmd, option);
        this.fcb = fcb;
    }

    @Override
    public void execute() {
        // fetch and parse service map
        try {
            CommandOptionParser.CommandOption option = getOption();
            Map<Resource, String> resources;
            if (StringUtils.isNotBlank(option.getFile())) {
                resources = Collections.singletonMap(new FileSystemResource(option.getFile()), option.getFile());
            } else {
                ResourceExtractor re = getResourceExtractor();
                String resourcePattern = StringUtils.isBlank(option.getResourcePattern()) ?
                                         DEFAULT_RESOURCE_PATTERN : option.getResourcePattern();
                resources = re.extractResource(resourcePattern, true);
                BaseResourceIteratorRemoteCliCommand.log.info("{} resources got.", resources.size());
            }
            // wait for ouaa token
            if (!getOption().isUseAuth() || waitingForOuaaToken(10000))
            // 逐一提取resource, 解读目标requirements
            {
                for (Map.Entry<Resource, String> resourceEntry : resources.entrySet()) {
                    Resource resource = resourceEntry.getKey();
                    String resourceNote = resourceEntry.getValue();
                    BaseResourceIteratorRemoteCliCommand.log.info(">>>> processing {}[{}]", resourceNote, resource.getURI());

                    DcmDeclarationParam declarationParam = DcmTools.Parser.declarationParamFromYamlContent(resource.getInputStream());
                    Set<String> allowedProviders = getProviders().stream().map(String::toUpperCase).collect(Collectors.toSet());
                    Set<String> providers = declarationParam.getRequests().stream().map(DcmRequirementParam::getProvider)
                            .collect(Collectors.toCollection(LinkedHashSet::new));

                    boolean hasSystem = getOption().getCommands().contains("system");
                    List<String> tenants = getOption().getCommands().stream()
                            .filter(ApplyCommand::isValidTenant)
                            .collect(Collectors.toList());
                    for (String provider : providers) {
                        if (!allowedProviders.contains(provider.toUpperCase())) {
                            log.info("requested provider {} not allowed.", provider);
                            continue;
                        }
                        // 逐一向指定目标发送请求
                        String targetUrl = getTargetUrl(provider);
                        log.info("process {} on {}, url: {}", resourceNote, provider, targetUrl);
                        DcmConfigFeignClient client = fcb
                                .forType(DcmConfigFeignClient.class,
                                         "dcm-config-" + convertProvider2HostNameCompatiable(provider.toLowerCase()))
                                .url(targetUrl)
                                .build();
                        boolean success = execute(client, declarationParam, hasSystem, tenants, provider, resourceNote);
                        log.info(">>>> processing {}: resource: {} provider: {}", success ? "success" : "failed", resourceNote, provider);
                    }
                }
            }
        } catch (IOException e) {
            log.error("extract resource failed: {}", e.toString());
        }
    }

    private ResourceExtractor getResourceExtractor() {
        ResourceExtractor re;
        String fileMode = getOption().getFileMode();
        String filePath = getOption().getFilePath();

        if (CommandOptionParser.FM_BASE_DIR.equalsIgnoreCase(fileMode)) {
            re = new BaseDirectResourceExtractor(filePath);
        } else if (CommandOptionParser.FM_SBT_FAT.equalsIgnoreCase(fileMode)) {
            re = new SpringBootFatJarResourceExtractor(filePath, getOption().getOutputPath());
        } else {
            re = new LibsModeJarResourceExtractor(filePath, getOption().getOutputPath());
        }
        return re;
    }

    /**
     * Execute on Remote Target.
     *
     * @param client           到请求对象端点的client
     * @param declarationParam 发往被请求对象的申明内容
     * @param system           是否执行系统请求
     * @param tenants          待执行的租户列表
     * @param provider         被请求对象的provider标识。作为一个冗余信息，方便实现体进行注记
     * @param resourceNote     资源文件的一个标记，用于实现体在日志或操作记录中加以注记
     *
     * @return 是否完全成功 （如果某些处理包含错误，将返回false）
     */
    protected abstract boolean execute(DcmConfigFeignClient client, DcmDeclarationParam declarationParam,
                                       boolean system, List<String> tenants, String provider, String resourceNote
                                      );
}
