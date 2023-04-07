package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import t.f.recyclerimage.R;

/**
 * Created by Friday on 2018/7/12.
 */

public class FunctionGridViewAdapter extends BaseAdapter {
    private String [] resource;
    private int [] resourceImg;
    private Context context;

    private class ViewHolder {
        ImageView img;
        TextView text;
    }

    public FunctionGridViewAdapter(String [] resource, int [] resourceImg, Context context){
        this.resource=resource;
        this.resourceImg=resourceImg;
        this.context=context;
    }

    @Override
    public int getCount() {
        return resource.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.mainly_function_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView) convertView.findViewById(R.id.function_img_item);
            viewHolder.text = (TextView) convertView.findViewById(R.id.function_txt_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text.setText(resource[i]);
        viewHolder.img.setImageResource(resourceImg[i]);
        return convertView;
    }
}
