package com.guhecloud.kaihe.scada.server.infrastructure.web.servlet.xss;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class XssStringJsonDeserializer extends JsonDeserializer<String> {

	@Override
	public Class<?> handledType() {
		return String.class;
	}

	@Override
	public String deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String value = jsonParser.getValueAsString();
		if (!StringUtils.isEmpty(value)) {
			return JsoupHelper.clean(value);
		}
		return value;
	}

}
