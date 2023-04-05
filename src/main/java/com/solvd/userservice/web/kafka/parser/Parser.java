package com.solvd.userservice.web.kafka.parser;

import com.google.gson.internal.LinkedTreeMap;

public interface Parser<T> {

    T parse(LinkedTreeMap<String, String> map);

}
