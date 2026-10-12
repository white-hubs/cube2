package com.guhecloud.kaihe.scada.server.dto.rtdb;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class IOServerDTO {
	
	private String ID;
	private String Name;
	private String LastTime;
	private String IPAddress;
	private String Host;
	//private Boolean State;

}
