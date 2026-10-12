package com.guhecloud.cube.deliver.project.merchant.commons.utils;

import jakarta.activation.MimetypesFileTypeMap;

public class ContentTypeUtils {

	private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
	private static MimetypesFileTypeMap mimeTypesMap;

	public static String getContentType(String filename) {
		try {
			if (mimeTypesMap == null) {
				mimeTypesMap = new MimetypesFileTypeMap(ContentTypeUtils.class.getResourceAsStream("mime.types"));
			}
			return mimeTypesMap.getContentType(filename);
		} catch (Exception e) {
			return DEFAULT_CONTENT_TYPE;
		}
	}

}
