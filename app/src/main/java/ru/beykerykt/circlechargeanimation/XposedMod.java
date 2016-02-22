package ru.beykerykt.circlechargeanimation;

import android.view.ViewGroup;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.getBooleanField;
import static de.robv.android.xposed.XposedHelpers.getObjectField;


public class XposedMod implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.android.systemui"))
            return;

        findAndHookMethod("com.android.systemui.statusbar.phone.PhoneStatusBar", lpparam.classLoader, "makeStatusBarView", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                ViewGroup statusView = (ViewGroup) getObjectField(param.thisObject, "mStatusBarView");
                int containerId = statusView.getResources().getIdentifier("battery", "id", "com.android.systemui");
                Object batteryView = statusView.findViewById(containerId);
                if(!getBooleanField(batteryView, "mAnimationsEnabled")) {
                    callMethod(batteryView, "setAnimationsEnabled", true);
                }
            }
        });
    }

}