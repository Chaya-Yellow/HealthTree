package com.jks.Spo2MonitorEx.util.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jks.Spo2MonitorEx.R;

/**
 * Created by apple on 16/8/30.
 */
public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);

    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public void setMessage(String message) {
        CustomDialog.Builder.updateMessage(message);
    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {

        private Context context;
        private String title;
        private String message;
        private String content;
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

        }

        /**
         * 对话框的信息
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 通过资源文件id设置对话框内容
         * @param content
         * @return
         */
        public Builder setContent(int content) {
            this.content = (String) context.getText(content);
            return this;
        }

        /**
         * Set the Dialog content from String
         * @param content
         * @return
         */
        public Builder setcontente(String content) {
            this.content = content;
            return this;
        }

        /**
         * Set the Dialog message from resource
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
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

        /**
         * Create the custom dialog
         */
        public CustomDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context, R.style.dialog);
            layout = inflater.inflate(R.layout.mydialog, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title

            if (title != null) {
                ((TextView) layout.findViewById(R.id.title)).setText(Html.fromHtml(title));
                if (content != null) {
                    ((TextView) layout.findViewById(R.id.content)).setText(Html.fromHtml(content));
                } else {
                    ((TextView) layout.findViewById(R.id.content)).setVisibility(View.GONE);
                }

                if (message != null) {
                    ((TextView) layout.findViewById(R.id.message)).setText(Html.fromHtml(message));
                } else {
                    ((TextView) layout.findViewById(R.id.message)).setVisibility(View.GONE);
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.topPanel).setVisibility(View.GONE);
            }

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
            // set the content message
            if (message != null) {
                // Log.v("test","_message:"+message);
                // Log.v("test","_Html.fromHtml(message):"+Html.fromHtml(message));
                // ((TextView)
                // layout.findViewById(R.id.message)).setText(Html.fromHtml(message));
            } else {
                // layout.findViewById(R.id.content).setVisibility(View.GONE);
            }
            if (view != null) {
                // if no message set
                // add the contentView to the dialog body
                layout.findViewById(R.id.custom).setBackgroundDrawable(null);
                ((LinearLayout) layout.findViewById(R.id.custom)).removeAllViews();
                textView = (TextView) layout.findViewById(R.id.showline);
                LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.FILL_PARENT);
                mLayoutParams.leftMargin = 20;
                mLayoutParams.rightMargin = 20;
                ((LinearLayout) layout.findViewById(R.id.custom)).addView(view, mLayoutParams);
                if (isShowLine == false) {
                    textView.setVisibility(View.GONE);
                }
            } else {
                layout.findViewById(R.id.custom).setVisibility(View.GONE);
            }
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(true);
            return dialog;
        }

        public CustomDialog show() {
            CustomDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

    // public static CustomDialog getCustomDialog(Activity activity,int
    // title,int content,int leftBtn,int
    // rightBtn,DialogInterface.OnClickListener
    // leftListener,DialogInterface.OnClickListener rightListener) {
    // CustomDialog.Builder builder = new CustomDialog.Builder(activity);
    // builder.setTitle(R.string.dialog_prompt_net_error);
    // builder.setMessage("");
    // builder.setPositiveButton(R.string.dialog_common_btn_cancel, new
    // DialogInterface.OnClickListener() {
    //
    // @Override
    // public void onClick(DialogInterface dialog, int which) {
    //
    //
    // }
    // });
    // builder.setNegativeButton(R.string.dialog_common_btn_again, new
    // DialogInterface.OnClickListener() {
    //
    // @Override
    // public void onClick(DialogInterface dialog, int which) {
    //
    //
    // }
    // });
    // return builder.create();
    // }
    // 只是一个模板。。。用时就复制
    public static CustomDialog getCustomDialog(Activity activity, String title, String content, String leftBtn, String rightBtn,
                                               DialogInterface.OnClickListener leftListener, DialogInterface.OnClickListener rightListener) {
        CustomDialog.Builder builder = new CustomDialog.Builder(activity);
        // builder.setTitle(R.string.prompt_net_error);
        // builder.setMessage("");
        // builder.setPositiveButton(R.string.common_btn_cancel, new
        // DialogInterface.OnClickListener() {
        //
        // @Override
        // public void onClick(DialogInterface dialog, int which) {
        //
        //
        // }
        // });
        // builder.setNegativeButton(R.string.common_btn_again, new
        // DialogInterface.OnClickListener() {
        //
        // @Override
        // public void onClick(DialogInterface dialog, int which) {
        //
        //
        // }
        // });
        return builder.create();
    }

}