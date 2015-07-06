package com.seguetech.zippy.data.rest;

import com.seguetech.zippy.data.model.rximage.RxImage;

import retrofit.http.GET;
import retrofit.http.Query;

public interface RxImageService {
    @GET("/api/rximage/1/rxnav")
    RxImage search(
            @Query("ndc") String ndc,
            @Query("resolution") String resolution
    );
}
