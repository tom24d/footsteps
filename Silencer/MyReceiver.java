package jp.gr.java_conf.nstommo_silenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())||Intent.ACTION_REBOOT.equals(intent.getAction())) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            ArrayList<String> op = new Gson().fromJson(sp.getString("OPTION",""),ArrayList.class);

            if (op!=null){

                if (!op.isEmpty()){

                    Intent service = new Intent(context, MyService.class);
                    context.startService(service);

                }
            }
        }
        //    throw new UnsupportedOperationException("Not yet implemented");
    }
}
