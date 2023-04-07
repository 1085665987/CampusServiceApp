package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import JavaBeans.LostForumBean;
import t.f.recyclerimage.Login_Activity;
import t.f.recyclerimage.R;
import utils.DisplayUtils;
import utils.GlideCircleTransform;
import utils.Property;
import views.NineGridTestLayout;
import views.RotateTextView;

/**
 * Created by Friday on 2018/8/11.
 */

public class LostForum_Adapter extends RecyclerView.Adapter {

    private List<LostForumBean>lostForumBeans;
    private Context context;

    public LostForum_Adapter(List<LostForumBean>lostForumBeans,Context context){
        this.context=context;
        this.lostForumBeans=lostForumBeans;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView head,sex;                         //发帖人头像和性别的图标
        TextView nickname,time,school,content;      //发帖人昵称，发帖时间，发帖人学校，帖子正文

        views.RotateTextView isLost;                    //是丢失还是捡到

        LinearLayout share,call;                        //分享此帖，给发帖人打电话

        LinearLayout things_parent;                     //用来动态添加布局的父布局

        RelativeLayout user;                            //用来监听的布局属性

        views.NineGridTestLayout pictures;              //真实图片

        public ViewHolder(View itemView) {
            super(itemView);

            head=(ImageView)itemView.findViewById(R.id.head);
            sex=(ImageView)itemView.findViewById(R.id.sex);

            nickname=(TextView)itemView.findViewById(R.id.nickname);
            time=(TextView)itemView.findViewById(R.id.time);
            school=(TextView)itemView.findViewById(R.id.school);
            content=(TextView)itemView.findViewById(R.id.content);

            pictures=(NineGridTestLayout)itemView.findViewById(R.id.pictures);

            isLost=(RotateTextView)itemView.findViewById(R.id.is_lost);
            share=(LinearLayout)itemView.findViewById(R.id.share);
            call=(LinearLayout)itemView.findViewById(R.id.call);

            things_parent=(LinearLayout)itemView.findViewById(R.id.things_parent);
            user=(RelativeLayout)itemView.findViewById(R.id.user);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lost_thing_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        if (onItemClickListener==null)
            return viewHolder;

        //为查看头像添加监听
        viewHolder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onItemClickListener.onItemClick(Property.FORUM_ITEM_TOUXIANG_TAG,v, (Integer) v.getTag(R.id.image_key));
            }
        });

        //为查看此帖主人空间添加监听
        viewHolder.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(Property.FORUM_ITEM_PARENT_LAYOUT,v, (Integer) v.getTag());
            }
        });

        //为分享此帖添加监听
        viewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(Property.FORUM_ITEM_SHARE_TAG,v, (Integer) v.getTag());
            }
        });

        //为联系（打电话）此人添加监听
        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(Property.FORUM_ITEM_COMMENT_TAG,v, (Integer) v.getTag());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LostForumBean bean=lostForumBeans.get(position);
        if (holder instanceof LostForum_Adapter.ViewHolder){
            ((ViewHolder) holder).nickname.setText(bean.getUserNickname());
            ((ViewHolder) holder).time.setText(bean.getForumDate());
            ((ViewHolder) holder).school.setText(bean.getUserSchool());
            ((ViewHolder) holder).content.setText(bean.getForumContent());

            if (!bean.isLost()){
                ((ViewHolder) holder).isLost.setText("捡到");
                ((ViewHolder) holder).isLost.setBackground(context.getResources().getDrawable(R.drawable.e_rotate_bg2));
            }

            if (bean.getUserSex().trim().equals("女")){
                ((ViewHolder) holder).sex.setImageResource((R.mipmap.ic_launcher_round));
            }

            Glide.with(context).load(bean.getUserImg()).transform(new GlideCircleTransform(context)).into(((ViewHolder) holder).head);


//            String things[]=bean.getThings();
//            TextView thing[]=new TextView[things.length];
//            for (int i=0;i<things.length;i++){
//                thing[i].setLeft(DisplayUtils.dp2px(context,8));
//
//                thing[i].setText(things[i]);
//                thing[i].setTextSize(DisplayUtils.dp2px(context,11));
//                thing[i].setHeight(DisplayUtils.dp2px(context,25));
//                thing[i].setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
//                thing[i].setTextColor(context.getResources().getColor(R.color.cursor_color));
//                thing[i].setBackgroundDrawable(context.getDrawable(R.drawable.stroke));
//                thing[i].setPadding(DisplayUtils.dp2px(context,5),DisplayUtils.dp2px(context,5),DisplayUtils.dp2px(context,5),DisplayUtils.dp2px(context,5));
//                ((ViewHolder) holder).things_parent.addView(thing[i]);
//            }


            ((ViewHolder) holder).pictures.setSpacing(6f);

            if (bean.getImageList() == null) {
                ((ViewHolder) holder).pictures.setVisibility(View.GONE);
            } else {
                ((ViewHolder) holder).pictures.setIsShowAll(true);
                ((ViewHolder) holder).pictures.setUrlList(bean.getImageList().urlList);
            }

            holder.itemView.setTag(position);
//            ((ViewHolder) holder).head.setTag(position,R.id.image_key);
            ((ViewHolder) holder).user.setTag(position);
            ((ViewHolder) holder).share.setTag(position);
            ((ViewHolder) holder).call.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return lostForumBeans.size();
    }


    public interface OnItemClickListener{
        void onItemClick(int tag,View view,int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
}
