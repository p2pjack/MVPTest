package com.uk.umf_solutions.mvptest.comman;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by Eaun-Ballinger on 19/03/2017.
 *
 */

public class StateMaintainer {

    private final String TAG = getClass().getSimpleName();

    private final String mStateMaintainerTag;
    private final WeakReference<FragmentManager> mFragmentManager;
    private StateMngFragment mStateMaintainerFrag;
    private boolean mIsRecreating;

    /**
     * Constructor
     */
    public StateMaintainer(FragmentManager fragmentManager, String stateMaintainerTAG) {
        mFragmentManager = new WeakReference<>(fragmentManager);
        mStateMaintainerTag = stateMaintainerTAG;
    }

    public boolean firstTimeIn() {
        try {
            mStateMaintainerFrag = (StateMngFragment)
                    mFragmentManager.get().findFragmentByTag(mStateMaintainerTag);

            if (mStateMaintainerFrag == null) {
                Log.d(TAG, "creating new RetainedFragment " + mStateMaintainerTag);
                mStateMaintainerFrag = new StateMngFragment();
                mFragmentManager.get().beginTransaction()
                        .add(mStateMaintainerFrag, mStateMaintainerTag).commit();
                mIsRecreating = false;
                return true;
            } else {
                Log.d(TAG, "\n" +
                        "Returning retained fragment existence " + mStateMaintainerTag);
                mIsRecreating = true;
                return false;
            }
        } catch (NullPointerException e) {
            Log.w(TAG, "Error firstTimeIn()");
            return false;
        }
    }

    private void put(String key, Object obj) {
        mStateMaintainerFrag.put(key, obj);
    }

    public void put(Object obj) {
        put(obj.getClass().getName(), obj);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return mStateMaintainerFrag.get(key);

    }

    public static class StateMngFragment extends Fragment {
        private HashMap<String, Object> mData = new HashMap<>();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Grants that the fragment will be preserved
            setRetainInstance(true);
        }

        public void put(String key, Object obj) {
            mData.put(key, obj);
        }

        @SuppressWarnings("unchecked")
        public <T> T get(String key) {
            return (T) mData.get(key);
        }

    }
}
