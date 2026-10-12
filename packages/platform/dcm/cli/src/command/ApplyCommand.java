package com.guhecloud.platform.dcm.command;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.guhecloud.platform.dcm.cli.CommandOptionParser;
import com.guhecloud.platform.dcm.client.DcmConfigFeignClient;
import com.guhecloud.platform.dcm.param.DcmDeclarationParam;
import com.guhecloud.platform.dcm.param.RequestedConfigItemParam;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClientBuilder;

import java.util.List;

@Slf4j
class ApplyCommand extends BaseResourceIteratorRemoteCliCommand {

    ApplyCommand(CommandOptionParser.CommandOption option, FeignClientBuilder fcb) {
        super("apply", option, fcb);
    }

    protected boolean execute(DcmConfigFeignClient client, DcmDeclarationParam param,
                              boolean hasSystem, List<String> tenants, String provider, String resourceNote
                             ) {
        boolean somethingWrong = false;
        if (hasSystem) {
            log.info("apply system config of {} to {} at {}", resourceNote, provider, client);
            somethingWrong = doApplyAtomic(client, param, provider, true, null) || somethingWrong;
        }
        for (String tenant : tenants) {
            log.info("apply tenant config of {} to provider: {}, tenant: {} at {}",
                     resourceNote, provider, tenant, client);
            somethingWrong = doApplyAtomic(client, param, provider, false, tenant) || somethingWrong;
        }
        return somethingWrong;
    }

    private static boolean doApplyAtomic(DcmConfigFeignClient client, DcmDeclarationParam declarationParam, String provider
            , boolean isSystem, String tenant
                                        ) {
        try {
            List<RequestedConfigItemParam> response = client.applyDcmDeclaration(isSystem, tenant, declarationParam);
            log.info("apply result: {} changes applied", response.size());
            log.debug("contents: {}", new YAMLMapper().writeValueAsString(response));
            return true;
        } catch (Exception e) {
            log.error("apply config to {} failed, param: [isSystem: {} tenant: {}],  error message: {}",
                      client, isSystem, tenant, e.getMessage());
            if (e instanceof FeignException) {
                ((FeignException) e).responseBody().ifPresent(body -> {
                    log.error("detailed error message: {}", new String(body.array()));
                });
            }
        }
        return false;
    }

}
