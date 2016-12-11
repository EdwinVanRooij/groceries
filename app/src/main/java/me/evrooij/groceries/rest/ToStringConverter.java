package me.evrooij.groceries.rest;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;
import retrofit.Converter;

import java.io.IOException;

public final class ToStringConverter implements Converter<String> {

    @Override
    public String fromBody(ResponseBody body) throws IOException {
        return body.string();
    }

    @Override
    public RequestBody toBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }
}