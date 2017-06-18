/**
 * 
 */
package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

/**
 * @author 	zhoujian
 * @date	2015年12月8日 下午4:46:32
 * @desc	招募对象
 */
public class NoticeRecruitVo extends NoticeRecruitListVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2103389716491066161L;
//	private long recruitId;
//	private int gender;
//	private int count;
//	private long reward;
//	private String country;
//	private int province;
//	private int city;
//	private int maxAge;
//	private int minAge;
//	private int maxHeight;
//	private int minHeight;
//	private int maxWeight;
//	private int minWeight;
//	private int maxBust;
//	private int minBust;
//	private int maxWaist;
//	private int minWaist;
//	private int maxHip;
//	private int minHip;
//	private int maxCup;
//	private int minCup;
//	private int maxShoescode;
//	private int minShoescode;
//	private int affordTravelFee;
//	private int affordAccomFee;
	private String remark;
	private int applied;//是否已报名该招募条件
	private int applySum;//报名人数
	private int okSum;//已录取人数
	private Integer  locIndex;
	private Integer  recordStatus;//录用情况：0，未报名，1：报名；2：待定；3：淘汰；4：录用

//	public long getRecruitId() {
//		return recruitId;
//	}
//	public void setRecruitId(long recruitId) {
//		this.recruitId = recruitId;
//	}
//	public int getGender() {
//		return gender;
//	}
//	public void setGender(int gender) {
//		this.gender = gender;
//	}
//	public int getCount() {
//		return count;
//	}
//	public void setCount(int count) {
//		this.count = count;
//	}
//	public long getReward() {
//		return reward;
//	}
//	public void setReward(long reward) {
//		this.reward = reward;
//	}
//	public String getCountry() {
//		return country;
//	}
//	public void setCountry(String country) {
//		this.country = country;
//	}
//	public int getProvince() {
//		return province;
//	}
//	public void setProvince(int province) {
//		this.province = province;
//	}
//	public int getCity() {
//		return city;
//	}
//	public void setCity(int city) {
//		this.city = city;
//	}
//	public int getMaxAge() {
//		return maxAge;
//	}
//	public void setMaxAge(int maxAge) {
//		this.maxAge = maxAge;
//	}
//	public int getMinAge() {
//		return minAge;
//	}
//	public void setMinAge(int minAge) {
//		this.minAge = minAge;
//	}
//	public int getMaxHeight() {
//		return maxHeight;
//	}
//	public void setMaxHeight(int maxHeight) {
//		this.maxHeight = maxHeight;
//	}
//	public int getMinHeight() {
//		return minHeight;
//	}
//	public void setMinHeight(int minHeight) {
//		this.minHeight = minHeight;
//	}
//	public int getMaxWeight() {
//		return maxWeight;
//	}
//	public void setMaxWeight(int maxWeight) {
//		this.maxWeight = maxWeight;
//	}
//	public int getMinWeight() {
//		return minWeight;
//	}
//	public void setMinWeight(int minWeight) {
//		this.minWeight = minWeight;
//	}
//	public int getMaxBust() {
//		return maxBust;
//	}
//	public void setMaxBust(int maxBust) {
//		this.maxBust = maxBust;
//	}
//	public int getMinBust() {
//		return minBust;
//	}
//	public void setMinBust(int minBust) {
//		this.minBust = minBust;
//	}
//	public int getMaxWaist() {
//		return maxWaist;
//	}
//	public void setMaxWaist(int maxWaist) {
//		this.maxWaist = maxWaist;
//	}
//	public int getMinWaist() {
//		return minWaist;
//	}
//	public void setMinWaist(int minWaist) {
//		this.minWaist = minWaist;
//	}
//	public int getMaxHip() {
//		return maxHip;
//	}
//	public void setMaxHip(int maxHip) {
//		this.maxHip = maxHip;
//	}
//	public int getMinHip() {
//		return minHip;
//	}
//	public void setMinHip(int minHip) {
//		this.minHip = minHip;
//	}
//	public int getMaxCup() {
//		return maxCup;
//	}
//	public void setMaxCup(int maxCup) {
//		this.maxCup = maxCup;
//	}
//	public int getMinCup() {
//		return minCup;
//	}
//	public void setMinCup(int minCup) {
//		this.minCup = minCup;
//	}
//	public int getMaxShoescode() {
//		return maxShoescode;
//	}
//	public void setMaxShoescode(int maxShoescode) {
//		this.maxShoescode = maxShoescode;
//	}
//	public int getMinShoescode() {
//		return minShoescode;
//	}
//	public void setMinShoescode(int minShoescode) {
//		this.minShoescode = minShoescode;
//	}
//	public int getAffordTravelFee() {
//		return affordTravelFee;
//	}
//	public void setAffordTravelFee(int affordTravelFee) {
//		this.affordTravelFee = affordTravelFee;
//	}
//	public int getAffordAccomFee() {
//		return affordAccomFee;
//	}
//	public void setAffordAccomFee(int affordAccomFee) {
//		this.affordAccomFee = affordAccomFee;
//	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getApplied() {
		return applied;
	}
	public void setApplied(int applied) {
		this.applied = applied;
	}
	public int getApplySum() {
		return applySum;
	}
	public void setApplySum(int applySum) {
		this.applySum = applySum;
	}
	public int getOkSum() {
		return okSum;
	}
	public void setOkSum(int okSum) {
		this.okSum = okSum;
	}

	public Integer getLocIndex() {
		return locIndex;
	}

	public void setLocIndex(Integer locIndex) {
		this.locIndex = locIndex;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
}
