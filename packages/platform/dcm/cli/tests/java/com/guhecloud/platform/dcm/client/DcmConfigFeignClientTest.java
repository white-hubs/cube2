package com.guhecloud.platform.dcm.client;

import com.guhecloud.platform.dcm.param.DcmDeclarationParam;
import com.guhecloud.platform.dcm.param.RequestedConfigItemParam;
import com.guhecloud.platform.dcm.utils.DcmTools;
import feign.Logger;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@Slf4j
@SpringBootTest(classes = { TestConfiguration.class })
class DcmConfigFeignClientTest {

    @Autowired
    ApplicationContext applicationContext;

    private FeignClientBuilder builder;

    private static final String default_name = "dcm-config";
    private static final String default_url = "http://localhost:10203/dict";

    @BeforeEach
    public void initialize() {
        log.info("Before initialize ");
        builder = new FeignClientBuilder(applicationContext);
        log.info("After initialize ");
    }

    @Test
    void testFeignClientBuilder() {
        assertNotNull(builder);
    }

    DcmConfigFeignClient getClient(String url, String name) {
        return builder
                .forType(DcmConfigFeignClient.class, name)
                .url(url)
                .customize(builder -> {
                    builder.logger(new Logger.JavaLogger())
                            .logLevel(Logger.Level.BASIC);
                })
                .build();
    }

    @Test
    void getBuiltinDcmDeclaration() {
        DcmConfigFeignClient client = getClient(default_url, default_name);
        DcmDeclarationParam param = client.getBuiltinDcmDeclaration();
        assertNotNull(param);
    }

    @Test
    void getRuntimeDcmDeclaration() {
        DcmConfigFeignClient client = getClient(default_url, default_name);
        DcmDeclarationParam sysRuntime = client.getRuntimeDcmDeclaration(Boolean.TRUE, null);
        assertNotNull(sysRuntime);
        DcmDeclarationParam tenantRuntime = client.getRuntimeDcmDeclaration(Boolean.TRUE, "1");
        assertNotNull(tenantRuntime);
    }

    @Test
    void getInternalDcmDiff() {
        DcmConfigFeignClient client = getClient(default_url, default_name);
        client.internalDiff(Boolean.TRUE, null);
        client.internalDiff(Boolean.FALSE, "1");
    }

    @Test
    void getExternalDcmDiff() throws IOException {
        DcmConfigFeignClient client = getClient(default_url, default_name);
        // DcmDeclarationParam declaration = new DcmDeclarationParam();

        Response response = client.externalDiffRaw(Boolean.TRUE, null, new ClassPathResource("cases/dcm_partial_declaration_1.yaml").getInputStream());
        assertEquals(200, response.status());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamUtils.copy(response.body().asInputStream(), baos);
        assertTrue(baos.size() > 0);
        assertTrue(response.headers().get("Content-Type").contains("application/yaml"));
        // client.internalDiff(Boolean.FALSE, "1");
    }

    private List<RequestedConfigItemParam> parseAsRCIs(ByteArrayOutputStream baos) {
        return DcmTools.Parser.requestConfigItemsFromContent(new ByteArrayInputStream(baos.toByteArray()));
    }

    private List<RequestedConfigItemParam> parseAsRCIs(Response baos) throws IOException {
        return DcmTools.Parser.requestConfigItemsFromContent(baos.body().asInputStream());
    }

    @Test
    void testApplyPartial() throws IOException {
        DcmConfigFeignClient client = getClient(default_url, default_name);

        Response initResponse = client.applyRaw(Boolean.TRUE, null, new ClassPathResource("cases/dcm_init_declaration_sys.yaml").getInputStream());
        assertEquals(200, initResponse.status());
        List<RequestedConfigItemParam> initResponseItems = parseAsRCIs(initResponse);
        log.info("InitResponse: {}", initResponseItems);
        Response firstReponse = client.applyRaw(Boolean.TRUE, null, new ClassPathResource("cases/dcm_partial_declaration_1.yaml").getInputStream());
        assertEquals(200, firstReponse.status());
        assertTrue(firstReponse.headers().get("Content-Type").contains("application/yaml"));
        List<RequestedConfigItemParam> firstResponseItems = parseAsRCIs(firstReponse);
        log.info("First response items: {}", firstResponseItems);
        assertTrue(firstResponseItems.size() > 0, "some declaration should be included");
        Response secondResponse = client.applyRaw(Boolean.TRUE, null, new ClassPathResource("cases/dcm_partial_declaration_1.yaml").getInputStream());
        assertEquals(200, secondResponse.status());
        List<RequestedConfigItemParam> secondResponseItems = parseAsRCIs(secondResponse);
        assertEquals(0, secondResponseItems.size());
    }

    @Test
    void applyDcmDeclaration2() throws IOException {
        DcmConfigFeignClient client = getClient(default_url, default_name);
        Response initResponse = client.applyRaw(Boolean.TRUE, null, new ClassPathResource("cases/dcm_init_declaration_sys.yaml").getInputStream());
        assertEquals(200, initResponse.status());
        Response firstResponse = client.applyRaw(Boolean.TRUE, null, new ClassPathResource("cases/dcm_non_partial_declaration_1.yaml").getInputStream());
        assertEquals(200, firstResponse.status());
        assertEquals(2, parseAsRCIs(firstResponse).size());
        Response secondResponse = client.applyRaw(Boolean.TRUE, null, new ClassPathResource("cases/dcm_non_partial_declaration_1.yaml").getInputStream());
        assertEquals(200, secondResponse.status());
        List<?> secondResponseItems = parseAsRCIs(secondResponse);
        assertEquals(0, secondResponseItems.size());
    }

}
