package com.guhecloud.cube.biz.collection.analysis.collection;

import java.util.List;

public interface ICleaner<REQ, REP> {

    List<REP> cleanData(List<REQ> reqs);
}
