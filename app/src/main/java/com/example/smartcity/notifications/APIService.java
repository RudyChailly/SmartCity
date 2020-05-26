package com.example.smartcity.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
        {
            "Content-Type:application/json",
            "Authorization:key=AAAAWb8LdsM:APA91bEF8ayd7f1q-Ov5viazHqxMBkGIKLPm2LsE0aeBeTI89JDSxiiI5F_WTca6jP6cu_YjvR7KRTmMPVeNvTjH3OZO_LZks08FWctV6AbH5LK-lYDihBaD6My4qbiD0r5GePDAAvxd"
        }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
