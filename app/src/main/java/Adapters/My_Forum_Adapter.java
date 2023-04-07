package Adapters;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import JavaBeans.ForumBean;
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;
import t.f.recyclerimage.R;
import utils.Property;
import views.NineGridTestLayout;

public class My_Forum_Adapter extends RecyclerView.Adapter {
    private Context context;
    private List<ForumBean> list;

    private int day_forums_count=0;

    public My_Forum_Adapter(Context context, List<ForumBean> list) {
        this.list = list;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView one_day;
        RecyclerView forums_in_the_day;
        ForumAdapter forumAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            one_day = (TextView) itemView.findViewById(R.id.time);
            forums_in_the_day = (RecyclerView) itemView.findViewById(R.id.my_day_forum);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_forum_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (day_forums_count!=0){
            day_forums_count--;
            return;
        }

        ForumBean forumBean = list.get(position);
        if (holder instanceof ViewHolder) {
            String day = null;
            String time = null;
            for (int i = 0; i < forumBean.getForumDate().trim().length(); i++) {
                day += forumBean.getForumDate().trim().charAt(i);
                if ((day.equals("昨天") || day.equals("前天")|| day.equals("今天")) && i == 1) {
                    for (i = 2; i < forumBean.getForumDate().trim().length(); i++) {
                        time += forumBean.getForumDate().trim().charAt(i);
                    }
                    break;
                } else {
                    for (i = 0; i < forumBean.getForumDate().trim().length(); i++) {
                        if (i< 4) {
                            day += forumBean.getForumDate().trim().charAt(i);
                            break;
                        }else {
                            time += forumBean.getForumDate().trim().charAt(i);
                        }
                    }
                    break;
                }
            }

            List<ForumBean> day_forums=new ArrayList<ForumBean>() ;
            for (int i=position;i<list.size();i++){
                if (list.get(position).getForumDate().trim().equals(day)){
                    day_forums.add(list.get(position));
                    day_forums_count++;
                }
                break;
            }

            forumBean.setForumDate(time);
            ((ViewHolder) holder).one_day.setText(day);
            ((ViewHolder) holder).forumAdapter = new ForumAdapter(day_forums);
            ((ViewHolder) holder).forums_in_the_day.setAdapter(((ViewHolder) holder).forumAdapter);

            ((ViewHolder) holder).forumAdapter.setItemClickListener(new ForumAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int tag, View view, int position) {

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ForumAdapter extends RecyclerView.Adapter {
        private List<ForumBean> forumList;
        private String TAG = "static class ForumAdapter";

//        private void initImageLoader() {
//            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
//            ImageLoader.getInstance().init(configuration);
//        }

        public ForumAdapter(List<ForumBean> forumList) {
            this.forumList = forumList;
//            initImageLoader();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements AnimateViewHolder {

            NineGridTestLayout layout;                  //帖子的图片      （最多九个）

            ImageView img_love;    //发帖人头像,发帖人性别,点赞img图标

//            ImageView show_dislog;                   //显示dialog，具有删除、屏蔽等功能

            TextView txtDate, txtContent, txtLoveCount, txtCommentCount;
            //这几个TextView分别是：发帖人昵称，发帖日期，发帖人所在学校，帖子内容，赞贴人数，评论人数

            LinearLayout doLove, doComment;      //点赞、评论等的layout

            LinearLayout parentLayout;

            public ViewHolder(View itemView) {
                super(itemView);

//                imageView=(ImageView)itemView.findViewById(R.id.say_item_touxiang) ;

                doLove = (LinearLayout) itemView.findViewById(R.id.love_layout);
                doComment = (LinearLayout) itemView.findViewById(R.id.comment_layout);

                layout = (NineGridTestLayout) itemView.findViewById(R.id.pictures);

//                txtUser=(TextView)itemView.findViewById(R.id.say_item_sayer);
                txtDate = (TextView) itemView.findViewById(R.id.say_item_time);
//                txtUserSchool=(TextView)itemView.findViewById(R.id.say_item_school);
                txtContent = (TextView) itemView.findViewById(R.id.say_item_content);
                txtLoveCount = (TextView) itemView.findViewById(R.id.say_item_love_count);
                txtCommentCount = (TextView) itemView.findViewById(R.id.say_item_comment_count);
//                img_sex=(ImageView)itemView.findViewById(R.id.say_item_sayer_sex);
//                show_dislog=(ImageView) itemView.findViewById(R.id.show_dialog);
                img_love = (ImageView) itemView.findViewById(R.id.say_item_love);

                parentLayout = (LinearLayout) itemView.findViewById(R.id.prient_layout);
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

                ViewCompat.animate(itemView).translationY(0).alpha(1).setDuration(300).setListener(listener).start();
            }

            @Override
            public void animateRemoveImpl(RecyclerView.ViewHolder holder, ViewPropertyAnimatorListener listener) {
                ViewCompat.animate(itemView).translationY(-itemView.getHeight() * 0.3f).alpha(0).setDuration(300).setListener(listener).start();
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_forum_day_item, parent, false);
            Adapters.My_Forum_Adapter.ForumAdapter.ViewHolder holder = new Adapters.My_Forum_Adapter.ForumAdapter.ViewHolder(view);

            //传递过来的接口对象不能为空
            if (mItemClickListener == null)
                return holder;

            //设置显示删除、屏蔽帖子的dialog的监听
//            holder.show_dislog.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mItemClickListener!=null){
//                        mItemClickListener.onItemClick(Property.FORUM_ITEM_SHOW_DIALOG_TAG, view, (Integer) view.getTag());
//                    }
//                }
//            });
//
//            //设置点击头像放大的监听
//            holder.imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mItemClickListener.onItemClick(Property.FORUM_ITEM_TOUXIANG_TAG, view, (Integer) view.getTag(R.id.image_key));
//                }
//            });

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
            if (holder instanceof Adapters.ForumAdapter.ViewHolder) {
//                ((Adapters.ForumAdapter.ViewHolder) holder).txtUser.setText(forumBean.getUserNickname());
                ((Adapters.ForumAdapter.ViewHolder) holder).txtDate.setText(forumBean.getForumDate());
//                ((Adapters.ForumAdapter.ViewHolder) holder).txtUserSchool.setText(forumBean.getUserSchool());
                ((Adapters.ForumAdapter.ViewHolder) holder).txtContent.setText(forumBean.getForumContent());
                ((Adapters.ForumAdapter.ViewHolder) holder).txtLoveCount.setText(forumBean.getLoveCount() + "");
                ((Adapters.ForumAdapter.ViewHolder) holder).txtCommentCount.setText(forumBean.getCommentCount() + "");

//                Glide.with(context).load(forumBean.getUserImg()).transform(new GlideCircleTransform(context)).into(((Adapters.ForumAdapter.ViewHolder) holder).imageView);//加载图片

//                if(forumBean.getUserSex().toString().trim().equals("女"))
//                    Glide.with(context).load(R.mipmap.ic_launcher_round).into(((Adapters.ForumAdapter.ViewHolder) holder).img_sex);       //设置性别为女，默认是男

                if (forumBean.isLoved()) {
//                Glide.with(context).load(R.mipmap.ic_launcher_round).into(((ViewHolder) holder).img_love);       //设置赞
                    ((Adapters.ForumAdapter.ViewHolder) holder).img_love.setImageResource((R.mipmap.ic_launcher_round));
                } else {
//                Glide.with(context).load(R.mipmap.cancer).into(((ViewHolder) holder).img_love);       //设置没点过赞
                    ((Adapters.ForumAdapter.ViewHolder) holder).img_love.setImageResource((R.mipmap.ic_launcher));
                }

                ((Adapters.ForumAdapter.ViewHolder) holder).layout.setSpacing(6f);

                if (forumBean.getImageList() == null) {
                    ((Adapters.ForumAdapter.ViewHolder) holder).layout.setVisibility(View.GONE);
                } else {
                    ((Adapters.ForumAdapter.ViewHolder) holder).layout.setIsShowAll(true);
                    ((Adapters.ForumAdapter.ViewHolder) holder).layout.setUrlList(forumBean.getImageList().urlList);
                }
                holder.itemView.setTag(position);

//                ((Adapters.ForumAdapter.ViewHolder) holder).show_dislog.setTag(position);
                ((Adapters.ForumAdapter.ViewHolder) holder).imageView.setTag(R.id.image_key, position);
                ((Adapters.ForumAdapter.ViewHolder) holder).doLove.setTag(position);
                ((Adapters.ForumAdapter.ViewHolder) holder).doComment.setTag(position);
                ((Adapters.ForumAdapter.ViewHolder) holder).parentLayout.setTag(position);
            }
        }

        @Override
        public int getItemCount() {
            return forumList.size();
        }

        /**
         * 为item添加监听
         */

        public interface OnItemClickListener {
            void onItemClick(int tag, View view, int position);
        }

        private Adapters.My_Forum_Adapter.ForumAdapter.OnItemClickListener mItemClickListener;

        public void setItemClickListener(Adapters.My_Forum_Adapter.ForumAdapter.OnItemClickListener itemClickListener) {
            mItemClickListener = itemClickListener;
        }
    }

}
