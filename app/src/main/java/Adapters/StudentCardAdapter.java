package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.List;

import t.f.recyclerimage.Login_Activity;
import t.f.recyclerimage.R;
import utils.GlideCircleTransform;
import utils.GlideRoundTransform;

/**
 * Created by Friday on 2018/7/16.
 */

public class StudentCardAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<String> txtList;
    private List<Integer> imageList;
    private int layoutId;
    public static String TAG="StudentCardAdapter";

    public StudentCardAdapter(List<String> txtList, List<Integer> imageList,Context context,int layoutId){
        this.context=context;
        this.imageList=imageList;
        this.txtList=txtList;
        this.layoutId=layoutId;

        Log.i(TAG,"imageList是否为空："+(imageList==null)+imageList.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView) (itemView).findViewById(R.id.student_card_image_item);
            textView=(TextView)itemView.findViewById(R.id.student_card_text_item);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener!=null){
                    mItemClickListener.onItemClick((Integer) view.getTag());
                }
            }
        });
        return holder;
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String txt=txtList.get(position);
        int imgId=imageList.get(position);
        Log.i(TAG,"第"+position+"个"+imgId);
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).textView.setText(txt);
            Glide.with(context).load(imgId).into(((ViewHolder) holder).imageView);
           // Glide.with(context) .load(imgId).transform(new CenterCrop(context), new GlideRoundTransform(context)).into(((ViewHolder) holder).imageView);
        }
        holder.itemView.setTag(position);
    }
    /**
     * @return
     */
    @Override
    public int getItemCount() {
        return txtList.size()>imageList.size()?txtList.size():imageList.size();
    }

    /**
     * 为item添加监听
     */

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    private OnItemClickListener mItemClickListener;
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}
