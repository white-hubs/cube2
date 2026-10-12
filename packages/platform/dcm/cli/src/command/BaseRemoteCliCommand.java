package com.guhecloud.platform.dcm.command;

import com.guhecloud.platform.dcm.cli.CommandOptionParser;
import com.guhecloud.platform.dcm.client.DcmConfigFeignClient;
import com.guhecloud.platform.dcm.exception.DcmRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClientBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
abstract class BaseRemoteCliCommand extends AbstractCliCommand {
    protected final FeignClientBuilder fcb;

    public BaseRemoteCliCommand(String cmd, CommandOptionParser.CommandOption option, FeignClientBuilder fcb) {
        super(cmd, option);
        this.fcb = fcb;
    }

    @Override
    public void execute() {
        // wait for ouaa token
        List<String> providers = getProviders();
        if (!getOption().isUseAuth() || waitingForOuaaToken(10000)) {
            for (String provider : providers) {
                try {
                    // 逐一向指定目标发送请求
                    String targetUrl = getTargetUrl(provider);
                    log.info("process provider: {}, url: {}", provider, targetUrl);
                    DcmConfigFeignClient client = fcb.forType(DcmConfigFeignClient.class,
                                                              "dcm-config-" + convertProvider2HostNameCompatiable(provider.toLowerCase()))
                            .url(targetUrl)
                            .build();
                    List<String> tenants = getOption().getCommands().stream()
                            .filter(ApplyCommand::isValidTenant)
                            .collect(Collectors.toList());
                    execute(client, getOption().getCommands().contains("system"), tenants, provider);
                    BaseRemoteCliCommand.log.info(">>>> processing {} end", provider);
                } catch (DcmRuntimeException dre) {
                    log.warn(">>>> processing {} error: {}", provider, dre.getMessage());
                }
            }
        } else {
            log.info("fetch token timeout, exit");
        }
    }

    /**
     * Execute on Remote Target.
     *
     * @param client
     * @param system
     * @param tenants
     * @param provider
     *
     * @return
     */
    protected abstract boolean execute(DcmConfigFeignClient client, boolean system, List<String> tenants, String provider);

}
