package com.example.a.webviewtest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.domain.JsonRootBean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/26.
 */

public class CqsscRecyAdapter  extends RecyclerView.Adapter<CqsscRecyAdapter.ViewHolder> {
    private List<JsonRootBean.DataBean> mCqsscList;

    public CqsscRecyAdapter(List<JsonRootBean.DataBean> list){
        mCqsscList=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cqssc_item, parent, false);
        final ViewHolder holder=new ViewHolder(view);
        holder.expect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                JsonRootBean.DataBean cqssc = mCqsscList.get(position);
                Toast.makeText(v.getContext(),"您点击了期号"+cqssc.getExpect(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.opencode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                JsonRootBean.DataBean cqssc = mCqsscList.get(position);
                Toast.makeText(v.getContext(),"您点击了开奖号码"+cqssc.getOpencode(),Toast.LENGTH_SHORT).show();

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        JsonRootBean.DataBean cqssc = mCqsscList.get(position);
        holder.expect.setText(cqssc.getExpect());
        holder.opencode.setText(cqssc.getOpencode());
        holder.opentime.setText(cqssc.getOpentime());
        holder.opentimestamp.setText(cqssc.getOpentimestamp()+"");
    }

    @Override
    public int getItemCount() {
        return mCqsscList.size();
    }

    static  class ViewHolder extends  RecyclerView.ViewHolder{
        View cqssc;

        TextView expect;
        TextView opencode;
        TextView opentime;
        TextView opentimestamp;
        public ViewHolder(View view){
            super(view);
            cqssc=view;
            expect=(TextView)view.findViewById(R.id.text_expect);
            opencode=(TextView)view.findViewById(R.id.text_opencode);
            opentime=(TextView)view.findViewById(R.id.text_opentime);
            opentimestamp=(TextView)view.findViewById(R.id.text_opentimestamp);
        }
    }
}
