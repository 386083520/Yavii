package com.example.gsd.yavii.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/10/31.
 */

public class Utils {
    private static Toast toast;

    public static void showToast(Context context,
                                 String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

}
