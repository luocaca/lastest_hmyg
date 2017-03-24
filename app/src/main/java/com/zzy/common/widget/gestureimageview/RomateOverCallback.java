package com.zzy.common.widget.gestureimageview;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RomateOverCallback {

	public static final int TYPE_CHAT_PIC = 0;
	
	public static final int TYPE_FEED_PIC = 1;
	
	
	public interface IRomateOverListener {

		public void notifyRomateOver(int type, long id, int rotation);
	}

	/** 软引用队列 */
	private List<WeakReference<IRomateOverListener>> iRomateOverListeners = new ArrayList<WeakReference<IRomateOverListener>>();

	private static RomateOverCallback romateOverCallback;

	public static RomateOverCallback getInstance() {
		if (romateOverCallback == null)
			romateOverCallback = new RomateOverCallback();
		return romateOverCallback;
	}

	/**
	 * 添加接口监听
	 * 
	 * @param iJoinGroup
	 */
	public void addIRomateOverListener(IRomateOverListener iRomateOverListener) {
		iRomateOverListeners.add(new WeakReference<IRomateOverListener>(
				iRomateOverListener));
	}

	public void removeIRomateOverListener(
			IRomateOverListener iRomateOverListener) {
		iRomateOverListeners.remove(new WeakReference<IRomateOverListener>(
				iRomateOverListener));
	}

	public void notifyRomateOver(int type, long id, int rotation) {
		for (WeakReference<IRomateOverListener> i : iRomateOverListeners) {
			IRomateOverListener ir = i.get();
			if (ir != null)
				ir.notifyRomateOver(type, id, rotation);
		}
	}
}
