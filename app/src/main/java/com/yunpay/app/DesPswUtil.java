package com.yunpay.app;

/**
 * Created by Administrator on 2016/7/21.
 */
public class DesPswUtil {
    static {
        try {
            System.loadLibrary("JniTest");
        } catch (Throwable ex) {
        }
    }
    public static String getDesStr() {
            return getStringFromNative();
    }
    public static String getDesKeyStr() {//33FE2C7EFA44dsF206E48froED20416C727B6m_forF206E48dwa416C727rding
            return getDesPswStringFromNative();
    }
    public static String getKeyStr() {
            return getKeyStringFromNative();//M5zv9tP+XRCiyOhuhpVNbQ==
    }
    public static native String getStringFromNative();
    public static native String getKeyStringFromNative();//M5zv9tP+XRCiyOhuhpVNbQ==
    public static native String getDesPswStringFromNative();//33FE2C7EFA44dsF206E48froED20416C727B6m_forF206E48dwa416C727rding
}
