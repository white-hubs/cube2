package com.guhecloud.kaihe.scada.server.dto.rtdb;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class IOVarDTO {
	
	private String ID;
	private String Name;
	private String Value;
	private String ValueType;
	private String DateStamp;
	private String Status;
	private String IOServerID;
	private String CtrlID;
	private String ChlID;
	private String Area;
	private String Category;
	private String Level;
	private String Enable;
	private String Address;

}
