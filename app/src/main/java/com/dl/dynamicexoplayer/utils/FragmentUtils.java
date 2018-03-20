package com.dl.dynamicexoplayer.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by dannylin on 2016/12/2.
 */

public class FragmentUtils {

    public static Fragment getFragment(FragmentManager fm, Class<? extends Fragment> fragmentClass, String tag) {
        Fragment fragment = fm.findFragmentByTag(tag);

        if (fragment == null) {
            try {
                fragment = fragmentClass.newInstance();

            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return fragment;
    }

    public static void addFragmentTo(FragmentManager fm, Fragment fragment, String fragmentTag, int containerId) {
        FragmentTransaction transaction = fm.beginTransaction();

        // 在add fragment的時候，要記得做檢查，避免重複add導致crash
        if (!fragment.isAdded()) {
            transaction.add(containerId, fragment, fragmentTag);
        }

        transaction.commit();
    }

    /**
     * 這個method會將之前的fragment給hide起來，然後add或是show新的fragment，
     * 如果之前的fragment已經有被add到activity，而且又再換回來，那本來那個fragment的狀態可以被保留
     *
     * 之前fragment的狀態可以被保留
     *
     * Reference: https://yrom.net/blog/2013/03/10/fragment-switch-not-restart/
     */
    public static void hideAndShowFragmentTo(FragmentManager fm,
                                             Fragment fragmentToHide, Fragment fragmentToShow,
                                             String fragmentToShowTag, int containerId) {
        FragmentTransaction transaction = fm.beginTransaction();

        if (fragmentToHide.isAdded()) {
            transaction.hide(fragmentToHide);
        }

        if (fragmentToShow.isAdded()) {
            transaction.show(fragmentToShow);

        } else {
            transaction.add(containerId, fragmentToShow, fragmentToShowTag);
        }

        transaction.commit();
    }

    /**
     * 這個method會將之前的fragment給remove掉，然後replace上新的fragment，但如果之前的fragment再換回來的話，沒有辦法保留之前fragment的狀態
     *
     * 之前fragment的狀態不會被保留
     */
    public static void replaceFragmentTo(FragmentManager fm, Class<? extends Fragment> fragmentClass,
                                         int containerId, String fragmentTag) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        Fragment fragment = getFragment(fm, fragmentClass, fragmentTag);
        fragmentTransaction.replace(containerId, fragment, fragmentTag);

        fragmentTransaction.commit();
    }
}
