package com.guhecloud.platform.dcm.command;

import com.guhecloud.platform.dcm.cli.CommandOptionParser.CommandOption;
import com.guhecloud.platform.dcm.client.DcmConfigFeignClient;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.http.HttpStatus;

import java.util.List;

@Slf4j
class ExtractCommand extends BaseRemoteCliCommand implements ICliCommand {
    public ExtractCommand(CommandOption option, FeignClientBuilder fcb) {
        super("extract", option, fcb);
    }

    @Override
    protected boolean execute(DcmConfigFeignClient client, boolean system, List<String> tenants, String provider) {
        try {
            Response response = client.getBuiltinDcmDeclarationAsResponse();
            if (response.status() == HttpStatus.OK.value()) {
                dumpResponse(response, buildOutFileName(getOption(), provider + "-builtin-dcm-declaration",
                                                        true, "all", response));
            } else {
                log.warn("error on fetch builtin dcm declaration from {}: http status: {}, message: {}", client, response.status(),
                         response.reason());
            }
        } catch (Exception e) {
            log.warn("fetch builtin dcm declaration from {} failed, error message: {}", client, e.toString());
            log.debug("exception: ", e);
        }
        return true;
    }
}
