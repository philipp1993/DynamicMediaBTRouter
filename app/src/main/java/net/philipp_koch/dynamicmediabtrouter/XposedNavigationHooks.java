package net.philipp_koch.dynamicmediabtrouter;

/**
 * Created by Philipp on 15.03.2015.
 */

import android.util.Log;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedNavigationHooks implements  IXposedHookLoadPackage {

    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.contains("navigon"))
            return;

        XposedBridge.log("Navigon detected: " + lpparam.packageName);
        Log.d("NavigonXposed", "Navigon started");
        net.philipp_koch.dynamicmediabtrouter.Global.setXposedRequestON(true);
        Class NK_Sound = XposedHelpers.findClass("com.navigon.nk.iface.NK_Sound", lpparam.classLoader);

        findAndHookMethod("com.navigon.nk.impl.AudioSystem", lpparam.classLoader, "playSound", NK_Sound, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Log.d("NavigonXposed", " Navigon Sound NK_Sound");
                XposedBridge.log("Navigon sound NK_Sound");
            }
        });

        findAndHookMethod("com.navigon.nk.impl.AudioSystem", lpparam.classLoader, "playSound", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Log.d("NavigonXposed", " Navigon Sound String");
                XposedBridge.log("Navigon sound String");
            }
        });

        findAndHookMethod("com.navigon.nk.impl.Advice$Factory", lpparam.classLoader, "create", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Log.d("NavigonXposed", " Navigon Advice");
                XposedBridge.log("Navigon Advice" + Global.getBT());

            }
        });
    }
}