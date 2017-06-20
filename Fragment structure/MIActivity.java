package com.podz;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.podz.utils.Methods;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by mind on 2/10/15.
 */
public class MIActivity extends AppCompatActivity {

    public Context mContext;
    public Methods methods;

    public Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        mContext = MIActivity.this;
        methods = new Methods(MIActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Methods.hideKeyboard();
        Methods.dismissProgressBar();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(newBase);
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void addFragment(final Fragment fragment, final boolean isAddToBackStack, final boolean shouldAnimate)
    {
        pushFragment(fragment,null,R.id.fragment_container, isAddToBackStack, true, shouldAnimate, false);
    }
    public void replaceFragment(final Fragment fragment, final boolean isAddToBackStack, final boolean shouldAnimate)
    {
        pushFragment(fragment,null,R.id.fragment_container, isAddToBackStack, false, shouldAnimate, false);
    }

    public void addFragmentIgnorIfCurrent(final Fragment fragment, final boolean isAddToBackStack, final boolean shouldAnimate)
    {
        pushFragment(fragment,null,R.id.fragment_container, isAddToBackStack, true, shouldAnimate, true);
    }
    public void replaceFragmentIgnorIfCurrent(final Fragment fragment, final boolean isAddToBackStack, final boolean shouldAnimate)
    {
        pushFragment(fragment,null,R.id.fragment_container, isAddToBackStack, false, shouldAnimate, true);
    }

    //

    public void addChildFragment(final Fragment fragment, final Fragment parentFragment, final int containerId, final boolean isAddToBackStack, final boolean shouldAnimate)
    {
        pushFragment(fragment,parentFragment,containerId, isAddToBackStack, true, shouldAnimate, false);
    }
    public void replaceChildFragment(final Fragment fragment, final Fragment parentFragment, final int containerId, final boolean isAddToBackStack, final boolean shouldAnimate)
    {
        pushFragment(fragment,parentFragment,containerId, isAddToBackStack, false, shouldAnimate, false);
    }

    public void addChildFragmentIgnorIfCurrent(final Fragment fragment, final Fragment parentFragment, final int containerId, final boolean isAddToBackStack, final boolean shouldAnimate)
    {
        pushFragment(fragment,parentFragment,containerId, isAddToBackStack, true, shouldAnimate, true);
    }
    public void replaceChildFragmentIgnorIfCurrent(final Fragment fragment, final Fragment parentFragment, final int containerId, final boolean isAddToBackStack, final boolean shouldAnimate)
    {
        pushFragment(fragment,parentFragment,containerId, isAddToBackStack, false, shouldAnimate, true);
    }

   /* protected void pushFragments(final int id, final Bundle args,
                              final Fragment fragment, boolean shouldAnimate,
                              final boolean shouldAdd, final boolean ignoreIfCurrent, final boolean justAdd) {
        pushFragments(id, args, fragment, null, null, shouldAnimate, shouldAdd, ignoreIfCurrent, justAdd);
    }*/
   /* protected void pushFragments(final int id, final Bundle args,
                              final Fragment fragment, final Fragment fragmentLast, final Fragment fragmentParent, boolean shouldAnimate,
                              final boolean shouldAdd, final boolean ignoreIfCurrent, final boolean justAdd) {

        int i = 0;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(id);


        if (!ignoreIfCurrent || !currentFragment.getClass().equals(fragment.getClass())) {

            if(args!=null){
                fragment.setArguments(args);
            }
            FragmentManager fragmentManager;
            if (fragmentParent == null) {
                fragmentManager = getSupportFragmentManager();
            } else {
                fragmentManager = fragmentParent.getChildFragmentManager();
            }

            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (shouldAdd) {
                ft.addToBackStack(fragment.getClass().getSimpleName());
            }

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            if (justAdd) {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    ft.hide(currentFragment);
                }
                System.out.println("@@ "+fragment.getClass().getSimpleName());
                ft.add(id, fragment, fragment.getClass().getSimpleName());
                ft.show(fragment);
            } else {
                ft.replace(id, fragment, fragment.getClass().getSimpleName());
            }

            if (fragmentLast != null) ft.hide(fragmentLast);

            ft.commit();
        }

    }*/

    protected void pushFragment(final Fragment fragment, final Fragment parentFragment, final int containerId, boolean isAddToBackStack, boolean isJustAdd, final boolean shouldAnimate, final boolean ignorIfCurrent)
    {
        if (fragment == null)
            return;
        // Add the fragment to the 'fragment_container' FrameLayout
        final FragmentManager fragmentManager;// = getSupportFragmentManager();

        if (parentFragment != null)
        {
            fragmentManager = parentFragment.getChildFragmentManager();
        }
        else {
            fragmentManager = getSupportFragmentManager();
        }


        // Find current visible fragment
        Fragment fragmentCurrent = fragmentManager.findFragmentById(R.id.fragment_container);

        if (ignorIfCurrent && fragmentCurrent!=null) {
            if (fragment.getClass().getCanonicalName().equalsIgnoreCase(fragmentCurrent.getTag())) {
                return;
            }
        }


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (shouldAnimate) {
//            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
           // fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        } else {
            fragmentTransaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }

        if (fragmentCurrent != null)
        {
            fragmentTransaction.hide(fragmentCurrent);
        }

        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getCanonicalName());
        }

        if (isJustAdd) {
            fragmentTransaction.add(containerId, fragment, fragment.getClass().getCanonicalName());
        }
        else {
            fragmentTransaction.replace(containerId, fragment, fragment.getClass().getCanonicalName());
        }

        Methods.hideKeyboard();

        try {
//            fragmentTransaction.commit();
            fragmentTransaction.commitAllowingStateLoss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public Fragment getCurrentFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Find current visible fragment
        return fragmentManager.findFragmentById(R.id.fragment_container);
    }


    public void clearBackStackFragments()
    {

        try {


        // in my case I get the support fragment manager, it should work with the native one too
        FragmentManager fragmentManager = getSupportFragmentManager();

        List<Fragment> fragmentList = fragmentManager.getFragments();



        if (fragmentList != null && !fragmentList.isEmpty())
        {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            for (Fragment fragment : fragmentList)
            {
                if (fragment != null)
                {
                    fragmentTransaction.remove(fragment);
                }
            }
            fragmentTransaction.commit();

            // this will clear the back stack and displays no animation on the screen
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            // fragmentManager.popBackStackImmediate(SplashFragment.class.getCanonicalName(),FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public void clearBackStackFragments(FragmentManager fragmentManager)
    {

        // in my case I get the support fragment manager, it should work with the native one too
//        FragmentManager fragmentManager = getSupportFragmentManager();
        // this will clear the back stack and displays no animation on the screen
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
       // fragmentManager.popBackStackImmediate(SplashFragment.class.getCanonicalName(),FragmentManager.POP_BACK_STACK_INCLUSIVE);

        List<Fragment> fragmentList = fragmentManager.getFragments();


        if (fragmentList != null && !fragmentList.isEmpty())
        {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            for (Fragment fragment : fragmentList)
            {
                if (fragment != null)
                {
                    fragmentTransaction.remove(fragment);
                }
            }
            fragmentTransaction.commit();
        }

        Methods.hideKeyboard();


    }

    public void addTabChildFragment(Fragment fragmentParent, Fragment fragmentChild)
    {
        if (fragmentParent != null && fragmentChild != null)
        {
            FragmentManager fragmentManager = fragmentParent.getChildFragmentManager();
            clearBackStackFragments(fragmentManager);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, fragmentChild, fragmentChild.getClass().getCanonicalName());
            fragmentTransaction.commit();
        }
    }

    public void clearBackStackFragmentsTag(final String tag)
    {
        // in my case I get the support fragment manager, it should work with the native one too
        FragmentManager fragmentManager = getSupportFragmentManager();
        // this will clear the back stack and displays no animation on the screen
        fragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // fragmentManager.popBackStackImmediate(SplashFragment.class.getCanonicalName(),FragmentManager.POP_BACK_STACK_INCLUSIVE);

        Methods.hideKeyboard();
    }


    /*public void clearBackStack() {

        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();

        for (int i = 0; i < count; i++) {
            fm.popBackStack();
        }

    }*/

}
