package com.event.appevent.network;

import com.event.appevent.model.Event;
import com.event.appevent.model.GetEvent;
import com.event.appevent.model.ListDataTamu;
import com.event.appevent.model.ResponseEvent;
import com.event.appevent.model.Ticket;
import com.event.appevent.model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    @GET("event/{id}/{uid}")
    Call<Event> getEventById(@Path("id") Integer id,
                             @Path("uid") Integer uid);

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
    @POST("event/tambah")
    Call<ResponseEvent> tambahEvent(
            @Part("uid") RequestBody uid,
            @Part("namaEvent") RequestBody namaEvent,
            @Part("tanggalEvent") RequestBody tanggalEvent,
            @Part("jamEvent") RequestBody jamEvent,
            @Part("jumlahPesertaEvent") RequestBody jumlahPesertaEvent,
            @Part("lokasiEvent") RequestBody lokasiEvent,
            @Part MultipartBody.Part brosurEvent,
            @Part("deskripsiEvent") RequestBody deskripsiEvent

    );

    @FormUrlEncoded
    @POST("ticket/join")
    Call<Ticket> tambahTicket(
            @Field("uidMengikuti") Integer uidMengikuti,
            @Field("eid") Integer eid
    );

    @GET("ticket/{eid}/{uidMengikuti}")
    Call<Ticket> getTicketById(
            @Path("eid") String eid,
            @Path("uidMengikuti") String uid
    );

    @GET("ticket/{eid}")
    Call<ListDataTamu> getDataPeserta(
            @Path("eid") Integer eid
            );

    @GET("ticket/scan/{qrCode}")
    Call<Ticket> scanned(
            @Path("qrCode") String qrCode
    );

    @GET("event/mengikuti/{userId}")
    Call<GetEvent> mengikuti(
            @Path("userId") Integer userId
    );

    @GET("search/{cari}")
    Call<GetEvent> search(
            @Path("cari") String cari
    );
}
