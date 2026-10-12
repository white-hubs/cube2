package com.guhecloud.cube.srv.gen.tag.tag.dto.request;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class TagTreeRequest {
    private Period period;
    private String name;

}
