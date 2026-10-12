package com.guhecloud.kaihe.scada.server.dto.rtdb;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class RtdbResult<T> {

	private Boolean success;
	private T data;
	private String Code;
	private String Message;
	private String RequestID;
	private String HostId;
	private String ClientId;

}
