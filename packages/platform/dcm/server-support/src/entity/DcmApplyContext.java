package com.guhecloud.platform.dcm.entity;

import com.guhecloud.platform.dcm.Identifiable;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
public class DcmApplyContext<BE, DE extends Identifiable> {
    public static <BE, DE extends Identifiable> DcmApplyContext<BE, DE> create() {
        return DcmApplyContext.<BE, DE>builder()
                .bizEntitiesToAdd(new ArrayList<>())
                .bizEntitiesToUpdate(new ArrayList<>())
                .bizEntitiesToRemove(new ArrayList<>())
                .dcmEntitiesToAdd(new ArrayList<>())
                .dcmEntitiesToUpdate(new ArrayList<>())
                .dcmEntitiesToRemove(new ArrayList<>())
                .build();
    }

    private Collection<BE> bizEntitiesToAdd;
    private Collection<DE> dcmEntitiesToAdd;
    private Collection<BE> bizEntitiesToUpdate;
    private Collection<DE> dcmEntitiesToUpdate;
    private Collection<BE> bizEntitiesToRemove;
    private Collection<DE> dcmEntitiesToRemove;

    public boolean isUpdateNeeded() {
        return !bizEntitiesToUpdate.isEmpty();
    }

    public boolean isAddNeeded() {
        return !bizEntitiesToAdd.isEmpty() || !dcmEntitiesToAdd.isEmpty();
    }

    public boolean isRemoveNeeded() {
        return !bizEntitiesToRemove.isEmpty();
    }
}
