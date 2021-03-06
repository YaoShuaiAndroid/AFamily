package com.family.afamily.utils;

import android.os.Handler;
import android.widget.TextView;

/**
 * ================================================================
 * 创建时间：2017/7/27 下午2:17
 * 创建人：Alan
 * 文件描述：倒计时工具类
 * 至尊宝：长夜漫漫无心睡眠，我以为只有我睡不着，原来晶晶姑娘你也睡不着 ！
 * ================================================================
 */
public class Countdown implements Runnable {
    private int remainingSeconds;
    private int currentRemainingSeconds;
    private boolean running;
    private String defaultText;
    private String countdownText;
    private TextView showTextView;
    private Handler handler;
    private CountdownListener countdownListener;
    private TextViewGetListener textViewGetListener;

    /**
     * 创建一个倒计时器
     *
     * @param showTextView     显示倒计时的文本视图
     * @param countdownText    倒计时中显示的内容，例如："%s秒后重新获取验证码"，在倒计时的过程中会用剩余描述替换%s
     * @param remainingSeconds 倒计时秒数，例如：60，就是从60开始倒计时一直到0结束
     */
    public Countdown(TextView showTextView, String countdownText, int remainingSeconds) {
        this.showTextView = showTextView;
        this.countdownText = countdownText;
        this.remainingSeconds = remainingSeconds;
        this.handler = new Handler();
    }

    /**
     * 创建一个倒计时器
     *
     * @param textViewGetListener 显示倒计时的文本视图获取监听器
     * @param countdownText       倒计时中显示的内容，例如："%s秒后重新获取验证码"，在倒计时的过程中会用剩余描述替换%s
     * @param remainingSeconds    倒计时秒数，例如：60，就是从60开始倒计时一直到0结束
     */
    public Countdown(TextViewGetListener textViewGetListener, String countdownText, int remainingSeconds) {
        this.textViewGetListener = textViewGetListener;
        this.countdownText = countdownText;
        this.remainingSeconds = remainingSeconds;
        this.handler = new Handler();
    }

    /**
     * 创建一个倒计时器，默认60秒
     *
     * @param showTextView  显示倒计时的文本视图
     * @param countdownText 倒计时中显示的内容，例如："%s秒后重新获取验证码"，在倒计时的过程中会用剩余描述替换%s
     */
    public Countdown(TextView showTextView, String countdownText) {
        this(showTextView, countdownText, 60);
    }

    /**
     * 创建一个倒计时器，默认60秒
     *
     * @param textViewGetListener 显示倒计时的文本视图获取监听器
     * @param countdownText       倒计时中显示的内容，例如："%s秒后重新获取验证码"，在倒计时的过程中会用剩余描述替换%s
     */
    public Countdown(TextViewGetListener textViewGetListener, String countdownText) {
        this(textViewGetListener, countdownText, 60);
    }

    private TextView getShowTextView() {
        if (showTextView != null) {
            return showTextView;
        }

        if (textViewGetListener != null) {
            return textViewGetListener.OnGetShowTextView();
        }

        return null;
    }

    @Override
    public void run() {
        if (currentRemainingSeconds > 0) {
            getShowTextView().setEnabled(false);
            getShowTextView().setText(
                    String.format(countdownText, currentRemainingSeconds));
            if (countdownListener != null) {
                countdownListener.onUpdate(currentRemainingSeconds);
            }
            currentRemainingSeconds--;
            handler.postDelayed(this, 1000);
        } else {
            stop();
        }
    }

    public void start() {
        defaultText = (String) getShowTextView().getText();
        currentRemainingSeconds = remainingSeconds;
        handler.removeCallbacks(this);
        handler.post(this);
        if (countdownListener != null) {
            countdownListener.onStart();
        }
        running = true;
    }

    public void stop() {
        getShowTextView().setEnabled(true);
        getShowTextView().setText(defaultText);
        handler.removeCallbacks(this);
        if (countdownListener != null) {
            countdownListener.onFinish();
        }
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public String getCountdownText() {
        return countdownText;
    }

    public void setCountdownText(String countdownText) {
        this.countdownText = countdownText;
    }

    public void setCurrentRemainingSeconds(int currentRemainingSeconds) {
        this.currentRemainingSeconds = currentRemainingSeconds;
    }

    public void setCountdownListener(CountdownListener countdownListener) {
        this.countdownListener = countdownListener;
    }

    /**
     * 倒计时监听器
     */
    public interface CountdownListener {
        /**
         * 当倒计时开始
         */
        public void onStart();

        /**
         * 当倒计时结束
         */
        public void onFinish();

        /**
         * 更新
         *
         * @param currentRemainingSeconds 剩余时间
         */
        public void onUpdate(int currentRemainingSeconds);
    }

    public interface TextViewGetListener {
        public TextView OnGetShowTextView();
    }
}