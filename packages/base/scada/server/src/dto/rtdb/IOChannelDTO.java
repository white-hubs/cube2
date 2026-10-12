package com.guhecloud.kaihe.scada.server.dto.rtdb;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class IOChannelDTO {

	private String ID;
	private String Name;
	private String IOServerID;
	private Boolean State;
	private String ProtocolName;
	private String DateStamp;
	private Integer Interval;
	private Boolean Enable;

}
