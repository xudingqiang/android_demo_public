package com.bella.android_demo_public.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.PopupWindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bella.android_demo_public.BellaDataBase;
import com.bella.android_demo_public.R;
import com.bella.android_demo_public.bean.RegionInfo;
import com.bella.android_demo_public.utils.StringUtils;
import com.bella.android_demo_public.utils.Utils;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GpsSetActivity extends AppCompatActivity {
    private static final String TAG = "GpsSetActivity";

    ImageView imgSave;
    TextView txtCountry;
    TextView txtProvince;
    TextView txtCity;
    ImageView imgBack;

    List<String> listCountrys;
    List<String> listProvinces;
    List<String> listCitys;

    List<RegionInfo> listAddress;
    List<String> listCityGps;

    Context context;

    Activity activity;

    String gpsValue;

    int indexCountry = 0;
    int indexProvince = 0;
    int indexCity = 0;
    boolean isChineseLanguage;


    SimpleAdapter adapterCountry;
    SimpleAdapter adapterProvince;
    SimpleAdapter adapterCity;

    private static final int MSG_COURTY = 1001;
    private static final int MSG_PROVINCE = 1002;
    private static final int MSG_CITY = 1003;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int pos = msg.arg1;
            switch (msg.what) {
                case MSG_COURTY:
                    if (listCountrys != null) {
                        Log.w(TAG, "bella listCountrys  = " + listCountrys.toString());
                        adapterCountry.notifyDataSetChanged();
                        if (listCountrys.size() > 0) {
                            queryProvincesByCountry(listCountrys.get(indexCountry), indexCountry);
                        }
                    } else {
                        listCountrys = new ArrayList<>();
                        adapterCountry.notifyDataSetChanged();
                    }
                    break;

                case MSG_PROVINCE:
                    txtCountry.setText(listCountrys.get(pos));
                    if (listProvinces != null) {
                        Log.w(TAG, "bella Provinces pos  = " + pos + ", indexProvince: "+ indexProvince);
//                        Log.w(TAG, "bella listProvinces  = " + listProvinces.toString());

//                        if (pos != indexProvince) {
//                            txtProvince.setText(listProvinces.get(indexProvince));
//                        }
                        indexCountry = pos;
                        adapterProvince.notifyDataSetChanged();
//                        queryCitysByProvince(listProvinces.get(0), 0);
                    } else {
                        listProvinces = new ArrayList<>();
                        adapterProvince.notifyDataSetChanged();
                    }


                    break;

                case MSG_CITY:
                    txtProvince.setText(listProvinces.get(pos));
                    if (listCitys != null) {
                        Log.w(TAG, "bella city pos  = " + pos + ", indexCity: "+ indexCity);
                        Log.w(TAG, "bella listCitys  = " + listCitys.toString());

                        adapterCity.notifyDataSetChanged();
//                        if (pos != indexProvince) {
                        txtCity.setText(listCitys.get(indexCity));
//                        }
                        gpsValue = listAddress.get(0).getGps();
                        indexProvince = pos;
                    } else {
                        listCitys = new ArrayList<>();
                        adapterCity.notifyDataSetChanged();
                    }

                    break;

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_set);
        context = this ;
        activity = this ;
        initView();
        isChineseLanguage = Utils.isChineseLanguage(context);
        initData();
    }


    private void initView() {
//        imgSave = (ImageView) rootView.findViewById(R.id.imgSave);
        txtCountry = (TextView) findViewById(R.id.txtCountry);
        txtProvince = (TextView) findViewById(R.id.txtProvince);
        txtCity = (TextView) findViewById(R.id.txtCity);

        txtCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopWindow(txtCountry, adapterCountry);
            }
        });

        txtProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopWindow(txtProvince, adapterProvince);
            }
        });

        txtCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopWindow(txtCity, adapterCity);
            }
        });

//        PendingIntent.getBroadcast()
    }


    public void setGps() {
//        String value = listCityGps.get(pos);
        String value = gpsValue;
        String locationGps = indexCountry + "~" + indexProvince + "~"
                + indexCity;
        Settings.Global.putString(context.getContentResolver(), "locationGps", locationGps);
        Log.w(TAG, "bella gpsValue = " + gpsValue);


        value = value.replace("\n", "").trim();
        String address = "/tmp/unix.str";
        LocalSocket clientSocket = new LocalSocket();
        LocalSocketAddress locSockAddr = new LocalSocketAddress(address, LocalSocketAddress.Namespace.FILESYSTEM);
        OutputStream clientOutStream = null;
        try {
            clientSocket.connect(locSockAddr);
            clientOutStream = clientSocket.getOutputStream();
            clientOutStream.write(value.getBytes());
            clientSocket.shutdownOutput();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static boolean isChineseLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language.equals("zh");
    }

    private void initData() {



        listCountrys = new ArrayList<>();
        listProvinces = new ArrayList<>();
        listCitys = new ArrayList<>();
        listAddress = new ArrayList<>();
        adapterCountry = new SimpleAdapter(context, listCountrys, new SimpleAdapter.ItemClick() {
            @Override
            public void setOnItemClick(int pos) {
                popWindow.dismiss();
                queryProvincesByCountry(listCountrys.get(pos), pos);
            }
        });

        adapterProvince = new SimpleAdapter(context, listProvinces, new SimpleAdapter.ItemClick() {
            @Override
            public void setOnItemClick(int pos) {
                txtProvince.setText(listProvinces.get(pos));
                popWindow.dismiss();
                queryCitysByProvince(listProvinces.get(pos), pos);
            }
        });
        adapterCity = new SimpleAdapter(context, listCitys, new SimpleAdapter.ItemClick() {
            @Override
            public void setOnItemClick(int pos) {
                txtCity.setText(listCitys.get(pos));
                popWindow.dismiss();
                gpsValue = listAddress.get(pos).getGps();
                indexCity = pos;
            }
        });


        String locationGps = Settings.Global.getString(context.getContentResolver(), "locationGps");
//        LogTools.i("locationGps: " + locationGps);
        if (locationGps != null) {
            String[] arrLocationGps = locationGps.split("~");
            try {
                indexCountry = StringUtils.ToInt(arrLocationGps[0]);
                indexProvince = StringUtils.ToInt(arrLocationGps[1]);
                indexCity = StringUtils.ToInt(arrLocationGps[2]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


//        listCountrys.add("中国");
//        listCountrys.add("美国");
//        adapterCountry.notifyDataSetChanged();
//
//        listProvinces.add("中国");
//        listProvinces.add("美国");
//        adapterProvince.notifyDataSetChanged();

        queryAllCountry();

    }

    private PopupWindow popWindow;

    @SuppressLint("WrongConstant")
    private void initPopWindow(View v, SimpleAdapter simpleAdapter) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_popup, null, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(simpleAdapter);
        int height = FrameLayout.LayoutParams.WRAP_CONTENT;

        if (null == popWindow) {
            if (v.getId() == R.id.txtCountry) {
                if (listCountrys.size() > 10) {
                    height = 200;
                }
            } else if (v.getId() == R.id.txtProvince) {
                if (listProvinces.size() > 10) {
                    height = 200;
                }
            } else {
                if (listCitys.size() > 10) {
                    height = 200;
                }
            }
            popWindow = new PopupWindow(view,
                    200, height, true);
            popWindow.setFocusable(false);// 底部导航消失
            popWindow.setSoftInputMode(popWindow.INPUT_METHOD_NEEDED);
            popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popWindow.setTouchable(true);
            popWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            //
            popWindow.setOutsideTouchable(true);//
            popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {// 消失后的处理
                @Override
                public void onDismiss() {
                    popWindow = null;
                }
            });
            // 要为popWindow设置一个背景才有效
            popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            // PopupWindowCompat.showAsDropDown(popWindow, v, 0, 0, Gravity.START);
            PopupWindowCompat.showAsDropDown(popWindow, v, -50, 10, Gravity.RIGHT);
        }

    }

    private void queryAllCountry() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    listCountrys = new ArrayList<>();
                    try {
                        if (isChineseLanguage) {
                            listCountrys.addAll(BellaDataBase.getInstance(context).regionDao().getAllZhCoutry());
                        } else {
                            listCountrys.addAll(BellaDataBase.getInstance(context).regionDao().getAllEnCoutry());
                        }
                        queryProvincesByCountry(listCountrys.get(indexCountry), indexCountry);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.w(TAG, "bella listCountrys size = " + listCountrys.size());

                    Message msg = new Message();
                    msg.what = MSG_COURTY;
                    msg.arg1 = indexCountry;
                    handler.sendMessage(msg);

                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void queryProvincesByCountry(String countryName, int pos) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                listProvinces.clear();
                try {
                    if (isChineseLanguage) {
                        listProvinces.addAll(BellaDataBase.getInstance(context).regionDao().getAllZhProvincesByCoutryId(countryName));
                    } else {
                        listProvinces.addAll(BellaDataBase.getInstance(context).regionDao().getAllEnProvincesByCoutryId(countryName));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.w(TAG, "bella listProvinces size = " + listProvinces.size());

                Message msg = new Message();
                msg.arg1 = pos;
                msg.what = MSG_PROVINCE;
                handler.sendMessage(msg);

            }
        }).start();
    }

    private void queryCitysByProvince(String province, int pos) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                listCitys.clear();
                listAddress.clear();
                try {
                    listAddress.addAll(BellaDataBase.getInstance(context).regionDao().getAllCitysByProvinceId(province));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.w(TAG, "bella listAddress size = " + listAddress.size());

                if (listAddress != null) {
                    listCitys.addAll(listAddress.stream()
                            .map(address -> isChineseLanguage ? address.getCityName() : address.getCityNameEn())
                            .collect(Collectors.toList()));
                }

                Message msg = new Message();
                msg.what = MSG_CITY;
                msg.arg1 = pos;
                handler.sendMessage(msg);

            }
        }).start();
    }


    static class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.Holder> {
        Context context;
        List<String> list;
        ItemClick itemClick;

        public interface ItemClick {
            void setOnItemClick(int pos);
        }

        public SimpleAdapter(Context context, List<String> list, ItemClick itemClick) {
            this.context = context;
            this.list = list;
            this.itemClick = itemClick;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SimpleAdapter.Holder(LayoutInflater.from(context).inflate(R.layout.item_location, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.txtName.setText(list.get(position));
            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.setOnItemClick(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView txtName;
            LinearLayout rootView;

            public Holder(@NonNull View itemView) {
                super(itemView);
                txtName = itemView.findViewById(R.id.txtName);
                rootView = itemView.findViewById(R.id.rootView);
            }
        }
    }
}