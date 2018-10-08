package com.example.prabhatkushwaha.ss;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;


public class viewpageadaptar extends PagerAdapter {
    Context context;
    ArrayList<String> arrayListQ = new ArrayList<>();
    Map<String, Map<String, String>> newhashmap;
    int layout[];
    ArrayList<Integer> selectedradio;
    String names[];

    public viewpageadaptar(Context context, int[] layout, String[] names, Map<String, Map<String, String>> newhashmap) {
        this.context = context;
        this.layout = layout;
        selectedradio = new ArrayList<Integer>();
        this.newhashmap = newhashmap;
        this.names = names;
        Log.d("viewpagermap", newhashmap.toString());
        for (String question : newhashmap.keySet()) {
            Log.d("key", question);
            arrayListQ.add(question);
        }

    }

    @Override
    public int getCount() {
        return layout.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View viewPagerView = inflater.inflate(layout[position], container, false);
        TextView textView = viewPagerView.findViewById(R.id.name);
        TextView q = viewPagerView.findViewById(R.id.qno);
        RadioGroup radioGroup = viewPagerView.findViewById(R.id.rg);
        RadioButton r1 = viewPagerView.findViewById(R.id.rb1);
        RadioButton r2 = viewPagerView.findViewById(R.id.rb2);
        RadioButton r3 = viewPagerView.findViewById(R.id.rb3);
        RadioButton r4 = viewPagerView.findViewById(R.id.rb4);

        if (position < 10) {
            q.setText(arrayListQ.get(position));
            r1.setText(newhashmap.get(arrayListQ.get(position)).get("opt1"));
            r2.setText(newhashmap.get(arrayListQ.get(position)).get("opt2"));
            r3.setText(newhashmap.get(arrayListQ.get(position)).get("opt3"));
            r4.setText(newhashmap.get(arrayListQ.get(position)).get("opt4"));
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selectedButton = viewPagerView.findViewById(radioGroup.getCheckedRadioButtonId());
                ((Quiz_main_window) context).saveUserAnswer(arrayListQ.get(position), selectedButton.getText().toString());
            }
        });
        container.addView(viewPagerView);
        return viewPagerView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
