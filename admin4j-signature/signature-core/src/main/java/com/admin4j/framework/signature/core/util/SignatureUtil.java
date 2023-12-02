package com.admin4j.framework.signature.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SignatureUtil {

    @SuppressWarnings("unchecked")
    public static TreeMap<String, Object> traverseMap(Map<String, Object> map) {
        TreeMap<String, Object> result = new TreeMap<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                result.put(key, traverseMap((Map<String, Object>) value));
            } else if (value instanceof List) {
                result.put(key, traverseList((List<Object>) value));
            } else {
                result.put(key, value);
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public static List<Object> traverseList(List<Object> list) {
        List<Object> result = new ArrayList<>();

        for (Object obj : list) {
            if (obj instanceof Map) {
                result.add(traverseMap((Map<String, Object>) obj));
            } else if (obj instanceof List) {
                result.add(traverseList((List<Object>) obj));
            } else {
                result.add(obj);
            }
        }

        return result;
    }

}

