package com.hldj.hmyg.buy.adapter;

import com.hy.utils.GifHelper.GifFrame;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;


//用来循环播放Gif每帧图片  
class PlayGifTask implements Runnable {
	int i = 0;
	ImageView iv;
	GifFrame[] frames;
	int framelen, oncePlayTime = 0;

	public PlayGifTask(ImageView iv, GifFrame[] frames) {
		this.iv = iv;
		this.frames = frames;

		int n = 0;
		framelen = frames.length;
		while (n < framelen) {
			oncePlayTime += frames[n].delay;
			n++;
		}
		Log.d("msg", "playTime= " + oncePlayTime); // Gif图片单次播放时长

	}

	Handler h2 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				iv.setImageBitmap((Bitmap) msg.obj);
				break;
			}
		};
	};

	@Override
	public void run() {
		if (!frames[i].image.isRecycled()) {
			// iv.setImageBitmap(frames[i].image);
			Message m = Message.obtain(h2, 1, frames[i].image);
			m.sendToTarget();
		}
		iv.postDelayed(this, frames[i++].delay);
		i %= framelen;
	}

	public void startTask() {
		iv.post(this);
	}

	public void stopTask() {
		if (null != iv)
			iv.removeCallbacks(this);
		iv = null;
		if (null != frames) {
			for (GifFrame frame : frames) {
				if (frame.image != null && !frame.image.isRecycled()) {
					frame.image.recycle();
					frame.image = null;
				}
			}
			frames = null;
			// mGifTask=null;
		}
	}

}