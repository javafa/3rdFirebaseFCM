package com.veryworks.android.firebasefcm;

import android.provider.Settings;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static android.content.ContentValues.TAG;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference uidRef = database.getReference("uid");

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendTokenToServer(refreshedToken);
    }

    private void sendTokenToServer(String token){
        // 1. UUID 생성 후 저장 객체에 담기
        String deviceUid = Settings.Secure.getString(
                getContentResolver(), Settings.Secure.ANDROID_ID
        );
        Log.e("UUID---", deviceUid+"");
        Uid uid = new Uid();
        uid.deviceUid = deviceUid;
        uid.name = "손님";
        uid.token = token;
        // 2. 데이터베이스에 저장
        uidRef.child(deviceUid).setValue( uid );
    }
}
