package com.xygame.sg.utils.ImageActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ImageGridActivity extends Activity {
    public static final String EXTRA_IMAGE_LIST = "imagelist";
    private static int totalPic = 1;

    // ArrayList<Entity> dataList;
    List<ImageItem> dataList = new ArrayList<ImageItem>();
    GridView gridView;



    ImageGridAdapter adapter;
    AlbumHelper helper;
    TextView bt;
    TextView back;
    long s = System.currentTimeMillis();
    long e;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if(s==0){
                        s = System.currentTimeMillis();
                    }else{
                    e = System.currentTimeMillis();
                        if (e-s<8000){
                            s = 0;
                            return;

                        }
                    }
                    Toast.makeText(ImageGridActivity.this, "只能选" + totalPic + "张了", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    bt.performClick();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_grid);
        totalPic = getIntent().getIntExtra("max",1);

        gridView = (GridView) findViewById(R.id.gridview);
        bt = (TextView) findViewById(R.id.bt);
        back = (TextView) findViewById(R.id.back);

        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        Intent intent = getIntent();
        boolean refresh = true;
        if (intent != null && intent.hasExtra(Constants.REFRESH_PHOTO)) {
            refresh = intent.getBooleanExtra(Constants.REFRESH_PHOTO, true);
        }

        if (helper.getImagesBucketList(refresh) != null && helper.getImagesBucketList(refresh).size() != 0) {


            dataList = helper.getImagesBucketList(refresh).get(0).imageList;


        }

        // dataList = (List<ImageItem>) getIntent().getSerializableExtra(
        //		 EXTRA_IMAGE_LIST);

        initView();

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<String>();
                /*Collection<String> c = adapter.map.values();
				Iterator<String> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}*/

                for (Map.Entry<String, String> entry : adapter.map.entrySet()) {

                    list.add(entry.getValue());

                }


                if (Bimp.act_bool) {
					/*
					 * Intent intent = new Intent(ImageGridActivity.this,
					 * PublishedActivity.class); startActivity(intent);
					 */
                    Bimp.act_bool = false;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (Bimp.drr.size() < totalPic) {
                        Bimp.drr.add(list.get(i));
                    }
                }
                setResult(0,new Intent().putExtra("bimp", new ArrayList<String>(adapter.list)).putExtra("multi",getIntent().getBooleanExtra("multi",false)));
                finish();
            }

        });
    }

    @Override
    public void finish() {


        adapter.list.clear();
        super.finish();
    }

    /**
     */
    private void initView() {
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ImageGridAdapter(ImageGridActivity.this, dataList, mHandler, totalPic);
        gridView.setAdapter(adapter);

        if (Bimp.drr.size() > 0) {
            bt.setText("完成" + "(" + Bimp.drr.size() + ")");
        }
        adapter.setTextCallback(new ImageGridAdapter.TextCallback() {
            public void onListen(int count) {
                if (count <= 0) {
                    count = 0;
                }
                bt.setText("完成" + "(" + count + ")");
            }
        });

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dataList.get(position).isSelected = !dataList.get(position).isSelected;
                adapter.notifyDataSetChanged();
            }

        });

    }



}
