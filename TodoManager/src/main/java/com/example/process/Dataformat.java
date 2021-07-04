package com.example.process;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import com.example.model.User;

public class Dataformat {
    public static ExchangeData format(String json) throws JsonSyntaxException {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
        ExchangeData data = null;

        data = gson.fromJson(json, ExchangeData.class);
        return data;
    }
}
