package com.xygame.sg.activity.commen;

import android.app.Activity;
import android.content.ClipboardManager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtils;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import base.ViewBinder;
import base.frame.VisitUnit;

public class ShareBoardView extends SGBaseActivity implements OnClickListener {
	private View shareWeiXin, shareCancel, shareQQ, shareWeibo,penyouquan,qqzone,copyurl,addBlackListView,juBaoView;
	private String title = "你我之间，就差一次相遇";
	private String subTitle = "在蜜桃社区上发了一个活动";
	private String shareUrl = Constants.TARG_URL;
	private String iconUrl = "",flagFrom;
	private Bitmap thumbBmp;
	private  UMImage image;
	private ClipboardManager myClipboard;
	private String userNickName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_share_layout);
		initViews();
		initListeners();
		initDatas();
	}

	private void initViews() {
		juBaoView=findViewById(R.id.juBaoView);
		addBlackListView=findViewById(R.id.addBlackListView);
		shareWeiXin = findViewById(R.id.shareWeiXin);
		shareCancel = findViewById(R.id.shareCancel);
		shareQQ = findViewById(R.id.shareQQ);
		shareWeibo = findViewById(R.id.shareWeibo);
		penyouquan=findViewById(R.id.penyouquan);
		qqzone=findViewById(R.id.qqzone);
		copyurl=findViewById(R.id.copyurl);
	}


	private void initListeners() {
		shareWeiXin.setOnClickListener(this);
		shareCancel.setOnClickListener(this);
		shareQQ.setOnClickListener(this);
		shareWeibo.setOnClickListener(this);
		penyouquan.setOnClickListener(this);
		qqzone.setOnClickListener(this);
		copyurl.setOnClickListener(this);
		addBlackListView.setOnClickListener(this);
		juBaoView.setOnClickListener(this);
	}


	private void initDatas() {
		flagFrom=getIntent().getStringExtra("flagFrom");
		userNickName=getIntent().getStringExtra("userNickName");
		subTitle=userNickName.concat(subTitle);
		if ("group".equals(flagFrom)){
			addBlackListView.setVisibility(View.INVISIBLE);
		}
		myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		Intent intent = getIntent();
		if (intent.hasExtra(Constants.SHARE_TITLE_KEY)){
			title = intent.getStringExtra(Constants.SHARE_TITLE_KEY);
		}
		if (intent.hasExtra(Constants.SHARE_SUBTITLE_KEY)) {
			subTitle = intent.getStringExtra(Constants.SHARE_SUBTITLE_KEY);
		}
		if (intent.hasExtra(Constants.SHARE_URL_KEY)){
			shareUrl = intent.getStringExtra(Constants.SHARE_URL_KEY);
		}
		if (intent.hasExtra(Constants.SHARE_ICONURL_KEY)){
			iconUrl = intent.getStringExtra(Constants.SHARE_ICONURL_KEY);
		}
		if (StringUtils.isEmpty(iconUrl)){
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
       		image = new UMImage(this, bitmap);
		} else {
			image = new UMImage(this,iconUrl);
		}
	}

	/**
	 * 重载方法
	 *
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.shareWeiXin) {
			new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
					.withMedia(image).withText(subTitle).withTitle(title).withTargetUrl(shareUrl).share();
		} else if (v.getId() == R.id.shareCancel) {
			finish();
		} else if (v.getId() == R.id.shareQQ) {
			new ShareAction(this).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
					.withMedia(image).withText(subTitle).withTitle(title).withTargetUrl(shareUrl).share();
		} else if (v.getId() == R.id.shareWeibo) {
			new ShareAction(this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
					.withMedia(image).withText(subTitle).withTitle(title).withTargetUrl(shareUrl).share();
		}else if (v.getId()==R.id.penyouquan){
			new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
					.withMedia(image).withText(subTitle).withTitle(title.concat("。").concat(subTitle)).withTargetUrl(shareUrl).share();
		}else if (v.getId()==R.id.qqzone){
			new ShareAction(this).setPlatform(SHARE_MEDIA.QZONE).setCallback(umShareListener)
					.withMedia(image).withText(subTitle).withTitle(title).withTargetUrl(shareUrl).share();
		}else if (v.getId()==R.id.copyurl){
			myClipboard.setPrimaryClip(ClipData.newPlainText("text", shareUrl));
			Toast.makeText(getApplicationContext(), "复制成功",
					Toast.LENGTH_SHORT).show();
			finish();
		}else if (v.getId()==R.id.addBlackListView){
			Intent intent = new Intent();
			intent.putExtra(Constants.COMEBACK, "blackList");
			setResult(Activity.RESULT_OK, intent);
			finish();
		}else  if (v.getId()==R.id.juBaoView){
			Intent intent = new Intent();
			intent.putExtra(Constants.COMEBACK, "jubao");
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}

	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			Toast.makeText(ShareBoardView.this, " 分享成功啦", Toast.LENGTH_SHORT).show();
			finish();
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(ShareBoardView.this," 分享失败啦", Toast.LENGTH_SHORT).show();
			finish();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(ShareBoardView.this," 分享取消了", Toast.LENGTH_SHORT).show();
			finish();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** attention to this below ,must add this**/
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}
}
