/**
 * 
 */
package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 	zhoujian
 * @date	2015年12月18日 下午4:53:46
 * @desc	
 */
public class NoticeMemberStatsVo implements Serializable {
	
	private static final long serialVersionUID = 845034929030133883L;

	private List<NoticeMemberGroupVo> groups = new ArrayList<NoticeMemberGroupVo>();
	
	private List<NoticeMemberVo> members = new ArrayList<NoticeMemberVo>();

	public List<NoticeMemberGroupVo> getGroups() {
		return groups;
	}

	public void setGroups(List<NoticeMemberGroupVo> groups) {
		this.groups = groups;
	}

	public List<NoticeMemberVo> getMembers() {
		return members;
	}

	public void setMembers(List<NoticeMemberVo> members) {
		this.members = members;
	}
}
