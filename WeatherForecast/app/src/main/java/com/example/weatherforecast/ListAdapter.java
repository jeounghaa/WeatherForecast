package com.example.weatherforecast;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    Context context;
    ArrayList<ItemWeather> itemList = new ArrayList<>();

    TextView tvArea2; // 지역명
    TextView tvTemp; // 기온
    TextView tvImg;


    public ListAdapter(Context context, ArrayList<ItemWeather> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() { // 리스트뷰 몇개의 아이템을 가지고 있는지
        return this.itemList.size();
    }

    @Override
    public Object getItem(int position) { // 현재 어떤 아이템인지를 알려주는 부분
        return this.itemList.get(position);
    }

    @Override
    public long getItemId(int position) { // 현재 어떤 position인지
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {  // 아이템과 xml을 연결하여 화면에 표시
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_weather, parent, false);
            ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
            layoutParams.height = 100;
            convertView.setLayoutParams(layoutParams);
            tvArea2 = convertView.findViewById(R.id.tvArea2);
            tvTemp = convertView.findViewById(R.id.tvTemp);
            tvImg = convertView.findViewById(R.id.tvImg);

            tvArea2.setText(itemList.get(position).getArea());
            tvTemp.setText(itemList.get(position).getTemp());
            tvImg.setText(imgGET(itemList.get(position).getImg()));
        }
        return convertView;
    }

    // 받아온 값에 대해 이미지 변경해서 출력
    public Spanned imgGET(final String icon){
        Spanned imgText = Html.fromHtml("<img src=\"icon_drag\" width=\"100\" height=\"100\"", new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                if (source.equals("icon_drag")) {
                    Drawable drawable = null;
                    Log.e("●●●●●●●●", icon);
                    if (icon.contains("09")||icon.contains("10")){
                        drawable = context.getResources().getDrawable(R.drawable.ic_wi_rain);
                    } else if(icon.contains("01")){
                        drawable = context.getResources().getDrawable(R.drawable.ic_wi_day_sunny);
                    } else if(icon.contains("13")){
                        drawable = context.getResources().getDrawable(R.drawable.ic_wi_snow);
                    } else {
                        drawable = context.getResources().getDrawable(R.drawable.ic_wi_cloud);
                    }
                    drawable.setBounds(0, 0, 100, 100);
                    return drawable;
                }
                return null;
            }
        }, null);
        return imgText;
    }

}
