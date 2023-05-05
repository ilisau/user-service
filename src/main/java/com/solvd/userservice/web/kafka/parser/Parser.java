package com.solvd.userservice.web.kafka.parser;

import com.google.gson.internal.LinkedTreeMap;

public interface Parser<T> {

    /**
     * Parse the map to the object.
     *
     * @param map the map
     * @return the object
     */
    T parse(LinkedTreeMap<String, String> map);

}
