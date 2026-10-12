package com.guhecloud.cube.srv.gen.property.management.server.property.service.depository;

import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyCheckTaskRelation;

import java.util.List;

public interface PropertyCheckTaskRelationService {
    Boolean insertList(List<PropertyCheckTaskRelation> taskRelationList);
}
