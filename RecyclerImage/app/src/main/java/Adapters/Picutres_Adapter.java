package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.List;

import t.f.recyclerimage.R;
import utils.DisplayUtils;
import utils.GlideRoundTransform;
import utils.ScreenUtil;

public class Picutres_Adapter extends RecyclerView.Adapter{

        private Context context;
        private List<String> uris;
        
        public Picutres_Adapter(Context context,List<String> uris){
            this.context=context;
            this.uris=uris;
        }
        
        public interface OnClickListener{
            void onItemClick(int tag, View v, int position);
        }
        private OnClickListener onClickListener;
        
        public void setOnItemClickListener(OnClickListener onItemClickListener){
            this.onClickListener=onItemClickListener;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            
            RelativeLayout parent;
            //用来测量整个控件的宽和高

            ImageView image;
            //选中的图片
            
            ImageView cancel;
            //删除图片的按钮

            public ViewHolder(View itemView) {
                super(itemView);
                image=(ImageView)itemView.findViewById(R.id.image_view);
                cancel=(ImageView)itemView.findViewById(R.id.cancel); 
                parent=(RelativeLayout)itemView.findViewById(R.id.parent_layout);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view,parent,false);
            ViewHolder viewHolder=new ViewHolder(view);

            if (onClickListener==null)
                return viewHolder;

            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(100,v,(Integer) v.getTag(R.id.image_key));
                }
            });
            viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(101,v,(Integer) v.getTag(R.id.image_key));
                }
            });
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            String uri=uris.get(position);
            if(holder instanceof ViewHolder){
                ViewGroup.LayoutParams layoutParams=((ViewHolder) holder).parent.getLayoutParams();
                float itemWith=(ScreenUtil.getScreenWidth(context)- DisplayUtils.dp2px(context,100))/3;
                layoutParams.width= (int) itemWith;
                layoutParams.height= layoutParams.width;
                Glide.with(context).load(uri)
                        .transform(new CenterCrop(context), new GlideRoundTransform(context,3))
                        .override(layoutParams.width,layoutParams.height)
                        .into(((ViewHolder) holder).image);

                if(position==8||uris.size()==1){
                    ((ViewHolder) holder).cancel.setVisibility(View.GONE);
                }
                
                holder.itemView.setTag(position);
                ((ViewHolder) holder).image.setTag(R.id.image_key,position);
                ((ViewHolder) holder).cancel.setTag(R.id.image_key,position);
            }
        }


        @Override
        public int getItemCount() {
            return uris.size();
        }

}