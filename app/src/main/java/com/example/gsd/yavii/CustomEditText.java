package com.example.gsd.yavii;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.gsd.yavii.utils.GetIp;

/**
 * Created by Administrator on 2017/2/26.
 */

public class CustomEditText extends EditText implements View.OnFocusChangeListener{
   public Drawable drawable;
public Context context;
    public CustomEditText(Context context) {
        this(context, null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();
    }

    private void init() {
        drawable=getCompoundDrawables()[2];

    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {


                    GetIp getIp=new GetIp();
                    this.setText(getIp.getIp(context));
                    try {

                    } catch (Exception e) {

                    }

                }
            }
        }
        return super.onTouchEvent(event);
    }
}
