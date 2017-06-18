package com.xygame.sg.activity.image;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ImagePagerActivity extends SGBaseActivity implements OnClickListener {
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    public static final String EXTRA_IMAGE_DELETE = "image_delete";
    private TextView titleName, rightButtonText;
    private View backButton, rightButton, topView, topLine;
    private HackyViewPager mPager;
    private int pagerPosition;
    private boolean isDelete = false, isEditor = false, isShowBinner = true;
    private String[] urls, urlsId;
    private ImagePagerAdapter mAdapter;
    private List<String> deleteImages;
    private static Animation alphaIn, alphaOut;
    private String timerDesc;
    private String currUrl, currId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.le_image_detail_pager);
        isEditor = getIntent().getBooleanExtra(EXTRA_IMAGE_DELETE, false);
        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);
        urlsId = getIntent().getStringArrayExtra("urlsId");
        timerDesc = getIntent().getStringExtra("timerDesc");

        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);

        backButton = findViewById(R.id.backButton);
        rightButton = findViewById(R.id.rightButton);
        topView = findViewById(R.id.topView);
        topLine = findViewById(R.id.topLine);

        deleteImages = new ArrayList<String>();

        mPager = (HackyViewPager) findViewById(R.id.pager);
        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        if (isEditor) {
            rightButton.setVisibility(View.VISIBLE);
            rightButtonText.setVisibility(View.VISIBLE);
            rightButtonText.setText(getResources().getString(R.string.menu_delete_pic));
        } else {
            rightButton.setVisibility(View.GONE);
        }
        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
        titleName.setText(text);

        rightButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        alphaIn = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        alphaOut = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
        // 更新下标
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, mPager.getAdapter().getCount());
                titleName.setText(text);
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public String[] fileList;

        public ImagePagerAdapter(FragmentManager fm, String[] fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.length;
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList[position];
            ImageDetailFragment fragment = ImageDetailFragment.newInstance(url);
            fragment.addImageListener(new ImagePagerListener() {

                @Override
                public void onTopClickListener(Boolean flag) {
                    // TODO Auto-generated method stub
                    isShowBinner = !isShowBinner;
                    if (isShowBinner) {
                        topView.startAnimation(alphaIn);
                        topView.setVisibility(View.VISIBLE);
                        topLine.setVisibility(View.VISIBLE);
                        quitFullScreen();
                    } else {
                        topView.startAnimation(alphaOut);
                        topView.setVisibility(View.GONE);
                        topLine.setVisibility(View.GONE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    }
                }
            });
            return fragment;
        }

    }

    private void quitFullScreen() {
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 重载方法
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.rightButton) {
            if (pagerPosition <= urls.length - 1) {

                currUrl = urls[pagerPosition];
                if (currUrl.contains("http://")){
                    currId = urlsId[pagerPosition];
                    deleteImage();
                }else{
                    deleteImages.add(currUrl);
                    if (new File(currUrl).delete()) {
                        refreshUI();
                    }
                }
            }
        } else if (v.getId() == R.id.backButton) {
            finishResult();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishResult();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finishResult() {
        TransferImagesBean dataBean = new TransferImagesBean();
        List<String> tempList = Arrays.asList(urls);
        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.addAll(tempList);
        dataBean.setSelectImagePah(linkedList);
        dataBean.setDeleteImages(deleteImages);
        Intent intent = new Intent(Constants.IMAGE_BROADCAST_LISTENER);
        intent.putExtra(Constants.COMEBACK, dataBean);
        intent.putExtra(Constants.IS_DELE_IMAGES, isDelete);
        intent.putExtra("timerDesc", timerDesc);
        sendBroadcast(intent);
        setResult(0, new Intent());
        finish();
    }

    private void deleteImage() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("resId", currId);
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, false);
        item.setServiceURL(ConstTaskTag.QUEST_GALLERY_DELETE);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GALLERY_DELETE);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_GALLERY_DELETE:
                if ("0000".equals(data.getCode())) {
                    deleteImages.add(currId);
                    refreshUI();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.getResponseBean(data);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void refreshUI() {
        // TODO Auto-generated method stub
        urls = remove(urls, pagerPosition);
        if (pagerPosition > 0 && pagerPosition > urls.length - 1) {
            pagerPosition--;
        }
        isDelete = true;
        jugmentExit();
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void jugmentExit() {
        // TODO Auto-generated method stub
        if (0 == urls.length) {
            finishResult();
        } else {
            mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
            mPager.setAdapter(mAdapter);
            mPager.setCurrentItem(pagerPosition);
            CharSequence text = getString(R.string.viewpager_indicator, pagerPosition + 1,
                    mPager.getAdapter().getCount());
            titleName.setText(text);
        }
    }

    private String[] remove(String[] arr, int index) {

        String[] tmp = new String[arr.length - 1];

        int idx = 0;
        for (int i = 0; i < arr.length; i++) {

            if (i != index) {
                tmp[idx++] = arr[i];
            }
        }

        return tmp;
    }
}