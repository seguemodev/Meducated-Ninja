package com.seguetech.zippy.data.rest;


import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.Path;

public interface TermService {
    @GET("/api/Term/{term}")
    @Headers({"OkHttp-Force-Offline:true"})
    ArrayList<String> search(@Path("term") String term);

}
