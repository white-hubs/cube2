package com.guhecloud.kaihe.scada.server.dto.rtdb;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class IOControllerDTO {
	
	private String ID;
	private String Name;
	private String Address;
	private String IOServerID;
	private String ChlID;
	private Boolean State;
	private Boolean Enable;
	private String DateStamp;

}
