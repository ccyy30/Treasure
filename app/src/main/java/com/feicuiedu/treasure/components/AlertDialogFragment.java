package com.feicuiedu.treasure.components;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.feicuiedu.treasure.R;

/**
 * 作者：yuanchao on 2016/7/13 0013 11:19
 * 邮箱：yuanchao@feicuiedu.com
 */
public class AlertDialogFragment extends DialogFragment {

    private static final String KEY_TITLE = "title";
    private static final String KEY_MESSAGE = "message";

    /**
     * 创建当前AlertDialogFragment的一个实例
     *
     * @param resTile 标题的字符串资源id
     * @param msg     描述信息
     * @return AlertDialogFragment对象
     */
    public static AlertDialogFragment newInstance(int resTile, String msg) {
        AlertDialogFragment fragment = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_TITLE, resTile);
        args.putString(KEY_MESSAGE, msg);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt(KEY_TITLE);
        String msg = getArguments().getString(KEY_MESSAGE);
        return new AlertDialog.Builder(getActivity(), getTheme())
                .setTitle(title) // 设置title
                .setMessage(msg) // 设置message
                .setNeutralButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }
}
