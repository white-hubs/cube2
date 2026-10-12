package com.guhecloud.platform.dcm.command;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.guhecloud.platform.dcm.cli.CommandOptionParser.CommandOption;
import com.guhecloud.platform.dcm.client.DcmConfigFeignClient;
import com.guhecloud.platform.dcm.param.DcmDeclarationParam;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
class DiffCommand extends BaseResourceIteratorRemoteCliCommand implements ICliCommand {
    public DiffCommand(CommandOption option, FeignClientBuilder fcb) {
        super("diff", option, fcb);
    }

    @Override
    protected boolean execute(DcmConfigFeignClient client,
                              DcmDeclarationParam param,
                              boolean system, List<String> tenants, String provider, String resourceNote
                             ) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new YAMLMapper().writeValue(byteArrayOutputStream, param);
        } catch (IOException e) {
            log.error("Failed to encode declaration param, error: {}", e.getMessage());
            return false;
        }
        byte[] contents = byteArrayOutputStream.toByteArray();
        if (system) {
            log.info("fetch system diff from {}", provider);
            doDiffAtomic(client, new ByteArrayInputStream(contents), provider, true, null, resourceNote);
        }
        for (String tenant : tenants) {
            log.info("fetch diff of tenant {} from {}", tenant, provider);
            doDiffAtomic(client, new ByteArrayInputStream(contents), provider, false, tenant, resourceNote);
        }
        return true;
    }

    private void doDiffAtomic(DcmConfigFeignClient client, InputStream paramInRaw, String provider,
                              boolean isSystem, String tenant, String resourceNote
                             ) {
        Response response = client.externalDiffRaw(isSystem, tenant, paramInRaw);
        if (response.status() == HttpStatus.OK.value()) {
            dumpResponse(response, buildOutFileName(getOption(), provider + "-dcm-diff", isSystem, tenant, response));
        } else {
            log.warn("error on diff {} with {}: http status: {}, message: {}", resourceNote, client,
                     response.status(), response.reason());
        }
    }
}
