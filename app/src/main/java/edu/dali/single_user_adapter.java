package edu.dali;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class single_user_adapter extends RecyclerView.Adapter<single_user_adapter.ImageViewHolder> {
    Context mcontext;
    List<single_user> list;
    public single_user_adapter(Context context,List<single_user> list){
        this.mcontext=context;
        this.list=list;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v= LayoutInflater.from(mcontext).inflate(R.layout.item_single_user,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int i) {
          holder.username.setText(list.get(i).getUsername());
          holder.information.setText(list.get(i).getInfomation());
          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Toast.makeText(mcontext,"Position "+ i,Toast.LENGTH_SHORT).show();
              }
          });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public TextView information;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username);
            information=itemView.findViewById(R.id.information);
        }
    }
    public void Filterlist(ArrayList<single_user> filterlist){
        list=filterlist;
        notifyDataSetChanged();
    }
}
