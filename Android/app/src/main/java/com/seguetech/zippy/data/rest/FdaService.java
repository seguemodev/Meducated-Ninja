package com.seguetech.zippy.data.rest;

import com.seguetech.zippy.data.model.openfda.OpenFdaResponse;

import retrofit.http.GET;
import retrofit.http.Query;

public interface FdaService {
    @GET("/drug/label.json")
    OpenFdaResponse search(
            @Query("api_key") String apiKey,
            @Query(value = "search", encodeValue = false) String search,
            @Query("limit") int limit);
}
