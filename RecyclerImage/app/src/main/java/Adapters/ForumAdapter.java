package Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButtonsController;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;
import t.f.recyclerimage.Login_Activity;
import t.f.recyclerimage.R;
import java.util.List;

import JavaBeans.ForumBean;
import utils.GlideCircleTransform;
import utils.NineGridTestModel;
import utils.Property;
import utils.ZoomTutorial;
import views.NineGridTestLayout;

/**
 * Created by Friday on 2018/7/17.
 */

public class ForumAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<ForumBean> forumList;
    private static String TAG="ForumAdapter";

    private void initImageLoader() {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(configuration);
    }

    public  ForumAdapter(List<ForumBean> forumList,Context context){
        this.context=context;
        this.forumList=forumList;
        initImageLoader();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements AnimateViewHolder {

        NineGridTestLayout layout;                  //帖子的图片      （最多九个）

        ImageView imageView,img_sex,img_love;    //发帖人头像,发帖人性别,点赞img图标

        ImageView show_dislog;                   //显示dialog，具有删除、屏蔽等功能

        TextView txtUser,txtDate,txtUserSchool,txtContent,txtLoveCount,txtCommentCount;
        //这几个TextView分别是：发帖人昵称，发帖日期，发帖人所在学校，帖子内容，赞贴人数，评论人数

        LinearLayout doLove,doComment;      //点赞、评论等的layout

        LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.say_item_touxiang) ;

            doLove=(LinearLayout)itemView.findViewById(R.id.love_layout);
            doComment=(LinearLayout)itemView.findViewById(R.id.comment_layout);

            layout=(NineGridTestLayout)itemView.findViewById(R.id.layout_nine_grid);

            txtUser=(TextView)itemView.findViewById(R.id.say_item_sayer);
            txtDate=(TextView)itemView.findViewById(R.id.say_item_time);
            txtUserSchool=(TextView)itemView.findViewById(R.id.say_item_school);
            txtContent=(TextView)itemView.findViewById(R.id.say_item_content);
            txtLoveCount=(TextView)itemView.findViewById(R.id.say_item_love_count);
            txtCommentCount=(TextView)itemView.findViewById(R.id.say_item_comment_count);
            img_sex=(ImageView)itemView.findViewById(R.id.say_item_sayer_sex);
            show_dislog=(ImageView) itemView.findViewById(R.id.show_dialog);
            img_love=(ImageView)itemView.findViewById(R.id.say_item_love);

            parentLayout=(LinearLayout)itemView.findViewById(R.id.prient_layout);
        }

        @Override
        public void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
            ViewCompat.setTranslationY(itemView, -itemView.getHeight() * 0.3f);
            ViewCompat.setAlpha(itemView, 0);
        }

        @Override
        public void preAnimateRemoveImpl(RecyclerView.ViewHolder holder) {

        }

        @Override
        public void animateAddImpl(RecyclerView.ViewHolder holder, ViewPropertyAnimatorListener listener) {

            ViewCompat.animate(itemView) .translationY(0) .alpha(1) .setDuration(300) .setListener(listener) .start();
        }

        @Override
        public void animateRemoveImpl(RecyclerView.ViewHolder holder, ViewPropertyAnimatorListener listener) {
            ViewCompat.animate(itemView) .translationY(-itemView.getHeight() * 0.3f) .alpha(0) .setDuration(300) .setListener(listener) .start();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.say_item,parent,false);
        ForumAdapter.ViewHolder holder = new ForumAdapter.ViewHolder(view);

        //传递过来的接口对象不能为空
        if(mItemClickListener==null)
            return holder;

        //设置显示删除、屏蔽帖子的dialog的监听
        holder.show_dislog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener!=null){
                    mItemClickListener.onItemClick(Property.FORUM_ITEM_SHOW_DIALOG_TAG, view, (Integer) view.getTag());
                }
            }
        });

        //设置点击头像放大的监听
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(Property.FORUM_ITEM_TOUXIANG_TAG, view, (Integer) view.getTag(R.id.image_key));
            }
        });

        //设置点赞的监听
        holder.doLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(Property.FORUM_ITEM_LOVE_TAG, view, (Integer) view.getTag());
            }
        });

        //设置评论的监听
        holder.doComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(Property.FORUM_ITEM_COMMENT_TAG, view, (Integer) view.getTag());
            }
        });

        //设置点击整个帖子，用来查看帖子详情的监听
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(Property.FORUM_ITEM_PARENT_LAYOUT, view, (Integer) view.getTag());
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if()
        ForumBean forumBean = forumList.get(position);
        if (holder instanceof ForumAdapter.ViewHolder) {
            ((ViewHolder) holder).txtUser.setText(forumBean.getUserNickname());
            ((ViewHolder) holder).txtDate.setText(forumBean.getForumDate());
            ((ViewHolder) holder).txtUserSchool.setText(forumBean.getUserSchool());
            ((ViewHolder) holder).txtContent.setText(forumBean.getForumContent());
            ((ViewHolder) holder).txtLoveCount.setText(forumBean.getLoveCount()+"");
            ((ViewHolder) holder).txtCommentCount.setText(forumBean.getCommentCount()+"");

            Glide.with(context).load(forumBean.getUserImg()).transform(new GlideCircleTransform(context)).into(((ViewHolder) holder).imageView);//加载图片

            if(forumBean.getUserSex().toString().trim().equals("女"))
                Glide.with(context).load(R.mipmap.woman).into(((ViewHolder) holder).img_sex);       //设置性别为女，默认是男

            if(forumBean.isLoved()){
//                Glide.with(context).load(R.mipmap.ic_launcher_round).into(((ViewHolder) holder).img_love);       //设置赞
                ((ViewHolder) holder).img_love.setImageResource((R.mipmap.ic_launcher_round));
            }else{
//                Glide.with(context).load(R.mipmap.cancer).into(((ViewHolder) holder).img_love);       //设置没点过赞
                ((ViewHolder) holder).img_love.setImageResource((R.mipmap.ic_launcher));
            }

            ((ViewHolder) holder).layout.setSpacing(6f);

            if (forumBean.getImageList() == null) {
                ((ViewHolder) holder).layout.setVisibility(View.GONE);
            } else {
                ((ViewHolder) holder).layout.setIsShowAll(true);
                ((ViewHolder) holder).layout.setUrlList(forumBean.getImageList().urlList);
            }
            holder.itemView.setTag(position);

            ((ViewHolder) holder).show_dislog.setTag(position);
            ((ViewHolder) holder).imageView.setTag(R.id.image_key,position);
            ((ViewHolder) holder).doLove.setTag(position);
            ((ViewHolder) holder).doComment.setTag(position);
            ((ViewHolder) holder).parentLayout.setTag(position);
        }
    }
    @Override
    public int getItemCount() {
        return forumList.size();
    }

    /**
     * 为item添加监听
     */

    public interface OnItemClickListener{
        void onItemClick(int tag, View view, int position);
    }
    private OnItemClickListener mItemClickListener;
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}
