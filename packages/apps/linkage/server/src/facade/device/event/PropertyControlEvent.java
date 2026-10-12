package com.guhecloud.cube.biz.linkage.server.facade.device.event;

import com.guhecloud.cube.biz.linkage.server.facade.iot.reqeust.PropertyControlRequest;
import com.guhecloud.cube.biz.linkage.server.facade.iot.response.PropertyControlResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
public class PropertyControlEvent extends ApplicationEvent implements Supplier<List<PropertyControlResponse>>, Consumer<List<PropertyControlResponse>> {

    private final List<PropertyControlRequest> propertyControlRequests;

    private List<PropertyControlResponse> propertyControlResponses;

    public PropertyControlEvent(Object source, List<PropertyControlRequest> propertyControlRequests) {
        super(source);
        this.propertyControlRequests = propertyControlRequests;
    }

    @Override
    public void accept(List<PropertyControlResponse> propertyControlResponse) {
        this.propertyControlResponses = propertyControlResponse;
    }

    @Override
    public List<PropertyControlResponse> get() {
        return this.propertyControlResponses;
    }
}
