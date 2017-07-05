package com.veryworks.android.firebasefcm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 7/5/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder>{
    IParent parent;
    List<Uid> data = new ArrayList<>();

    public CustomAdapter(IParent parent){
        this.parent = parent;
    }

    public void setData(List<Uid> data){
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Uid uid = data.get(position);
        holder.position = position;
        holder.textUid.setText(uid.deviceUid);
        holder.textName.setText(uid.name);
    }

    public interface IParent {
        public void setToken(String token);
    }

    // 아답터를 호출한 클래스의 setToken 호출
    public void setToken(int position){
        parent.setToken(data.get(position).token);
    }

    class Holder extends RecyclerView.ViewHolder {
        int position;
        TextView textUid;
        TextView textName;
        public Holder(View itemView) {
            super(itemView);
            textUid = (TextView) itemView.findViewById(R.id.textUid);
            textName = (TextView) itemView.findViewById(R.id.textName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setToken(position);
                }
            });
        }
    }
}
