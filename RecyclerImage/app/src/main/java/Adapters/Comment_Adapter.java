package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import JavaBeans.AnswerBean;
import JavaBeans.CommentBean;
import t.f.recyclerimage.R;
import utils.GlideCircleTransform;
import utils.LisetViewUtil;
import utils.Property;

public class Comment_Adapter extends RecyclerView.Adapter {
    private static final String TAG = "Comment_Adapter";
    private List<CommentBean> commentBeans;
    private Context context;

    public Comment_Adapter(Context context, List<CommentBean> commentBeans) {
        this.commentBeans = commentBeans;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView commenter_head, love_comment, comment_comment;
        //分别是评论人的头像，给次评论点赞的img，评论这条评论的img
        TextView commenter_nickname, comment_time, comment_context;
        //分别是评论者的昵称，这条评论发表的时间，评论的正文
        ListView answer;                //回复

        public ViewHolder(View itemView) {
            super(itemView);

            commenter_head = (ImageView) itemView.findViewById(R.id.commenter_head);
            love_comment = (ImageView) itemView.findViewById(R.id.love_comment);
            comment_comment = (ImageView) itemView.findViewById(R.id.comment_comment);

            commenter_nickname = (TextView) itemView.findViewById(R.id.commenter_nickname);
            comment_time = (TextView) itemView.findViewById(R.id.commenter_time);
            comment_context = (TextView) itemView.findViewById(R.id.comment_context);

            answer = (ListView) itemView.findViewById(R.id.answer_list);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        if (onItemClickListener == null)
            return holder;
        holder.commenter_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(Property.COMMENT_ITEM_COMMENTER_HEAD, v, (Integer) v.getTag(R.id.image_key));
            }
        });
        holder.commenter_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(Property.COMMENT_ITEM_COMMENTER_NICKNAME, v, (Integer) v.getTag());
            }
        });
        holder.love_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(Property.COMMENT_ITEM_LOVE_COMMENT, v, (Integer) v.getTag());
            }
        });
        holder.comment_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(Property.COMMENT_ITEM_COMMENT_COMMENT, v, (Integer) v.getTag());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentBean commentBean = commentBeans.get(position);
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).commenter_nickname.setText(commentBean.getCommenterNickname());
            ((ViewHolder) holder).comment_time.setText(commentBean.getCommentTime());
            ((ViewHolder) holder).comment_context.setText(commentBean.getCommentContext());

            Log.e(TAG, commentBean.getCommenterNickname() + "评论：" + commentBean.getCommentContext());

            Glide.with(context).load(commentBean.getCommenterHead()).transform(new GlideCircleTransform(context)).into(((ViewHolder) holder).commenter_head);

            List<AnswerBean> answerBeans = commentBean.getAnswerBeans();
            if (answerBeans != null) {
                ArrayAdapter<SpannableString> adapter = new ArrayAdapter<SpannableString>(context, R.layout.answer_item_1);
                for (int i = 0; i < answerBeans.size(); i++) {
                    StringBuffer sb = new StringBuffer();

                    AnswerBean answerBean = answerBeans.get(i);
                    String answeder = answerBean.getAnswer().getNikeName();
                    String answer = answerBean.getAnsweder().getNikeName();
                    String answer_content = answerBean.getAnswerContext();

                    sb.append(answer);
                    sb.append("回复");
                    sb.append(answeder);
                    sb.append("：");
                    sb.append(answer_content);

                    Log.e(TAG, sb.toString());

                    SpannableString spannableString = new SpannableString(sb.toString());
                    spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 0, answer.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF895EFF")), answer.length() + 2, answer.length() + 2 + answeder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF895EFF")), answer.length() + 2 + answeder.length() + 1, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    adapter.add(spannableString);
                }

                ((ViewHolder) holder).answer.setAdapter(adapter);
            }
            holder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return commentBeans.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int tag, View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}