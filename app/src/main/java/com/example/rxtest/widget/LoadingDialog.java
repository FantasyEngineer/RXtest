package com.example.rxtest.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rxtest.R;

/**
 * Created by Administrator on 2018/8/27.
 */

public class LoadingDialog extends Dialog {

    private Context context;

    public LoadingDialog(Context context) {
        super(context);
        init(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }


    private void init(Context context) {
        this.context = context;
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.setBackgroundColor(context.getColor(android.R.color.transparent));
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(dp2px(60), dp2px(60)));
        linearLayout.addView(imageView);

        TextView textView = new TextView(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText("加载中...");
        textView.setTextColor(context.getColor(android.R.color.black));

        linearLayout.addView(textView);

//        View view = getLayoutInflater().inflate(R.layout.progress_custom, null);
//        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(getContext()).asGif().load(R.mipmap.loading_new).into(imageView);
        setContentView(linearLayout);
        // 设置居中
        getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.0f;
        getWindow().setAttributes(lp);
    }


//    /**
//     * 当窗口焦点改变时调用
//     */
//    public void onWindowFocusChanged(boolean hasFocus) {
//
////        // 获取ImageView上的动画背景
////        AnimationDrawable spinner = (AnimationDrawable) imageView.getDrawable();
////        // 开始动画
////        spinner.start();
//    }

//    /**
//     * 给Dialog设置提示信息
//     *
//     * @param message
//     */
//    public void setMessage(CharSequence message) {
//        if (message != null && message.length() > 0) {
//            findViewById(R.id.message).setVisibility(View.VISIBLE);
//            TextView txt = (TextView) findViewById(R.id.message);
//            txt.setText(message);
//            txt.invalidate();
//        }
//    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param context        上下文
     * @param message        提示
     * @param cancelable     是否按返回键取消
     * @param outside        是否点击外部消失
     * @param cancelListener 按下返回键监听
     * @return
     */
    public static LoadingDialog show(Context context, CharSequence message, boolean cancelable, boolean outside, OnCancelListener cancelListener) {
        LoadingDialog dialog = new LoadingDialog(context, R.style.CustomProgressDialog);
        dialog.setContentView(R.layout.progress_custom);
        dialog.setTitle("");
        if (message == null || message.length() == 0) {
            dialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) dialog.findViewById(R.id.message);
            txt.setText(message);
        }
        // 按返回键是否取消
        dialog.setCancelable(cancelable);
        // 监听返回键处理
        dialog.setOnCancelListener(cancelListener);
        dialog.setCanceledOnTouchOutside(outside);
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.4f;
        dialog.getWindow().setAttributes(lp);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
        return dialog;
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    private int dp2px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


}
