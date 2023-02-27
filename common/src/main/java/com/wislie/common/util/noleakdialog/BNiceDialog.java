package com.wislie.common.util.noleakdialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.shehuan.nicedialog.R.style;
import com.shehuan.nicedialog.Utils;
import com.shehuan.nicedialog.ViewHolder;
public abstract class BNiceDialog extends DialogFragment {
    private static final String MARGIN = "margin";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String DIM = "dim_amount";
    private static final String GRAVITY = "gravity";
    private static final String CANCEL = "out_cancel";
    private static final String THEME = "theme";
    private static final String ANIM = "anim_style";
    private static final String LAYOUT = "layout_id";
    private int margin;
    private int width;
    private int height;
    private float dimAmount = 0.5F;
    private int gravity = 17;
    private boolean outCancel = true;
    @StyleRes
    protected int theme;
    @StyleRes
    private int animStyle;
    @LayoutRes
    protected int layoutId;

    public BNiceDialog() {
        this.theme = style.NiceDialogStyle;
    }

    public abstract int intLayoutId();

    public abstract void convertView(ViewHolder var1, BNiceDialog var2);

    public int initTheme() {
        return this.theme;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setStyle(STYLE_NO_TITLE, this.initTheme());
        if (savedInstanceState != null) {
            this.margin = savedInstanceState.getInt("margin");
            this.width = savedInstanceState.getInt("width");
            this.height = savedInstanceState.getInt("height");
            this.dimAmount = savedInstanceState.getFloat("dim_amount");
            this.gravity = savedInstanceState.getInt("gravity");
            this.outCancel = savedInstanceState.getBoolean("out_cancel");
            this.theme = savedInstanceState.getInt("theme");
            this.animStyle = savedInstanceState.getInt("anim_style");
            this.layoutId = savedInstanceState.getInt("layout_id");
        }

    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.layoutId = this.intLayoutId();
        View view = inflater.inflate(this.layoutId, container, false);
        this.convertView(ViewHolder.create(view), this);
        return view;
    }

    public void onStart() {
        super.onStart();
        this.initParams();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("margin", this.margin);
        outState.putInt("width", this.width);
        outState.putInt("height", this.height);
        outState.putFloat("dim_amount", this.dimAmount);
        outState.putInt("gravity", this.gravity);
        outState.putBoolean("out_cancel", this.outCancel);
        outState.putInt("theme", this.theme);
        outState.putInt("anim_style", this.animStyle);
        outState.putInt("layout_id", this.layoutId);
    }

    @SuppressLint("WrongConstant")
    private void initParams() {
        Window window = this.getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = this.dimAmount;
            if (this.gravity != 0) {
                lp.gravity = this.gravity;
            }

            switch(this.gravity) {
                case 3:
                case 51:
                case 83:
                    if (this.animStyle == 0) {
                        this.animStyle = style.LeftAnimation;
                    }
                    break;
                case 5:
                case 53:
                case 85:
                    if (this.animStyle == 0) {
                        this.animStyle = style.RightAnimation;
                    }
                    break;
                case 48:
                    if (this.animStyle == 0) {
                        this.animStyle = style.TopAnimation;
                    }
                    break;
                case 80:
                    if (this.animStyle == 0) {
                        this.animStyle = style.BottomAnimation;
                    }
            }

            if (this.width == 0) {
                lp.width = Utils.getScreenWidth(this.getContext()) - 2 * Utils.dp2px(this.getContext(), (float)this.margin);
            } else if (this.width == -1) {
                lp.width = -2;
            } else {
                lp.width = Utils.dp2px(this.getContext(), (float)this.width);
            }

            if (this.height == 0) {
                lp.height = -2;
            } else {
                lp.height = Utils.dp2px(this.getContext(), (float)this.height);
            }

            window.setWindowAnimations(this.animStyle);
            window.setAttributes(lp);
        }

        this.setCancelable(this.outCancel);
    }

    public BNiceDialog setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    public BNiceDialog setWidth(int width) {
        this.width = width;
        return this;
    }

    public BNiceDialog setHeight(int height) {
        this.height = height;
        return this;
    }

    public BNiceDialog setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    public BNiceDialog setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public BNiceDialog setOutCancel(boolean outCancel) {
        this.outCancel = outCancel;
        return this;
    }

    public BNiceDialog setAnimStyle(@StyleRes int animStyle) {
        this.animStyle = animStyle;
        return this;
    }

    public BNiceDialog show(FragmentManager manager) {
        FragmentTransaction ft = manager.beginTransaction();
        if (this.isAdded()) {
            ft.remove(this).commit();
        }

        ft.add(this, String.valueOf(System.currentTimeMillis()));
        ft.commitAllowingStateLoss();
        return this;
    }
}

