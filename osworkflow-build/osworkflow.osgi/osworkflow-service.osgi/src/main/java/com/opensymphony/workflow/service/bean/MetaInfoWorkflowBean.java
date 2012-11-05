package com.opensymphony.workflow.service.bean;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class MetaInfoWorkflowBean {

    protected Map<String, String> meta;

    protected MetaInfoWorkflowBean() {
        meta = new LinkedHashMap<String, String>();
    }

    public Map<String, String> getMeta() {
        return Collections.unmodifiableMap(meta);
    }

    public void addMeta(String key, String value) {
        meta.put(key, value);
    }
}