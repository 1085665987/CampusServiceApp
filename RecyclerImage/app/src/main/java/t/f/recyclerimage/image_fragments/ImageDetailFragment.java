package t.f.recyclerimage.image_fragments;

import android.app.Application;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import t.f.recyclerimage.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 单张图片显示Fragment
 * Created by Hankkin on 15/11/22.
 */
public class ImageDetailFragment extends Fragment{
	private String mImageUrl;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;

	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);

		mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});
		mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				setOnLongClick();
				return false;
			}
		});
		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ImageLoader.getInstance().displayImage(mImageUrl, mImageView, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
					message = "下载错误";
					break;
				case DECODING_ERROR:
					message = "图片无法显示";
					break;
				case NETWORK_DENIED:
					message = "网络有问题，无法下载";
					break;
				case OUT_OF_MEMORY:
					message = "图片太大无法显示";
					break;
				case UNKNOWN:
					message = "未知的错误";
					break;
				}
				Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				progressBar.setVisibility(View.GONE);
				mAttacher.update();
			}
		});
	}



	//长按图片是  弹出底部功能栏
	private void setOnLongClick(){
		final Dialog mCameraDialog = new Dialog(getActivity(), R.style.BottomDialog);
		LinearLayout root = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.bottom_menu, null);
		root.findViewById(R.id.share_by_qq).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//实现QQ分享
				Toast.makeText(getActivity().getApplicationContext(),"QQ分享",Toast.LENGTH_SHORT).show();
			}
		});
		root.findViewById(R.id.share_by_wecahr).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//实现微信分享
				Toast.makeText(getActivity().getApplicationContext(),"微信分享",Toast.LENGTH_SHORT).show();
			}
		});
		root.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//实现保存到本地
				Toast.makeText(getActivity().getApplicationContext(),"保存到本地",Toast.LENGTH_SHORT).show();
			}
		});
		root.findViewById(R.id.cencel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mCameraDialog.cancel();
			}
		});
		mCameraDialog.setContentView(root);

		Window dialogWindow = mCameraDialog.getWindow();
		dialogWindow.setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		// 获取对话框当前的参数值
		lp.x = 0; // 新位置X坐标
		lp.y = 0; // 新位置Y坐标
		lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
		root.measure(0, 0);
		lp.height = root.getMeasuredHeight();
		lp.alpha = 9f; // 透明度
		dialogWindow.setAttributes(lp);
		mCameraDialog.show();
	}
}
