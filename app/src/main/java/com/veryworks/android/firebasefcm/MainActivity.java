package com.veryworks.android.firebasefcm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity implements CustomAdapter.IParent{
    // 파이어베이스 데이터베이스 연결
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference uidRef = database.getReference("uid");

    // 위젯
    RecyclerView recyclerView;
    TextView textReceive;
    EditText editMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUidToServer();
        setWidget();
        setRecycler();
        setFirebaseListener();
    }

    private void setUidToServer(){

    }

    private void setWidget(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        textReceive = (TextView) findViewById(R.id.textReceive);
        editMsg = (EditText) findViewById(R.id.editMsg);
    }

    CustomAdapter adapter;
    private void setRecycler(){
        adapter = new CustomAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setFirebaseListener() {
        uidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Uid> list = new ArrayList<Uid>();
                for(DataSnapshot item : dataSnapshot.getChildren()){

                    Uid uid = item.getValue(Uid.class);
                    list.add(uid);
                }
                refreshAdapter(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void refreshAdapter(List<Uid> list){
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setToken(String token) {
        textReceive.setText(token);
    }
    //--- 레트로핏을 사용하기 위한 선언부 --- //
    // 서비스 인터페이스
    public interface FcmService {
        public static final String SERVER_URL= "http://192.168.10.240:8080/";
        @POST("send_notification")
        Call<Result> sendFcm(@Body Msg data);
    }
    // 전송 클래스
    public class Msg {
        String token;
        String msg;
    }
    // 결과 클래스
    public class Result {
        String result_status;
    }

    public void sendMsg(View view){
        String token = textReceive.getText().toString();
        String msg = editMsg.getText().toString();

        // --- Retrofit 사용 --- //
        // 1. 레트로핏 주소를 가지고 있는 인터페이스를 정의한다. - FcmService
        // 2. 전송값을 전달할 클래스를 정의한다.
        // 3. 결과값을 받을 클래스를 정의한다. - Result

        // 4. Retrofit 클래스를 사용하기 위해 build 한다.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FcmService.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // 5. 전송에 사용될 인터페이스를 초기화한다.
        FcmService service = retrofit.create(FcmService.class);

        // 6. 전송할 클래스에 값을 담는다.
        Msg data = new Msg();
        data.token = token;
        data.msg = msg;

        // 7. 인터페이스 중에 사용할 메서드를 선택한다.
        Call<Result> result = service.sendFcm(data);

        // 8. 서브thread에서 통신한다.
        result.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.isSuccessful()){
                    Result data = response.body();
                    Log.i("Result", "결과값="+data.result_status);
                }else{
                    Log.e("Result", "에러="+response.message());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });

    }
}

class Uid {
    String deviceUid;
    String name;
    String token;

    public Uid(){
        // 파이어베이스 데이터베이스에서 사용하기 위해서는 public 생성자가 필요하다
    }
}