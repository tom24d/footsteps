package jp.gr.java_conf.nstommo_silenter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    public MyService() {
    }


    public class LocalBinder extends Binder{
        MyService getService(){
            return MyService.this;
        }
    }

    ArrayList<String> limtimebegin,limtimefinish,options;

    Gson gson;
    SharedPreferences sp;
    boolean recieverun = false;

    private boolean sboolean = false;

    private int rengermode;

    private boolean bbbloo = true;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new LocalBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        gson = new Gson();
        refreshData(null, null, null);


        Timer timer = new Timer(true);
        Log.d("service","timer shokika");
        timer.schedule(new MyTimerTask(), 61000 - Calendar.getInstance().get(Calendar.SECOND) * 1000, 60000);



        return super.onStartCommand(intent, flags, startId);
    }



    synchronized protected void refreshData(@Nullable ArrayList<String> begintime,@Nullable ArrayList<String> finishtime,@Nullable ArrayList<String> option){

        if (begintime == null){
            String readob = sp.getString("LIM_F","");
           if (!readob.equals(""))limtimebegin = gson.fromJson(readob,ArrayList.class);
        }else{limtimebegin = begintime;}

        if (finishtime == null){
            String readob = sp.getString("LIM_S","");
           if (!readob.equals(""))limtimefinish = gson.fromJson(readob,ArrayList.class);
        }else{limtimefinish = finishtime;}

        if (option == null){
            String readob = sp.getString("OPTION","");
           if (!readob.equals(""))options = gson.fromJson(readob,ArrayList.class);
        }else{options = option;}

    }


    protected void checking() {

        if (!options.isEmpty()) {

            int year,month,day,hour,minute;

            Calendar cal = Calendar.getInstance();
            year = (short)cal.get(Calendar.YEAR);
            month = (short)cal.get(Calendar.MONTH)+1;
            day = (short)cal.get(Calendar.DAY_OF_MONTH);
            hour = (short)cal.get(Calendar.HOUR_OF_DAY);
            minute = (short)cal.get(Calendar.MINUTE);

            Calendar begin = Calendar.getInstance();
            begin.set(Calendar.SECOND,0);
            Calendar finish = Calendar.getInstance();
            finish.set(Calendar.SECOND,0);

            Log.d("now time",Integer.toString(year)+"/"+Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(hour)+"/"+Integer.toString(minute));


            int i = 0;//i番目の設定
            loop1:
            while (true) {
                Log.d("service index",Integer.toString(i));
                int op = Integer.parseInt(options.get(i));
                String sbegin[] = new String[5];
                String sfinish[] = new String[5];

                Scanner sb = new Scanner(limtimebegin.get(i)).useDelimiter("s*/s*");
                Scanner sf = new Scanner(limtimefinish.get(i)).useDelimiter("s*/s*");

                boolean finishok = false;
                boolean beginok = false;

                int y = 0;

                while (sb.hasNext()) {

                    sbegin[y] = Integer.toString(sb.nextInt());
                    sfinish[y] = Integer.toString(sf.nextInt());
                    Log.d("service",sbegin[y]);
                    y++;
                    Log.d("service", "hairetu yomitori");

                }


                if (op == 0) {//曜日で分岐したい
              //      if (begin[0] <= year && begin[1] <= month && begin[2] <= day && begin[3] <= hour && begin[4] <= minute) {

              //          if (finish[0] >= year && finish[1] >= month && finish[2] >= day && finish[3] >=hour && finish[4] >= minute) {
                            //できればCalender.-を避けて一括でshortかなんかに取得して、比較したい
             //       long now = Long.valueOf(Integer.toString(year)+Integer.toString(month)+Integer.toString(day)+Integer.toString(hour)+Integer.toString(minute));

              //      long bdata = Long.valueOf(begin[0]+begin[1]+begin[2]+begin[3]+begin[4]);
             //       long fdata = Long.valueOf(finish[0]+finish[1]+finish[2]+finish[3]+finish[4]);
                    begin.set(Calendar.YEAR,Integer.parseInt(sbegin[0]));
                    begin.set(Calendar.MONTH,Integer.parseInt(sbegin[1])-1);
                    begin.set(Calendar.DATE,Integer.parseInt(sbegin[2]));
                    begin.set(Calendar.HOUR_OF_DAY,Integer.parseInt(sbegin[3]));
                    begin.set(Calendar.MINUTE,Integer.parseInt(sbegin[4]));

                    finish.set(Calendar.YEAR, Integer.parseInt(sfinish[0]));
                    finish.set(Calendar.MONTH, Integer.parseInt(sfinish[1])-1);
                    finish.set(Calendar.DATE, Integer.parseInt(sfinish[2]));
                    finish.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sfinish[3]));
                    finish.set(Calendar.MINUTE, Integer.parseInt(sfinish[4]));


                    if (begin.compareTo(cal)<=0&&finish.compareTo(cal)>=0) {
                        beginok = true;
                        Log.d("service", "just the day ok");
                    }

                } else if (op >= 1) {

             //       int intnow = Integer.parseInt(Integer.toString(hour)+Integer.toString(minute));

             //       int bdata = Integer.parseInt(begin[3]+begin[4]);
            //        int fdata = Integer.parseInt(finish[3]+finish[4]);

                    begin.set(Calendar.HOUR_OF_DAY,Integer.parseInt(sbegin[3]));
                    begin.set(Calendar.MINUTE,Integer.parseInt(sbegin[4]));

                    finish.set(Calendar.HOUR_OF_DAY,Integer.parseInt(sfinish[3]));
                    finish.set(Calendar.MINUTE,Integer.parseInt(sfinish[4]));

                    if (begin.compareTo(cal)<=0&&finish.compareTo(cal)>=0&&op==cal.get(Calendar.DAY_OF_WEEK)) {
                        finishok = true;
                        Log.d("service","date of week ok");
                    }
                }

                if (beginok || finishok) {

                    recieverun = true;
                    Log.d("service","recieverunflag on");
                    break loop1;

                } else if ((!beginok)&&(!finishok)){
                    recieverun = false;
                }
                i++;

                if (i >= options.size()) {
                    Log.d("service","loop1 out");
                    break loop1;
                }
            }

            sboolean = sp.getBoolean("SBOOL",false);

            if (recieverun) {

                if (!sboolean) {

                    sboolean = true;
                    AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
                    rengermode = am.getRingerMode();
                    SharedPreferences ap = PreferenceManager.getDefaultSharedPreferences(this);
                    ap.edit().putInt("SYSTEM_AM", am.getStreamVolume(AudioManager.STREAM_SYSTEM))
                            .putInt("ALARM_AM", am.getStreamVolume(AudioManager.STREAM_ALARM))
                            .putInt("DTMF_AM", am.getStreamVolume(AudioManager.STREAM_DTMF))
                            .putInt("NOTIFY_AM", am.getStreamVolume(AudioManager.STREAM_NOTIFICATION))
                            .putInt("RING_AM", am.getStreamVolume(AudioManager.STREAM_RING))
                            .putInt("MUSIC_AM", am.getStreamVolume(AudioManager.STREAM_MUSIC))
                            .putInt("RINGER_MODE",rengermode)
                            .putBoolean("SBOOL",sboolean)
                            .apply();

                    am.setRingerMode(AudioManager.RINGER_MODE_SILENT);

                    am.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, AudioManager.FLAG_VIBRATE);
                    am.setStreamVolume(AudioManager.STREAM_ALARM,0,AudioManager.FLAG_VIBRATE);
                    am.setStreamVolume(AudioManager.STREAM_DTMF,0,AudioManager.FLAG_VIBRATE);
                    am.setStreamVolume(AudioManager.STREAM_NOTIFICATION,0,AudioManager.FLAG_VIBRATE);
                    am.setStreamVolume(AudioManager.STREAM_RING,0,AudioManager.FLAG_VIBRATE);
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_VIBRATE);

                    myNotify();

                }else if (sboolean==true&&bbbloo==true){
                    myNotify();
                    bbbloo = false;
                }
                Log.d("service", "regist reciever");

            } else if (!recieverun) {

                if (sboolean) {
                    sboolean = false;
                    AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                    am.setRingerMode(sp.getInt("RINGER_MODE", AudioManager.RINGER_MODE_VIBRATE));
                    am.setStreamVolume(AudioManager.STREAM_SYSTEM, sp.getInt("SYSTEM_AM", 0), AudioManager.FLAG_VIBRATE);
                    am.setStreamVolume(AudioManager.STREAM_ALARM, sp.getInt("ALARM_AM", 0), AudioManager.FLAG_VIBRATE);
                    am.setStreamVolume(AudioManager.STREAM_DTMF, sp.getInt("DTMF_AM", 0), AudioManager.FLAG_VIBRATE);
                    am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, sp.getInt("NOTIFY_AM", 0), AudioManager.FLAG_VIBRATE);
                    am.setStreamVolume(AudioManager.STREAM_RING, sp.getInt("RING_AM", 0), AudioManager.FLAG_VIBRATE);
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, sp.getInt("MUSIC_AM", 0), AudioManager.FLAG_VIBRATE);
                    sp.edit().putBoolean("SBOOL",sboolean).apply();
                    Log.d("service", "unregist");

                    myunnotify();

                }
            }
        }

    }

    private void myNotify(){

        Log.d("service","notify");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.mylasticon01);

        Intent intent = new Intent(this,MainActivity.class);

        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle(getString(R.string.tuutioff));
        builder.setContentText(getString(R.string.tap_hensyuu));
        builder.setTicker(getString(R.string.tuutioff));
        builder.setContentIntent(pi);
        builder.setOngoing(true);

        NotificationManager nmanager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        nmanager.notify(133,builder.build());

    }

    private void myunnotify(){
        Log.d("service","unnotify");
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(133);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MyTimerTask extends TimerTask{

        public MyTimerTask(){}

        @Override
        public void run() {
            checking();
        }
    }
}
