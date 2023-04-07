package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.List;

import t.f.recyclerimage.R;
import utils.GlideRoundTransform;
import utils.ScreenUtil;

public class ImgSeleted_Adapter extends RecyclerView.Adapter{

        private Context context;
        private List <String> imgUris;

        public ImgSeleted_Adapter(Context context,List<String> imgUris){
            this.context=context;
            this.imgUris=imgUris;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView=(ImageView)itemView.findViewById(R.id.image_view);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view,parent,false);
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

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ViewHolder){
                ViewGroup.LayoutParams layoutParams=  ((ViewHolder) holder).imageView.getLayoutParams();
                float itemWidth=(ScreenUtil.getScreenWidth(context)-90)/3;
                layoutParams.width=(int)itemWidth;
                layoutParams.height=layoutParams.width;
                Glide.with(context).load(imgUris.get(position))
                        .transform(new CenterCrop(context), new GlideRoundTransform(context,3))
                        .override(layoutParams.width,layoutParams.height)
                        .into(((ViewHolder) holder).imageView);
            }
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return imgUris.size();
        }


    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    private OnItemClickListener mItemClickListener;
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
    }