package com.event.appevent.network;

import com.event.appevent.model.Event;
import com.event.appevent.model.EventTes;
import com.event.appevent.model.GetEvent;
import com.event.appevent.model.User;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("login/{email}/{password}")
    Call<User>login(@Path("email") String email,
                    @Path("password") String password);

    @GET("event")
    Call<GetEvent> getEvent();

    @GET("event/{id}")
    Call<Event> getEventById(@Path("id") Integer id);

    @GET("event/user/{uid}")
    Call<GetEvent> getEventByUid(@Path("uid") Integer uid);

    @FormUrlEncoded
    @POST("register")
    Call<User> register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("cpassword") String cpassword
    );

    @Multipart
    @FormUrlEncoded
    @POST("event/tambah")
    Call<Event> tambahEvent(
            @Field("namaEvent") String namaEvent,
            @Field("tanggalEvent") String tanggalEvent,
            @Field("jamEvent") String jamEvent,
            @Field("jumlahPesertaEvent") String jumlahPesertaEvent,
            @Field("lokasiEvent") String lokasiEvent,
            @Field("deskripsiEvent") String deskripsiEvent,
            @Part MultipartBody.Part image
    );
}
