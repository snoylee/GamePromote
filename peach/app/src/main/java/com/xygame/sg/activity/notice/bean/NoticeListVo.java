package com.xygame.sg.activity.notice.bean;

import java.util.List;

/**
 * @author tianxr
 * @date 2015年12月10日
 */
public class NoticeListVo {
	private int isCollect;
	private int prepayStatus;
	private NoticeBaseListVo base;
	private List<NoticeRecruitVo> recruit;
	private NoticeShootListVo shoot;
	private NoticeJoinListVo join;

	private boolean isExpand = false;

	public int getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(int isCollect) {
		this.isCollect = isCollect;
	}

	public int getPrepayStatus() {
		return prepayStatus;
	}

	public void setPrepayStatus(int prepayStatus) {
		this.prepayStatus = prepayStatus;
	}

	public NoticeBaseListVo getBase() {
		return base;
	}

	public void setBase(NoticeBaseListVo base) {
		this.base = base;
	}

	public List<NoticeRecruitVo> getRecruit() {
		return recruit;
	}

	public void setRecruit(List<NoticeRecruitVo> recruit) {
		this.recruit = recruit;
	}

	public NoticeShootListVo getShoot() {
		return shoot;
	}

	public void setShoot(NoticeShootListVo shoot) {
		this.shoot = shoot;
	}

	public NoticeJoinListVo getJoin() {
		return join;
	}

	public void setJoin(NoticeJoinListVo join) {
		this.join = join;
	}

	public boolean isExpand() {
		return isExpand;
	}

	public void setIsExpand(boolean isExpand) {
		this.isExpand = isExpand;
	}
}
