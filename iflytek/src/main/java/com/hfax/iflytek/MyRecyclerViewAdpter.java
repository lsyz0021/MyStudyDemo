package com.hfax.iflytek;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 作者：lcw 16-9-6
 * 博客：http://blog.csdn.net/lsyz0021/
 */
public class MyRecyclerViewAdpter extends RecyclerView.Adapter<MyRecyclerViewAdpter.MyViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private ArrayList<String> list;
    private OnItemClickListener onItemClickListener;

    public MyRecyclerViewAdpter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.layout_cv_list, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int position) {
        myViewHolder.tv_item.setText(list.get(position));

        if (onItemClickListener != null) {
            // 短按
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(position);
                }
            });

            // 长按
            myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onLongClick(position);
                    return false;
                }
            });

        }


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item = (TextView) itemView.findViewById(R.id.tv_item);

        }
    }

    public interface OnItemClickListener {
        public void onClick(int position);

        public void onLongClick(int position);

    }

    public void setOnItemClickLisnter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
