package jp.gr.java_conf.nstommo_silenter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by 智哉 on 2015/08/13.
 */
public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.ItemViewholder> implements View.OnClickListener,View.OnLongClickListener{


    ArrayList<String> limbegin,limfinish,options;
    OnDeleteListener listener;
    View parent;
    Context mc;

    public MyRecycleAdapter(ArrayList<String> limb,ArrayList<String> limf,ArrayList<String> opt,OnDeleteListener listener,View v){

        limbegin = limb;
        limfinish = limf;
        options = opt;
        this.listener = listener;
        parent = v;
        mc = parent.getContext();

    }

    @Override
    public int getItemCount() {
        return limbegin.size();
    }

    @Override
    public void onBindViewHolder(ItemViewholder itemViewholder,int i) {

        final int ii = i;

        String begin[] = new String[5];
        String finish[] = new String[5];

        Scanner sb = new Scanner(limbegin.get(i)).useDelimiter("s*/s*");
        Scanner sf = new Scanner(limfinish.get(i)).useDelimiter("s*/s*");


        int y = 0;

        while (sb.hasNext()) {

            begin[y] = Integer.toString(sb.nextInt());
            finish[y] = Integer.toString(sf.nextInt());

            Log.d("recyclwerAd", begin[y] + " " + finish[y]);
            y++;



        }


        if (options.get(i).equals("0")){

            itemViewholder.tv1.setText(begin[0]+mc.getString(R.string.year)+begin[1]+mc.getString(R.string.month)+begin[2]+mc.getString(R.string.date)+begin[3]+mc.getString(R.string.hour)+begin[4]+mc.getString(R.string.minute));
            itemViewholder.tv2.setText(finish[0]+mc.getString(R.string.year)+finish[1]+mc.getString(R.string.month)+finish[2]+mc.getString(R.string.date)+finish[3]+mc.getString(R.string.hour)+finish[4]+mc.getString(R.string.minute));

        }else if (Integer.parseInt(options.get(i))>=1) {

            itemViewholder.tv1.setText(begin[3]+mc.getString(R.string.hour)+begin[4]+mc.getString(R.string.minute));
            itemViewholder.tv2.setText(finish[3]+mc.getString(R.string.hour)+finish[4]+mc.getString(R.string.minute));




           if (options.get(i).equals("2")) {
                itemViewholder.tv3.setText(R.string.monday);

            }else if (options.get(i).equals("3")) {
                itemViewholder.tv3.setText("  "+mc.getString(R.string.tuesday));

            } else if (options.get(i).equals("4")) {
                itemViewholder.tv3.setText("  "+mc.getString(R.string.wednesday));

            } else if (options.get(i).equals("5")) {
                itemViewholder.tv3.setText("  "+mc.getString(R.string.thursday));

            } else if (options.get(i).equals("6")) {
                itemViewholder.tv3.setText("  "+mc.getString(R.string.friday));

            } else if (options.get(i).equals("7")) {
                itemViewholder.tv3.setText("  "+mc.getString(R.string.saturday));

            } else if (options.get(i).equals("1")) {
                itemViewholder.tv3.setText("  "+mc.getString(R.string.sunday));

            }
        }

        itemViewholder.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(parent, "delete this setting?", Snackbar.LENGTH_LONG)
                        .setAction("YES", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listener.onClickDelete(v, ii);
                            }
                        }).show();

                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
   //     listener.onClickDelete(v, i);
    }

    @Override
    public boolean onLongClick(View v) {
        Snackbar.make(parent, "delete this setting?", Snackbar.LENGTH_LONG)
                .setAction("YES", this).show();
        return false;
    }

    @Override
    public ItemViewholder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_layout,viewGroup,false);


        return new ItemViewholder(v);
    }






    public static class ItemViewholder extends RecyclerView.ViewHolder {

        public TextView tv1, tv2, tv3;
        public View v;

        public ItemViewholder(View v) {
            super(v);
            this.v = v;
            tv1 = (TextView)v.findViewById(R.id.begintime_all);
            tv2 = (TextView)v.findViewById(R.id.finishtime_all);
            tv3 = (TextView)v.findViewById(R.id.option_text_view);
        }
    }

    interface OnDeleteListener {


        void onClickDelete(View v,int i);
    }

}
