package com.itech.bookagoo;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Artem on 01.06.14.
 */
public abstract class AbstrTabAddContentFragment extends Fragment implements ITabContentFragment, View.OnClickListener {

    private TextView txtTagGroup;
    private ImageView imgTagGroup;
    private View mRiad1;
    private View mRiad2;
    private boolean mIsOpenTagGroup = false;

    abstract public View getView();

//    @Override
//    public void onActivityCreated(Bundle bundle) {
//        super.onActivityCreated(bundle);
//
//        View v = getView();
//
//        v.findViewById(R.id.includeAddContent_View_tagGroup).setOnClickListener(this);
//        txtTagGroup = (TextView) v.findViewById(R.id.includeAddContent_TextView_tagGroup);
//        imgTagGroup = (ImageView) v.findViewById(R.id.includeAddContent_ImageView_tagGroup);
//
//        mRiad1 = v.findViewById(R.id.includeAddContent_LinearLayout_riad1);
//        mRiad2 = v.findViewById(R.id.includeAddContent_LinearLayout_riad2);
//
//        setIsOpenTagGroup(false);
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.includeAddContent_View_tagGroup:
//                setIsOpenTagGroup(!mIsOpenTagGroup);
                break;
        }
    }

    private void setIsOpenTagGroup(boolean isOpen) {

        if (isOpen) {
            mRiad1.setVisibility(View.VISIBLE);
            mRiad2.setVisibility(View.VISIBLE);
            txtTagGroup.setText(App.getContext().getString(R.string.add_content_before_me));
            txtTagGroup.setTextColor(App.getContext().getResources().getColor(R.color.txt_active));
            imgTagGroup.setImageResource(R.drawable.arrow_boyyom_on);
        } else {
            mRiad1.setVisibility(View.GONE);
            mRiad2.setVisibility(View.GONE);
            txtTagGroup.setText(App.getContext().getString(R.string.add_content_select_tag_group));
            txtTagGroup.setTextColor(App.getContext().getResources().getColor(R.color.txt_passive));
            imgTagGroup.setImageResource(R.drawable.arrow_boyyom_off);
        }

        mIsOpenTagGroup = isOpen;

    }


}
