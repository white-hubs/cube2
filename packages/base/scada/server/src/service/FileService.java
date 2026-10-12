package com.guhecloud.kaihe.scada.server.service;

public interface FileService {
	
	
	public static final String TEMP_DIAGRAMS_FOLDER = "/TempDiagrams/";
	public static final String DIAGRAMS_FOLDER = "/diagrams/";
	public static final String FIGURES_FOLDER = "/figures/";
	public static final String IMPORT_FOLDER = "/import/";

	public String getRootDir();
	
	public void saveFile(String fileName, byte[] content, String... dir);
	
	public byte[] getFile(String fileName, String... dir);

	public void removeFile(String fileName, String... dir);

	public void removeDir(String... dir);

}
