package net.philipp_koch.dynamicmediabtrouter;

/**
 * Created by Philipp on 15.03.2015.
 */

import android.content.res.XModuleResources;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;

public class XposedModuleCheck implements IXposedHookZygoteInit, IXposedHookInitPackageResources {
    private static String MODULE_PATH = null;

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
    }

    @Override
    public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
        if (resparam.packageName.equals("net.philipp_koch.dynamicmediabtrouter")) {
            XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res);
            resparam.res.setReplacement(resparam.packageName, "string", "row_xposed_value", Global.getContext().getResources().getString(R.string.active));
        }
    }
}