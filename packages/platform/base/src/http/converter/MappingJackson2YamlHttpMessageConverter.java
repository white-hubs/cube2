package com.guhecloud.platform.base.http.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

public class MappingJackson2YamlHttpMessageConverter extends AbstractJackson2HttpMessageConverter {
    public MappingJackson2YamlHttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper,
              MediaType.parseMediaType("application/yaml"),
              MediaType.parseMediaType("application/yml"),
              MediaType.parseMediaType("application/x-yaml"),
              MediaType.parseMediaType("application/x-yml"),
              MediaType.parseMediaType("text/yaml"),
              MediaType.parseMediaType("text/yml"),
              MediaType.parseMediaType("text/x-yaml"),
              MediaType.parseMediaType("text/x-yml")
             );
    }
}
