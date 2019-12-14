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
    //Route untuk login
    @GET("login/{email}/{password}")
    Call<User>login(@Path("email") String email,
                    @Path("password") String password);

    //Route untuk list semua Event
    @GET("event")
    Call<GetEvent> getEvent();

    //Route untuk detail Event
    @GET("event/{id}/{uid}")
    Call<Event> getEventById(@Path("id") Integer id,
                             @Path("uid") Integer uid);

    //Route untuk my Event
    @GET("event/user/{uid}")
    Call<GetEvent> getEventByUid(@Path("uid") Integer uid);

    //Route untuk daftar akun
    @FormUrlEncoded
    @POST("register") //Route untuk daftar
    Call<User> register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("cpassword") String cpassword
    );

    //Route untuk tambah Event
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

    //Route ambil data untuk Generate QR
    @FormUrlEncoded
    @POST("ticket/join")
    Call<Ticket> tambahTicket(
            @Field("uidMengikuti") Integer uidMengikuti,
            @Field("eid") Integer eid
    );

    //Route untuk mengambil tiket milik seorang user dari suatu event
    @GET("ticket/{eid}/{uidMengikuti}")
    Call<Ticket> getTicketById(
            @Path("eid") String eid,
            @Path("uidMengikuti") String uid
    );

    //Route untuk list data peserta yang join suatu event
    @GET("ticket/{eid}")
    Call<ListDataTamu> getDataPeserta(
            @Path("eid") Integer eid
            );

    //Route untuk scan
    @GET("ticket/scan/{qrCode}")
    Call<Ticket> scanned(
            @Path("qrCode") String qrCode
    );

    //Route untuk list event yang sudah dijoin seorang user
    @GET("event/mengikuti/{userId}")
    Call<GetEvent> mengikuti(
            @Path("userId") Integer userId
    );

    //Route untuk search
    @GET("search/{cari}")
    Call<GetEvent> search(
            @Path("cari") String cari
    );
}
