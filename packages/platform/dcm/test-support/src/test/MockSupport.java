package com.guhecloud.platform.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class MockSupport {
    public static <T> T mockResultsFromResource(String resourcePath, TypeReference<T> typeDescription) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(resourcePath);
        return new YAMLMapper().readValue(classPathResource.getInputStream(), typeDescription);
    }

}
