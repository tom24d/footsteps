package jp.gr.java_conf.nstommo_silenter;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements MyRecycleAdapter.OnDeleteListener,ServiceConnection,Runnable{

    Toolbar toolbar;
    LinearLayout ll;
    ArrayList<String> limtimebegin,limtimefinish,options;
    SharedPreferences sp;

    private Intent intent;

    protected AdView madview;


    RecyclerView rv;
    RecyclerView.LayoutManager rlmanager;
    MyRecycleAdapter mradapter;

    ProgressDialog pd;

    private int indexi;


    private MyService mService = null;

    //java.util.scannerを使って一分ごとにserviceで呼ばれるチェックで構文解析可能かどうかためしてみる。たぶんいける。がんばれ


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findviews();
        ll = (LinearLayout)findViewById(R.id.ll);



        if (objectRead("LIM_F") != null){
            limtimebegin = objectRead("LIM_F");
        }else if (objectRead("LIM_F") == null){

            limtimebegin = new ArrayList<String>();

        }

        if (objectRead("LIM_S") != null){
            limtimefinish = objectRead("LIM_S");
        }else if (objectRead("LIM_S") == null){

            limtimefinish = new ArrayList<String>();

        }

        if (objectRead("OPTION") != null){
            options = objectRead("OPTION");
        }else if (objectRead("OPTION") == null){

            options = new ArrayList<String>();

        }

        mradapter = new MyRecycleAdapter(limtimebegin,limtimefinish,options,this,ll);
        rv.setAdapter(mradapter);





        intent = new Intent(this,MyService.class);
        startService(intent);
        bindService(intent, this, BIND_AUTO_CREATE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

     //   menu.add(0, MENU_ADD, 0, "Add").setIcon(R.drawable.ic_action_add);
     //   menu.add(0,MENU_SETTING,0,"Setting").setIcon(R.drawable.ic_action_settings);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, R.string.ninni,Toast.LENGTH_LONG).show();
            return true;

        }
        if (id == R.id.action_add){

            startActivityForResult(new Intent(this,Main2Activity.class),0);

            return true;
        }
        if (id == R.id.ic_action_help){

            MyDialogFragment fragment = new MyDialogFragment();
            fragment.show(getFragmentManager(),"mds");

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        kousin(indexi);

        pd.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {

            final Bundle bundle = data.getExtras();

            int nm = Calendar.getInstance().get(Calendar.SECOND);

            if (nm == 0) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(1800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        limtimebegin.add(Integer.toString(bundle.getInt("beginyear")) + "/" + Integer.toString(bundle.getInt("beginmonth")) + "/" + Integer.toString(bundle.getInt("begindate")) + "/" + Integer.toString(bundle.getInt("beginhourofday")) + "/" + Integer.toString(bundle.getInt("beginminute")));
                        limtimefinish.add(Integer.toString(bundle.getInt("finishyear")) + "/" + Integer.toString(bundle.getInt("finishmonth")) + "/" + Integer.toString(bundle.getInt("finishdate")) + "/" + Integer.toString(bundle.getInt("finishhourofday")) + "/" + Integer.toString(bundle.getInt("finishminute")));
                        options.add(Integer.toString(bundle.getInt("options")));

                        mService.refreshData(limtimebegin, limtimefinish, options);

                        objectSave(limtimebegin, limtimefinish, options);

                        pd.dismiss();

                        mradapter.notifyItemInserted(mradapter.getItemCount());


                    }
                });

                thread.start();

                pd.show();

            }else{

                limtimebegin.add(Integer.toString(bundle.getInt("beginyear")) + "/" + Integer.toString(bundle.getInt("beginmonth")) + "/" + Integer.toString(bundle.getInt("begindate")) + "/" + Integer.toString(bundle.getInt("beginhourofday")) + "/" + Integer.toString(bundle.getInt("beginminute")));
                limtimefinish.add(Integer.toString(bundle.getInt("finishyear")) + "/" + Integer.toString(bundle.getInt("finishmonth")) + "/" + Integer.toString(bundle.getInt("finishdate")) + "/" + Integer.toString(bundle.getInt("finishhourofday")) + "/" + Integer.toString(bundle.getInt("finishminute")));
                options.add(Integer.toString(bundle.getInt("options")));

                mService.refreshData(limtimebegin, limtimefinish, options);

                mradapter.notifyItemInserted(mradapter.getItemCount());

                objectSave(limtimebegin, limtimefinish, options);

            }

        }
    }



    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = ((MyService.LocalBinder)service).getService();
        Log.d("activity", "mservice on");
        mService.refreshData(limtimebegin,limtimefinish,options);
    }


    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // serviceの終了のしかたの確認

       if (limtimebegin.isEmpty()){

           unbindService(this);
           stopService(intent);
           NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
           nm.cancel(133);

       }else {
           unbindService(this);
       }

        madview.destroy();

    }

    @Override
    synchronized public void onClickDelete(View v, int i) {
        indexi = i;
        int x = Calendar.getInstance().get(Calendar.SECOND);
        if (x==0) {

            pd.show();

            Thread thread = new Thread(this);
            thread.start();

        }else{
            kousin(i);
            objectSave(limtimebegin, limtimefinish, options);
        }

    }

    synchronized private void kousin(int i){
        limtimebegin.remove(i);
        limtimefinish.remove(i);
        options.remove(i);

    //    mradapter.notifyItemRemoved(i);
        mradapter.notifyDataSetChanged();
    }


    synchronized private void objectSave(ArrayList lm1,ArrayList lm2,ArrayList option){

        Gson gson = new Gson();
        String lm1string = gson.toJson(lm1);
        String lm2string = gson.toJson(lm2);
        String opstring  = gson.toJson(option);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sp.edit().putString("LIM_F",lm1string).putString("LIM_S",lm2string).putString("OPTION",opstring).apply();


    }

    private ArrayList<String> objectRead(String key){

        Gson gson = new Gson();
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String readobject = sp.getString(key, "");
        if (readobject.equals("")){
            return null;
        }

        ArrayList o = gson.fromJson(readobject, ArrayList.class);

        return o;
    }

    private void findviews(){

        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.setting);
        setSupportActionBar(toolbar);


        rv = (RecyclerView)findViewById(R.id.my_recycle_view);
        rv.setHasFixedSize(true);
        rlmanager = new LinearLayoutManager(this);
        rv.setLayoutManager(rlmanager);

        pd = new ProgressDialog(this);
        pd.setTitle("処理中");
        pd.setMessage("お待ちください");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        madview = (AdView)findViewById(R.id.adView);
        AdRequest madrequest = new AdRequest.Builder().build();
        madview.loadAd(madrequest);



    }

    @Override
    protected void onPause() {
        super.onPause();
        madview.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        madview.resume();
    }

    public static class MyDialogFragment extends DialogFragment{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);

            View v = inflater.inflate(R.layout.custom_dialog_f, null);

            builder.setView(v);

            builder.setMessage("Help")
                    .setNegativeButton("閉じる", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            return  builder.create();
        }
    }

}
