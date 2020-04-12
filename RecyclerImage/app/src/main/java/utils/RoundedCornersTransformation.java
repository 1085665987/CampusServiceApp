package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

public class RoundedCornersTransformation implements Transformation<Bitmap> {

    private BitmapPool mBitmapPool;
    private int mRadius;

    public RoundedCornersTransformation(Context context, int mRadius) {
        this(Glide.get(context).getBitmapPool(), mRadius);
    }

    public RoundedCornersTransformation(BitmapPool mBitmapPool, int mRadius) {
        this.mBitmapPool = mBitmapPool;
        this.mRadius = mRadius;
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
        //从其包装类中拿出Bitmap
        Bitmap source = resource.get();
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap result = mBitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect(new RectF(0, 0, width, height), mRadius, mRadius, paint);
        //返回包装成Resource的最终Bitmap
        return BitmapResource.obtain(result, mBitmapPool);
    }

    @Override
    public String getId() {
        return "com.wiggins.glide.widget.GlideCircleTransform(radius=" + mRadius + ")";
    }
}