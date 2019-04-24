package jp.gr.java_conf.nstommo_silenter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener,RadioGroup.OnCheckedChangeListener{

    EditText et1,et2,et3,et4;

  //  RadioButton rb1,rb2,rb3,rb4,rb5,rb6,rb7;
    Switch s;
    RadioGroup group;
    Bundle bundle;
    Button savebt;

    private int options = 0;

    private int beginyear = 0;
    private int beginmonth = 0;
    private int begindate = 0;
    private int beginhourofday = 0;
    private int beginminute = 0;

    private int finishyear = 0;
    private int finishmonth = 0;
    private int finishdate = 0;
    private int finishhourofday = 0;
    private int finishminute = 0;

    protected AdView ad;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findviews();

        ad = (AdView)findViewById(R.id.adView2);
        AdRequest aq = new AdRequest.Builder().build();
        ad.loadAd(aq);



    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked){

            et3.setVisibility(View.INVISIBLE);
            et4.setVisibility(View.INVISIBLE);

            group.setVisibility(View.VISIBLE);

        }else if(!isChecked){

            et3.setVisibility(View.VISIBLE);
            et4.setVisibility(View.VISIBLE);

            group.setVisibility(View.INVISIBLE);

            options = 0;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (checkedId == R.id.radioButton1){

            options = 2;

        }else if (checkedId == R.id.radioButton2){
            options = 3;

        }else if (checkedId == R.id.radioButton3){
            options = 4;

        }else if (checkedId == R.id.radioButton4){
            options = 5;

        }else if (checkedId == R.id.radioButton5){

            options = 6;

        }else if (checkedId == R.id.radioButton6){

            options = 7;

        }else if (checkedId == R.id.radioButton7){
            options = 1;

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(0, null);
        ad.destroy();
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.save_bt){

            Calendar begin = Calendar.getInstance();
            begin.set(Calendar.SECOND,0);
            Calendar finish = Calendar.getInstance();
            finish.set(Calendar.SECOND,0);

            if (options==0) {
                begin.set(Calendar.YEAR, beginyear);
                begin.set(Calendar.MONTH, beginmonth-1);
                begin.set(Calendar.DATE, begindate);
                begin.set(Calendar.HOUR_OF_DAY, beginhourofday);
                begin.set(Calendar.MINUTE, beginminute);

                finish.set(Calendar.YEAR, finishyear);
                finish.set(Calendar.MONTH, finishmonth-1);
                finish.set(Calendar.DATE, finishdate);
                finish.set(Calendar.HOUR_OF_DAY, finishhourofday);
                finish.set(Calendar.MINUTE, finishminute);
            }else if (options>=1){
                begin.set(Calendar.HOUR_OF_DAY, beginhourofday);
                begin.set(Calendar.MINUTE, beginminute);

                finish.set(Calendar.HOUR_OF_DAY, finishhourofday);
                finish.set(Calendar.MINUTE, finishminute);
            }

            if ((begin.compareTo(finish)<0&&options==0&&beginyear!=0&&finishyear!=0)||(options>=1&&begin.compareTo(finish)<0)) {

                bundle.putInt("options", options);

                bundle.putInt("beginyear", beginyear);
                bundle.putInt("beginmonth", beginmonth);
                bundle.putInt("begindate", begindate);
                bundle.putInt("beginhourofday", beginhourofday);
                bundle.putInt("beginminute", beginminute);

                bundle.putInt("finishyear", finishyear);
                bundle.putInt("finishmonth", finishmonth);
                bundle.putInt("finishdate", finishdate);
                bundle.putInt("finishhourofday", finishhourofday);
                bundle.putInt("finishminute", finishminute);

                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(1, intent);

                Log.d("MainActivity2", "SetResult");


                finish();

            }else if ((options==0&&(beginyear==0||beginmonth==0||begindate==0))||options>=1&&begin.compareTo(finish)<0&&finishhourofday==0){

                Toast.makeText(getApplicationContext(),R.string.kuuran,Toast.LENGTH_SHORT).show();
            }else if (begin.compareTo(finish)>=0) {

                Toast.makeText(getApplicationContext(), R.string.begin_finish_error, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            }



        }

    }

    void findviews(){

        group = (RadioGroup)findViewById(R.id.radiogroup);


        et1 = (EditText)findViewById(R.id.begintime);
        et2 = (EditText)findViewById(R.id.finishtime);
        et3 = (EditText)findViewById(R.id.begindate);
        et4 = (EditText)findViewById(R.id.finishdate);

        et1.setFocusable(false);
        et2.setFocusable(false);
        et3.setFocusable(false);
        et4.setFocusable(false);

        s = (Switch)findViewById(R.id.switch1);

        savebt = (Button)findViewById(R.id.save_bt);
        savebt.setOnClickListener(this);

   //     rb1 = (RadioButton)findViewById(R.id.radioButton1);
   //     rb2 = (RadioButton)findViewById(R.id.radioButton2);
   //     rb3 = (RadioButton)findViewById(R.id.radioButton3);
   //     rb4 = (RadioButton)findViewById(R.id.radioButton4);
   //     rb5 = (RadioButton)findViewById(R.id.radioButton5);
   //     rb6 = (RadioButton)findViewById(R.id.radioButton6);
   //     rb7 = (RadioButton)findViewById(R.id.radioButton7);







        et1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog td = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        et1.setText(Integer.toString(hourOfDay) + "/" + Integer.toString(minute));
                        beginhourofday = hourOfDay;
                        beginminute = minute;
                    }
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);


                td.show();
            }
        });

        et2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog td2 = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        et2.setText(Integer.toString(hourOfDay)+"/"+Integer.toString(minute));
                        finishhourofday = hourOfDay;
                        finishminute = minute;
                    }
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),true);


                td2.show();
            }
        });

        et3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dp = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        et3.setText(Integer.toString(year)+"/"+Integer.toString(monthOfYear+1)+"/"+Integer.toString(dayOfMonth));
                        beginyear = year;
                        beginmonth = monthOfYear+1;
                        begindate = dayOfMonth;
                    }
                },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });

        et4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dp = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        et4.setText(Integer.toString(year) + "/" + Integer.toString(monthOfYear+1) + "/" + Integer.toString(dayOfMonth));
                        finishyear = year;
                        finishmonth = monthOfYear+1;
                        finishdate = dayOfMonth;
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });


        group.setOnCheckedChangeListener(this);
        s.setOnCheckedChangeListener(this);

        group.setVisibility(View.INVISIBLE);

        bundle = new Bundle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ad.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ad.pause();
    }
}
