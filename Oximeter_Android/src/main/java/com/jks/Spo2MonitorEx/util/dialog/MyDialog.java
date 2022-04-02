package com.jks.Spo2MonitorEx.util.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jks.Spo2MonitorEx.R;

/**
 * Created by apple on 16/8/23.
 */
public class MyDialog extends Dialog {
    public MyDialog(Context context) {
        super(context);

    }

    public MyDialog(Context context, int theme) {
        super(context, theme);
    }

    public void setMessage(String message) {
        MyDialog.Builder.updateMessage(message);
    }

    /**
     * Create the custom dialog
     */
    public void updateTitleData(String titleData) {
        if (titleData != null) {
            ((TextView) MyDialog.Builder.layout.findViewById(R.id.title_data)).setText(titleData);
        }
    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {

        private Context context;
        private String title;
        private String myViewTitle;// 用于自定义View时的标题
        private String message;
        private String detail;
        private String positiveButtonText;
        private String negativeButtonText;
        private View view;
        private Boolean isShowLine;
        private TextView textView;
        private static View layout;
        private DialogInterface.OnClickListener positiveButtonClickListener, negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public static void updateMessage(String message) {
            // ((TextView)
            // layout.findViewById(R.id.message)).setText(Html.fromHtml(message));
        }

        /**
         * Set the Dialog message from String
         *
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog detail from String
         *
         * @param detail
         * @return
         */
        public Builder setDetail(String detail) {
            this.detail = detail;
            return this;
        }

        /**
         * Set the Dialog detail from resource
         *
         * @param detail
         * @return
         */
        public Builder setDetail(int detail) {
            this.detail = (String) context.getText(detail);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * 设置自定义View时候的标题
         *
         * @param myViewTitle
         * @return
         */
        public Builder setMyViewTitle(String myViewTitle) {
            this.myViewTitle = myViewTitle;
            return this;
        }

        public Builder setMyViewTitle(int myViewTitle) {
            this.myViewTitle = (String) context.getText(myViewTitle);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Set a custom content view for the Dialog. If a message is set, the
         * contentView is not added to the Dialog...
         *
         * @param v
         * @return
         */
        public Builder setView(View v) {
            this.view = v;
            return this;
        }

        public Builder isShowLine(Boolean isShowLine) {
            this.isShowLine = isShowLine;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the positive button text and it's listener
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button resource and it's listener
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button text and it's listener
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }



        public MyDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            // instantiate the dialog with the custom Theme
            final MyDialog dialog = new MyDialog(context, R.style.myDialog);
            layout = inflater.inflate(R.layout.my_dialog, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            if (title != null) {
                ((TextView) layout.findViewById(R.id.title)).setText(title);
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.title).setVisibility(View.GONE);
            }
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            } else {
                ((TextView) layout.findViewById(R.id.message)).setVisibility(View.GONE);
            }
            if (detail != null) {
                ((TextView) layout.findViewById(R.id.detail)).setText(detail);
            } else {
                ((TextView) layout.findViewById(R.id.detail)).setVisibility(View.GONE);
            }
            // 设置自定义View 时候的title
            if (myViewTitle != null) {
                ((TextView) layout.findViewById(R.id.custom_title)).setText(myViewTitle);
            } else {
                ((TextView) layout.findViewById(R.id.custom_title)).setVisibility(View.GONE);
            }
            // 自定义VIEW
            if (view != null) {
                layout.findViewById(R.id.topPanel).setVisibility(View.GONE);// 当VIEW不为空，就把TITLE和MSG容器隐藏
                layout.findViewById(R.id.customPanel).setVisibility(View.VISIBLE);

                // if no message set
                // add the contentView to the dialog body

                layout.findViewById(R.id.custom).setBackgroundDrawable(null);
                ((LinearLayout) layout.findViewById(R.id.custom)).removeAllViews();
                textView = (TextView) layout.findViewById(R.id.showline);
                LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.FILL_PARENT);
                //mLayoutParams.leftMargin = 20;
                //mLayoutParams.rightMargin = 20;
                ((LinearLayout) layout.findViewById(R.id.custom)).addView(view, mLayoutParams);
                if (isShowLine == false) {
                    textView.setVisibility(View.GONE);
                }
            } else {
                layout.findViewById(R.id.customPanel).setVisibility(View.GONE);
            }
            // 按钮
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.positiveButton)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButton)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(View.GONE);
            }
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(true);
            return dialog;
        }

        public MyDialog show() {
            MyDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

    // 只是一个模板。。。用时就复制
    public static MyDialog getCustomDialog(Activity activity, String title, String content, String leftBtn, String rightBtn,
                                           DialogInterface.OnClickListener leftListener, DialogInterface.OnClickListener rightListener) {
        MyDialog.Builder builder = new MyDialog.Builder(activity);
        return builder.create();
    }
}
