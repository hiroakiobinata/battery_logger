package com.kinjo1506.batterylogger;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment {
    public static final String TAG = MainFragment.class.getSimpleName();

    private Context  mContext;
    private TextView mDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mDescription = (TextView) v.findViewById(R.id.description);

        v.findViewById(R.id.afresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BatteryLoggerLauncher.getInstance().afresh(mContext);
                invalidate();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        invalidate();
    }

    private void invalidate() {
        long startAtInMillies = BatteryLoggerLauncher.getInstance().getLoggerStartAt(mContext, 0);
        String startAt = mContext.getString(
                R.string.description, DateFormat.format("yyyy/MM/dd HH:mm", startAtInMillies));
        mDescription.setText(startAt);
    }
}
