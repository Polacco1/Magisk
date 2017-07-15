package com.topjohnwu.magisk.asyncs;

import android.app.Activity;

import com.topjohnwu.magisk.MagiskManager;
import com.topjohnwu.magisk.module.BaseModule;
import com.topjohnwu.magisk.module.Module;
import com.topjohnwu.magisk.utils.Logger;
import com.topjohnwu.magisk.utils.Utils;
import com.topjohnwu.magisk.utils.ValueSortedMap;

public class LoadModules extends ParallelTask<Void, Void, Void> {

    public LoadModules(Activity context) {
        super(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Logger.dev("LoadModules: Loading modules");

        magiskManager.moduleMap = new ValueSortedMap<>();

        for (String path : Utils.getModList(magiskManager.rootShell, MagiskManager.MAGISK_PATH)) {
            Logger.dev("LoadModules: Adding modules from " + path);
            try {
                Module module = new Module(magiskManager.rootShell, path);
                magiskManager.moduleMap.put(module.getId(), module);
            } catch (BaseModule.CacheModException ignored) {}
        }

        Logger.dev("LoadModules: Data load done");
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        magiskManager.moduleLoadDone.trigger();
        super.onPostExecute(v);
    }
}
