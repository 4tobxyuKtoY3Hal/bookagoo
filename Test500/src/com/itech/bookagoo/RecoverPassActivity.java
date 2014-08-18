package com.itech.bookagoo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import com.actionbarsherlock.app.ActionBar;

/**
 * Created by Artem on 05.08.14.
 */
public class RecoverPassActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_pass);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.ab_recover_pass_activity);
        actionBar.getCustomView().findViewById(R.id.absRecoverPassActivity_ImageView_back).setOnClickListener(this);

        findViewById(R.id.activityRecoverPass_btView_ok).setOnClickListener(this);
        final EditText etEmail = (EditText) findViewById(R.id.activityRecoverPass_EditText_email);
        etEmail.addTextChangedListener(
                new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        Drawable drawable;
                        if (etEmail.getText().length() == 0) {
                            drawable = getResources().getDrawable(R.drawable.ic_mail);
                        } else {
                            drawable = getResources().getDrawable(R.drawable.ic_mail_green);
                        }
                        etEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                }
        );


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activityRecoverPass_btView_ok:

                break;
            case R.id.absRecoverPassActivity_ImageView_back:
                finish();
                break;
        }
    }
}
