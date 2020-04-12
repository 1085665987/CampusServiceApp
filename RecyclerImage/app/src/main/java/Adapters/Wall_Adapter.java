package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import JavaBeans.WallBean;
import t.f.recyclerimage.R;

public class Wall_Adapter extends RecyclerView.Adapter {

    private ArrayList<WallBean> data;
    private Context context;

    private SparseBooleanArray mCheckStates=new SparseBooleanArray();

    public Wall_Adapter(Context context,ArrayList<WallBean> data){
        this.context=context;
        this.data=data;
    }

    /**
     * 表白墙发布的有选择时间的界面的RecycleView适配器
     */

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView startTime;     //起始时间
        TextView stopTime;      //终止时间

        TextView picket;        //剩余票数

        CheckBox checkBox;      //是否选择本时间段的选择框

        LinearLayout parentView;    //父控件，用于添加点击监听

        public ViewHolder(View itemView) {
            super(itemView);

            startTime=(TextView)itemView.findViewById(R.id.start_time);
            stopTime=(TextView)itemView.findViewById(R.id.stop_time);

            picket=(TextView)itemView.findViewById(R.id.picket);
            checkBox=(CheckBox)itemView.findViewById(R.id.check);

            parentView=(LinearLayout)itemView.findViewById(R.id.parent);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wall_item,null);
        final ViewHolder viewHolder=new ViewHolder(view);

        if (mItemClickListener==null)
            return viewHolder;

        viewHolder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(101,view,(Integer) view.getTag());
            }
        });

//        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                mItemClickListener.onItemClick(102,compoundButton,(Integer) compoundButton.getTag());
//            }
//        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WallBean wallBean=data.get(position);
        if (holder instanceof ViewHolder){
            ((ViewHolder) holder).checkBox.setTag(position);

            ((ViewHolder) holder).startTime.setText(wallBean.getStartTime()+":00");
            ((ViewHolder) holder).stopTime.setText(wallBean.getStopTime()+":00");

            ((ViewHolder) holder).picket.setText(wallBean.getPicket()+"");

            ((ViewHolder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int pos= (Integer) compoundButton.getTag();
                    if (b){
                        //点击时将当前CheckBox的索引值和Boolean存入SparseBooleanArray中
                        mCheckStates.put(pos,true);
                    }else {
                        //否则将 当前CheckBox对象从SparseBooleanArray中移除
                        mCheckStates.delete(pos);
                    }
                    mItemClickListener.onItemClick(102,compoundButton,(Integer) compoundButton.getTag());
                }
            });
            //得到CheckBox的Boolean值后，将当前索引的CheckBox状态改变
            ((ViewHolder) holder).checkBox.setChecked(mCheckStates.get(position,false));
            ((ViewHolder) holder).parentView.setTag(position);

            holder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int tag, View view, int position);
    }
    private OnItemClickListener mItemClickListener;
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}
