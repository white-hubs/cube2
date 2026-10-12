package com.guhecloud.platform.dcm.command;

import com.guhecloud.platform.dcm.cli.CommandOptionParser.CommandOption;
import com.guhecloud.platform.dcm.client.DcmConfigFeignClient;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.http.HttpStatus;

import java.util.List;

@Slf4j
class RtExtractCommand extends BaseRemoteCliCommand implements ICliCommand {
    public RtExtractCommand(CommandOption option, FeignClientBuilder fcb) {
        super("rt_extract", option, fcb);
    }

    @Override
    protected boolean execute(DcmConfigFeignClient client, boolean system, List<String> tenants, String provider) {
        if (!system && tenants.isEmpty()) {
            system = true;
        }
        if (system) {
            log.info("fetch system runtime config from {}", client);
            doFetchRuntimeConfig(client, provider, true, null);
        }
        for (String tenant : tenants) {
            log.info("fetch runtime config of tenant {} from {}", tenant, client);
            doFetchRuntimeConfig(client, provider, false, tenant);
        }
        return true;
    }

    private void doFetchRuntimeConfig(DcmConfigFeignClient client, String provider, boolean isSystem, String tenant) {
        try {
            Response response = client.getRuntimeDcmDeclarationAsResponse(isSystem, tenant);
            if (response.status() == HttpStatus.OK.value()) {
                dumpResponse(response, buildOutFileName(getOption(), provider + "-runtime-dcm-config",
                                                        isSystem, tenant, response));
            } else {
                log.warn("fetch runtime config from {} failed, params [isSystem: {} tenant: {}], status: {}, reason: {}",
                         client, isSystem, tenant, response.status(), response.reason());
            }
        } catch (Exception e) {
            log.warn("fetch runtime config from {} failed, params [isSystem: {} tenant: {}], error message: {}", client,
                     isSystem, tenant, e.toString());
            log.debug("exception: ", e);
        }
    }
}
