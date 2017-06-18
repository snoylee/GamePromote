package com.stevenhu.android.phone.bean;

import com.xygame.sg.activity.model.bean.BannerBean;

/**
 * 描述：广告信息</br>
 */
public class ADInfo {
	
	String id = "";
	String url = "";
	String content = "";
	String type = "";
	BannerBean bannerBean;

	public BannerBean getBannerBean() {
		return bannerBean;
	}

	public void setBannerBean(BannerBean bannerBean) {
		this.bannerBean = bannerBean;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
