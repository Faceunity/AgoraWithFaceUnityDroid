package com.faceunity.agorawithfaceunity.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faceunity.agorawithfaceunity.Effect;
import com.faceunity.agorawithfaceunity.Filter;
import com.faceunity.agorawithfaceunity.FuJsonHelper;
import com.faceunity.agorawithfaceunity.R;
import com.faceunity.agorawithfaceunity.dialog.BaseDialogFragment;
import com.faceunity.agorawithfaceunity.dialog.ConfirmDialogFragment;
import com.faceunity.agorawithfaceunity.view.beautybox.BeautyBox;
import com.faceunity.agorawithfaceunity.view.beautybox.BeautyBoxGroup;
import com.faceunity.agorawithfaceunity.view.beautybox.DetectBox;
import com.faceunity.agorawithfaceunity.view.seekbar.DiscreteSeekBar;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.Arrays;
import java.util.List;

/**
 * @author Qinyu on 2021-03-30
 * @description
 */
@SuppressLint("NonConstantResourceId")
public class BottomNavigationView extends LinearLayout implements CheckGroup.OnCheckedChangeListener
        , BeautyBoxGroup.OnCheckedChangeListener, DiscreteSeekBar.OnProgressChangeListener, DetectBox.OnCheckStateListener, CompoundButton.OnCheckedChangeListener {
    private static final int ID_SELECT_FILTER = -100;
    private DiscreteSeekBar beauty_seek_bar;
    private FrameLayout fl_face, fl_shape;
    private LinearLayout fl_detect;
    private ConstraintLayout cl_bottom_view;
    private RecyclerView rv_bottom;
    private BeautyBoxGroup cg_shape, cg_face;

    private FilterRecyclerAdapter filterAdapter;
    private EffectRecyclerAdapter stickerAdapter, segAdapter;
    private int mBeautyBoxSelId = View.NO_ID;
    private final FuJsonHelper mFuJsonHelper = FuJsonHelper.getInstance();

    public BottomNavigationView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_bottom_navigation, this);
        beauty_seek_bar = findViewById(R.id.beauty_seek_bar);
        rv_bottom = findViewById(R.id.rv_bottom);
        fl_face = findViewById(R.id.fl_face_skin_items);
        fl_shape = findViewById(R.id.fl_face_shape_items);
        fl_detect = findViewById(R.id.fl_detect_items);
        cl_bottom_view = findViewById(R.id.cl_bottom_view);
        cg_face = findViewById(R.id.beauty_group_skin_beauty);
        cg_shape = findViewById(R.id.beauty_group_face_shape);
        ImageView iv_recover_skin = findViewById(R.id.iv_recover_face_skin);
        ImageView iv_recover_shape = findViewById(R.id.iv_recover_face_shape);

        iv_recover_shape.setOnClickListener(v -> {
            ConfirmDialogFragment confirmDialogFragment = ConfirmDialogFragment.newInstance(getContext().getString(R.string.dialog_reset_avatar_model), new BaseDialogFragment.OnClickListener() {
                @Override
                public void onConfirm() {
                    mFuJsonHelper.resetShape();
                    long start = System.nanoTime();
                    for (int i = 0; i < cg_shape.getChildCount(); i++) {
                        onCheckedChanged(cg_shape, cg_shape.getChildAt(i).getId());
                    }
                    Log.e("TAG", "cost " + (System.nanoTime() - start));
                    if (cg_shape.getCheckedBeautyBoxId() != View.NO_ID) {
                        onCheckedChanged(cg_shape, cg_shape.getCheckedBeautyBoxId());
                    }
                }

                @Override
                public void onCancel() {}
            });
            confirmDialogFragment.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "ConfirmDialogFragmentReset");
        });
        iv_recover_skin.setOnClickListener(v -> {
            ConfirmDialogFragment confirmDialogFragment = ConfirmDialogFragment.newInstance(getContext().getString(R.string.dialog_reset_avatar_model), new BaseDialogFragment.OnClickListener() {
                @Override
                public void onConfirm() {
                    mFuJsonHelper.resetFace();
                    for (int i = 0; i < cg_face.getChildCount(); i++) {
                        onCheckedChanged(cg_face, cg_face.getChildAt(i).getId());
                    }
                    if (cg_face.getCheckedBeautyBoxId() != View.NO_ID) {
                        onCheckedChanged(cg_face, cg_face.getCheckedBeautyBoxId());
                    }
                }

                @Override
                public void onCancel() {}
            });
            confirmDialogFragment.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "ConfirmDialogFragmentReset");
        });
        beauty_seek_bar.setOnProgressChangeListener(this);
        CheckGroup checkGroup = findViewById(R.id.cg_bottom);
        checkGroup.setOnCheckedChangeListener(this);
        cg_face.setOnCheckedChangeListener(this);
        cg_shape.setOnCheckedChangeListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rv_bottom.setLayoutManager(linearLayoutManager);
        filterAdapter = new FilterRecyclerAdapter(Filter.getFilers());
        stickerAdapter = new EffectRecyclerAdapter(Effect.EffectEnum.getEffectsByKey("stickers"));
        segAdapter = new EffectRecyclerAdapter(Effect.EffectEnum.getEffectsByKey("PortraitSegmentation"));
        ((DetectBox) findViewById(R.id.detect_box_face)).setCheckStateListener(this);
//        ((DetectBox) findViewById(R.id.detect_box_emotion)).setCheckStateListener(this);
        ((DetectBox) findViewById(R.id.detect_box_expression)).setCheckStateListener(this);
        ((DetectBox) findViewById(R.id.detect_box_gesture)).setCheckStateListener(this);
        ((DetectBox) findViewById(R.id.detect_box_human)).setCheckStateListener(this);
    }

    @Override
    public void onCheckStateChange(DetectBox view, boolean checked) {
        switch (view.getId()) {
            case R.id.detect_box_face:
                mFuJsonHelper.putAndSet(checked ? 0 : -1, "FaceDetection");
                break;
//            case R.id.detect_box_emotion:
//                mFuJsonHelper.putAndSet(checked ? 0 : -1, "EmotionRecognition");
//                break;
            case R.id.detect_box_expression:
                mFuJsonHelper.putAndSet(checked ? 0 : -1, "ExpressionRecognition");
                break;
            case R.id.detect_box_human:
                SwitchButton sw = ((ViewGroup) getParent()).findViewById(R.id.switch_btn_body);
                if (checked) {
                    sw.setVisibility(VISIBLE);
                    mFuJsonHelper.putAndSet(sw.isChecked() ?  1 : 0, "BodyDetection");
                } else {
                    sw.setVisibility(GONE);
                    mFuJsonHelper.putAndSet(-1, "BodyDetection");
                }
                break;
            case R.id.detect_box_gesture:
                mFuJsonHelper.putAndSet(checked ? 0 : -1, "GestureRecognition");
                break;
            default:
        }
    }

    @Override
    public void onCheckedChanged(CheckGroup group, int checkedId) {
        cl_bottom_view.setVisibility(GONE);
        rv_bottom.setVisibility(GONE);
        fl_shape.setVisibility(GONE);
        fl_face.setVisibility(GONE);
        fl_detect.setVisibility(GONE);
        beauty_seek_bar.setVisibility(GONE);
        mBeautyBoxSelId = View.NO_ID;
        switch (checkedId) {
            case R.id.cb_detect:
                cl_bottom_view.setVisibility(VISIBLE);
                fl_detect.setVisibility(VISIBLE);
                break;
            case R.id.cb_skin:
                if (cg_face.getCheckedBeautyBoxId() != View.NO_ID) {
                    onCheckedChanged(cg_face, cg_face.getCheckedBeautyBoxId());
                }
                cl_bottom_view.setVisibility(VISIBLE);
                fl_face.setVisibility(VISIBLE);
                break;
            case R.id.cb_shape:
                if (cg_shape.getCheckedBeautyBoxId() != View.NO_ID) {
                    onCheckedChanged(cg_shape, cg_shape.getCheckedBeautyBoxId());
                }
                cl_bottom_view.setVisibility(VISIBLE);
                fl_shape.setVisibility(VISIBLE);
                break;
            case R.id.cb_filter:
                rv_bottom.setVisibility(VISIBLE);
                mBeautyBoxSelId = ID_SELECT_FILTER;
                rv_bottom.setAdapter(filterAdapter);
                beauty_seek_bar.setMin(0);
                beauty_seek_bar.setMax(100);
                if (filterAdapter.mPosSelected != 0) {
                    beauty_seek_bar.setProgress(filterAdapter.getFilterLevel());
                    beauty_seek_bar.setVisibility(VISIBLE);
                }
                break;
            case R.id.cb_effect:
                rv_bottom.setVisibility(VISIBLE);
                rv_bottom.setAdapter(stickerAdapter);
                break;
            case R.id.cb_segment:
                rv_bottom.setVisibility(VISIBLE);
                rv_bottom.setAdapter(segAdapter);
                break;
            default:
        }
    }

    @Override
    public void onCheckedChanged(BeautyBoxGroup group, int checkedId) {
        beauty_seek_bar.setVisibility(VISIBLE);
        mBeautyBoxSelId = checkedId;
        int value = mFuJsonHelper.getBeautyFaceValue(getKeyByViewID(checkedId));
        if (value < 0) {
            value = mFuJsonHelper.getBeautyShapeValue(getKeyByViewID(checkedId));
        }
        if (checkedId == R.id.beauty_box_intensity_chin || checkedId == R.id.beauty_box_intensity_forehead
                || checkedId == R.id.beauty_box_intensity_mouth || checkedId == R.id.beauty_box_long_nose
                || checkedId == R.id.beauty_box_eye_space || checkedId == R.id.beauty_box_eye_rotate
                || checkedId == R.id.beauty_box_philtrum) {
            beauty_seek_bar.setMin(-50);
            beauty_seek_bar.setMax(50);
            value -= 50;
        } else {
            beauty_seek_bar.setMin(0);
            beauty_seek_bar.setMax(100);
        }
        beauty_seek_bar.setProgress(value);
    }

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int progress, boolean fromUser) {
        if (mBeautyBoxSelId != View.NO_ID && mBeautyBoxSelId != ID_SELECT_FILTER) {
            ((BeautyBox) findViewById(mBeautyBoxSelId)).setOpen(progress != 0);
        }
        if (mBeautyBoxSelId == R.id.beauty_box_intensity_chin || mBeautyBoxSelId == R.id.beauty_box_intensity_forehead
                || mBeautyBoxSelId == R.id.beauty_box_intensity_mouth || mBeautyBoxSelId == R.id.beauty_box_long_nose
                || mBeautyBoxSelId == R.id.beauty_box_eye_space || mBeautyBoxSelId == R.id.beauty_box_eye_rotate
                || mBeautyBoxSelId == R.id.beauty_box_philtrum) {
            progress += 50;
        }
        switch (mBeautyBoxSelId) {
            case R.id.beauty_box_blur_level:
            case R.id.beauty_box_color_level:
            case R.id.beauty_box_red_level:
            case R.id.beauty_box_sharpen:
            case R.id.beauty_box_eye_bright:
            case R.id.beauty_box_tooth_whiten:
            case R.id.beauty_box_pouch:
            case R.id.beauty_box_nasolabial:
                mFuJsonHelper.setBeautyFace(progress, getKeyByViewID(mBeautyBoxSelId));
                break;
            case R.id.beauty_box_cheek_narrow:
            case R.id.beauty_box_cheek_small:
            case R.id.beauty_box_cheek_v:
            case R.id.beauty_box_cheek_thinning:
            case R.id.beauty_box_cheekbones:
            case R.id.beauty_box_lower_jaw:
            case R.id.beauty_box_eye_enlarge:
            case R.id.beauty_box_eye_circle:
            case R.id.beauty_box_intensity_chin:
            case R.id.beauty_box_intensity_forehead:
            case R.id.beauty_box_intensity_nose:
            case R.id.beauty_box_intensity_mouth:
            case R.id.beauty_box_canthus:
            case R.id.beauty_box_eye_space:
            case R.id.beauty_box_eye_rotate:
            case R.id.beauty_box_long_nose:
            case R.id.beauty_box_philtrum:
            case R.id.beauty_box_smile:
                mFuJsonHelper.setBeautyShape(progress, getKeyByViewID(mBeautyBoxSelId));
                break;
            case ID_SELECT_FILTER: //选择滤镜
                Effect effect = filterAdapter.getCurSelected();
                mFuJsonHelper.setParams("filter", new Object[]{effect.getJsonValue(), progress}, new String[]{"type", "level"});
                filterAdapter.setFilterLevels(progress);
                break;
            default:
        }
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {}

    /**
     * 外部 Switch
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mFuJsonHelper.putAndSet(isChecked ? 1 : 0, "BodyDetection");
    }

    class FilterRecyclerAdapter extends RecyclerView.Adapter<EffectRecyclerHolder> {
        private final List<Effect> mFilters;
        private int mPosSelected = 0;
        private int[] filterLevels;

        public FilterRecyclerAdapter(List<Effect> filters) {
            mFilters = filters;
            filterLevels = new int[filters.size()];
            Arrays.fill(filterLevels, 40);
        }

        @Override
        public EffectRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new EffectRecyclerHolder(LayoutInflater.from(getContext())
                    .inflate(R.layout.item_filter_recycler, parent, false));
        }

        @Override
        public void onBindViewHolder(EffectRecyclerHolder holder, final int position) {
            Effect filter = mFilters.get(position);
            holder.iv_icon.setImageResource(filter.getIconId());
            holder.tv_name.setText(filter.getName());
            if (mPosSelected == position) {
                holder.iv_icon.setBackgroundResource(R.drawable.shape_filter_selected);
                holder.tv_name.setSelected(true);
            } else {
                holder.iv_icon.setBackgroundResource(R.color.transparent);
                holder.tv_name.setSelected(false);
            }
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPosSelected = position;
                    notifyDataSetChanged();
                    if (position == 0) {
                        beauty_seek_bar.setVisibility(GONE);
                        beauty_seek_bar.setProgress(0);
                    } else {
                        beauty_seek_bar.setVisibility(VISIBLE);
                        beauty_seek_bar.setProgress(filterLevels[position]);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mFilters.size();
        }

        public Effect getCurSelected() {
            return mFilters.get(mPosSelected);
        }

        public int getFilterLevel() {
            return filterLevels[mPosSelected];
        }

        public void setFilterLevels(int filterLevel) {
            this.filterLevels[mPosSelected] = filterLevel;
        }
    }

    class EffectRecyclerAdapter extends RecyclerView.Adapter<EffectRecyclerHolder> {
        private final List<Effect> mEffects;
        private int mPosSelected = 0;

        public EffectRecyclerAdapter(List<Effect> effects) {
            mEffects = effects;
        }

        @NonNull
        @Override
        public EffectRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new EffectRecyclerHolder(LayoutInflater.from(getContext())
                    .inflate(R.layout.item_effect_recycler, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull EffectRecyclerHolder holder, int position) {
            Effect effect = mEffects.get(position);
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPosSelected == position) {
                        return;
                    }
                    mPosSelected = position;
                    notifyDataSetChanged();
                    mFuJsonHelper.putAndSet(effect.getJsonValue(), effect.getJsonKey());
                }
            });
            holder.iv_icon.setImageResource(effect.getIconId());
            if (position == mPosSelected) {
                holder.iv_icon.setBackgroundResource(R.drawable.shapre_effect_select);
            } else {
                holder.iv_icon.setBackgroundResource(R.color.transparent);
            }
        }

        @Override
        public int getItemCount() {
            return mEffects.size();
        }
    }

    static class EffectRecyclerHolder extends RecyclerView.ViewHolder {
        ImageView iv_icon;
        TextView tv_name; // maybe null

        public EffectRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.control_recycler_img);
            try {
                tv_name = itemView.findViewById(R.id.control_recycler_text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getKeyByViewID(@IdRes int id) {
        switch (id) {
            case R.id.beauty_box_blur_level:
                return "skin";
            case R.id.beauty_box_color_level:
                return "white";
            case R.id.beauty_box_red_level:
                return "red";
            case R.id.beauty_box_sharpen:
                return "sharpen";
            case R.id.beauty_box_eye_bright:
                return "lighteye";
            case R.id.beauty_box_tooth_whiten:
                return "teeth";
            case R.id.beauty_box_pouch:
                return "blackeye";
            case R.id.beauty_box_nasolabial:
                return "ade";
            case R.id.beauty_box_cheek_narrow:
                return "naface";
            case R.id.beauty_box_cheek_small:
                return "smallface";
            case R.id.beauty_box_cheek_v:
                return "vface";
            case R.id.beauty_box_cheek_thinning:
                return "thinface";
            case R.id.beauty_box_cheekbones:
                return "thincheekbone";
            case R.id.beauty_box_lower_jaw:
                return "thinmandible";
            case R.id.beauty_box_eye_enlarge:
                return "bigeye";
            case R.id.beauty_box_eye_circle:
                return "circleeye";
            case R.id.beauty_box_intensity_chin:
                return "chin";
            case R.id.beauty_box_intensity_forehead:
                return "forehead";
            case R.id.beauty_box_intensity_nose:
                return "thinnose";
            case R.id.beauty_box_intensity_mouth:
                return "mouth";
            case R.id.beauty_box_canthus:
                return "openeye";
            case R.id.beauty_box_eye_space:
                return "eyedis";
            case R.id.beauty_box_eye_rotate:
                return "eyerid";
            case R.id.beauty_box_long_nose:
                return "longnose";
            case R.id.beauty_box_philtrum:
                return "shrinking";
            case R.id.beauty_box_smile:
                return "smile";
            default:
                return "";
        }
    }

}
