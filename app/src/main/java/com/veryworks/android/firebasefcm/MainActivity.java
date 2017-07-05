package com.veryworks.android.firebasefcm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    public void sendMsg(View view){
        String token = textReceive.getText().toString();
        // 여기서 Retrofit
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