package com.guhecloud.platform.dcm.support;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Map;

public interface ResourceExtractor {

    Map<Resource, String> extractResource(String resourcePattern, boolean isRegex) throws IOException;
}
