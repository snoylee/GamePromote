package com.xygame.sg.activity.personal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.xygame.sg.R;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.activity.notice.JJRNoticeDetailActivity;
import com.xygame.sg.activity.notice.NoticeDetailActivity;
import com.xygame.sg.activity.notice.bean.JJRNoticeBean;
import com.xygame.sg.activity.notice.bean.NoticeBaseListVo;
import com.xygame.sg.activity.notice.bean.NoticeListBean;
import com.xygame.sg.activity.notice.bean.NoticeListVo;
import com.xygame.sg.activity.notice.bean.NoticePublisher;
import com.xygame.sg.activity.notice.bean.NoticeRecruitVo;
import com.xygame.sg.activity.notice.bean.NoticeShootListVo;
import com.xygame.sg.activity.personal.CMPersonInfoActivity;
import com.xygame.sg.activity.personal.CommentAllActivity;
import com.xygame.sg.activity.personal.EditNickNameActivity;
import com.xygame.sg.activity.personal.EditRecordActivity;
import com.xygame.sg.activity.personal.EditSummaryActivity;
import com.xygame.sg.activity.personal.EditorBirthdayActivity;
import com.xygame.sg.activity.personal.EditorBodyInfoActivity;
import com.xygame.sg.activity.personal.EditorCountryActivity;
import com.xygame.sg.activity.personal.EditorProvinceActivity;
import com.xygame.sg.activity.personal.EditorStyleActivity;
import com.xygame.sg.activity.personal.STSEditRecordActivity;
import com.xygame.sg.activity.personal.SelectJobTypeActivity;
import com.xygame.sg.activity.personal.bean.CarrierBean;
import com.xygame.sg.activity.personal.bean.ModelDataVo;
import com.xygame.sg.activity.personal.bean.ModelResumeVo;
import com.xygame.sg.activity.personal.bean.ModelStyleVo;
import com.xygame.sg.activity.personal.bean.UserInfoView;
import com.xygame.sg.activity.personal.bean.UserType;
import com.xygame.sg.bean.comm.TransResumeBean;
import com.xygame.sg.bean.comm.TransStyleBean;
import com.xygame.sg.define.draggrid.DataTools;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.task.utils.AssetDataBaseManager.CityBean;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.DateTime;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.widget.LinearLayoutForListView;
import com.xygame.sg.widget.SelectableRoundedImageView;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2015/11/16. 资料页面的adapter
 */
public class CMDataAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private UserInfoView dataMap;
	private TransStyleBean tsBean;
	private LayoutParams lp,lparms;
	private String countryName;
	private TransResumeBean tsResumeBean;
	private String userHeight = null, userWeight = null, userCup = null, userBust = null, userWaist = null,
			userHip = null, userShoesCode = null;
	private ModelDataVo modelDataVo;
	private NoticeListBean noticeList;
	private String userType;
	private View nullView;
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.considerExifParams(true)
			.displayer(new SimpleBitmapDisplayer())
			.build();

	public void addSYSNoticeList(NoticeListBean noticeList) {
		this.noticeList=noticeList;
	}

	public void addModelDataVo(ModelDataVo modelDataVo){
		this.modelDataVo=modelDataVo;
	}

	/**
	 * 标志是否从查看某个摄影师入口进入
	 */
	private boolean isQuery = false;
	private List<JJRNoticeBean> jjrDatas;
	public void addJJRNoticeList(List<JJRNoticeBean> datas) {
		this.jjrDatas=datas;
	}

	public CMDataAdapter(Context context, UserInfoView dataMap, boolean isQuery) {
		super();
		lparms=new LayoutParams(LayoutParams.MATCH_PARENT,50);
		this.context = context;
		this.dataMap = dataMap;
		inflater = LayoutInflater.from(context);
		this.isQuery = isQuery;
		tsBean = new TransStyleBean();
		lp = new LayoutParams(20, 20);
	}

	public void setData(UserInfoView dataMap,String userType) {
		this.userType=userType;
		this.dataMap = dataMap;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int i) {
		return dataMap;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView( int i,View view, ViewGroup viewGroup) {
		view = inflater.inflate(R.layout.cm_person_data_item, null);
		View noticeTip=view.findViewById(R.id.noticeTip);
		TextView record_tip_tv = (TextView) view.findViewById(R.id.record_tip_tv);
		ImageView record_rl_arrow_iv = (ImageView) view.findViewById(R.id.record_rl_arrow_iv);
		LinearLayout record_rl1 = (LinearLayout) view.findViewById(R.id.record_rl1);
		TextView summary_tip_tv = (TextView) view.findViewById(R.id.summary_tip_tv);
		ImageView summary_arrow_iv = (ImageView) view.findViewById(R.id.summary_arrow_iv);

		ImageView like_male_model_iv = (ImageView) view.findViewById(R.id.like_male_model_iv);
		ImageView like_female_model_iv = (ImageView) view.findViewById(R.id.like_female_model_iv);
		ImageView nick_name_iv = (ImageView) view.findViewById(R.id.nick_name_iv);
		ImageView age_iv = (ImageView) view.findViewById(R.id.age_iv);
		ImageView country_iv = (ImageView) view.findViewById(R.id.country_iv);
		ImageView area_iv = (ImageView) view.findViewById(R.id.area_iv);
		ImageView job_type_iv = (ImageView) view.findViewById(R.id.job_type_iv);

		RelativeLayout trade_num_rl = (RelativeLayout) view.findViewById(R.id.trade_num_rl);
		RatingBar score_rating = (RatingBar) view.findViewById(R.id.score_rating);
		TextView trade_num_tv = (TextView) view.findViewById(R.id.trade_num_tv);
		TextView comment_num_tv = (TextView) view.findViewById(R.id.comment_num_tv);
		LinearLayout noticeViews=(LinearLayout)view.findViewById(R.id.noticeViews);
		LinearLayout noticeJJRViews=(LinearLayout)view.findViewById(R.id.noticeJJRViews);
		RelativeLayout summary_rl = (RelativeLayout) view.findViewById(R.id.summary_rl);
		View showLine=view.findViewById(R.id.showLine);
		View llView=view.findViewById(R.id.llView);
		View commentView=view.findViewById(R.id.commentView);
		RelativeLayout job_type_rl = (RelativeLayout) view.findViewById(R.id.job_type_rl);
		showLine.setVisibility(View.GONE);
		job_type_rl.setVisibility(View.GONE);
		record_tip_tv.setVisibility(View.GONE);
		llView.setVisibility(View.GONE);
		commentView.setVisibility(View.GONE);
		record_rl1.setVisibility(View.GONE);

		if (Constants.CARRE_PHOTOR.equals(userType)){

			showLine.setVisibility(View.VISIBLE);
			job_type_rl.setVisibility(View.VISIBLE);
			record_tip_tv.setVisibility(View.VISIBLE);
			llView.setVisibility(View.VISIBLE);
			commentView.setVisibility(View.VISIBLE);

			if (modelDataVo!=null){
				trade_num_tv.setText(" ("+modelDataVo.getScore().getTradeCount()+")");
				comment_num_tv.setText(" ("+modelDataVo.getScore().getEvlCount()+")");
				score_rating.setRating((float)modelDataVo.getScore().getEvlScore());
				trade_num_rl.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(context, CommentAllActivity.class);
						intent.putExtra("seeUserId", modelDataVo.getSeeUserId()+"");
						List<UserType> utypesList = modelDataVo.getUserTypes();
						UserType utypes = new UserType();
						if (utypesList.size() > 1) {
							utypes = utypesList.get(1);
						} else {
							utypes = utypesList.get(0);
						}
						String userType = utypes.getUtype()+"";
						intent.putExtra("utype",userType);
						context.startActivity(intent);
					}
				});
			}

			if (noticeList!=null){
				noticeJJRViews.setVisibility(View.GONE);
				noticeViews.setVisibility(View.VISIBLE);
				for (int j=0;j<noticeList.getNotices().size();j++){
					View convertView=LayoutInflater.from(context).inflate(R.layout.notice_list_item, null);
					View lastLine=convertView.findViewById(R.id.lastLine);
					LinearLayout notice_list_item_root_ll = (LinearLayout) convertView.findViewById(R.id.notice_list_item_root_ll);
					SelectableRoundedImageView notice_top_bg_iv = (SelectableRoundedImageView) convertView.findViewById(R.id.notice_top_bg_iv);
					ImageView pre_payed_icon_iv = (ImageView) convertView.findViewById(R.id.pre_payed_icon_iv);
					TextView remain_time_tv = (TextView) convertView.findViewById(R.id.remain_time_tv);
					TextView signed_num_tv = (TextView) convertView.findViewById(R.id.signed_num_tv);
					ProgressBar time_progress = (ProgressBar) convertView.findViewById(R.id.time_progress);
					TextView shoot_content_tv = (TextView) convertView.findViewById(R.id.shoot_content_tv);
					TextView time_limit_tv = (TextView) convertView.findViewById(R.id.time_limit_tv);
					TextView address_tv = (TextView) convertView.findViewById(R.id.address_tv);
					TextView cm_num_tv = (TextView) convertView.findViewById(R.id.cm_num_tv);
					RelativeLayout message_rl = (RelativeLayout) convertView.findViewById(R.id.message_rl);
					TextView message_tv = (TextView) convertView.findViewById(R.id.message_tv);
					RelativeLayout publisher_rl = (RelativeLayout) convertView.findViewById(R.id.publisher_rl);
					ImageView cm_avatar_iv = (ImageView) convertView.findViewById(R.id.cm_avatar_iv);
					TextView cm_nick_name_tv = (TextView) convertView.findViewById(R.id.cm_nick_name_tv);
					ImageView identy_iv = (ImageView) convertView.findViewById(R.id.identy_iv);
					LinearLayoutForListView request_lv = (LinearLayoutForListView) convertView.findViewById(R.id.request_lv);
					LinearLayout expand_arrow_ll = (LinearLayout) convertView.findViewById(R.id.expand_arrow_ll);
					LinearLayout collapse_arrow_ll = (LinearLayout) convertView.findViewById(R.id.collapse_arrow_ll);

					FrameLayout.LayoutParams layoutParamsLl = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					if (j ==0){
						layoutParamsLl.setMargins(DataTools.dip2px(context, 10), DataTools.dip2px(context, 10), DataTools.dip2px(context, 10), 0);//4个参数按顺序分别是左上右下
						notice_list_item_root_ll.setLayoutParams(layoutParamsLl);
					} else {
						layoutParamsLl.setMargins(DataTools.dip2px(context, 10), DataTools.dip2px(context, 10), DataTools.dip2px(context, 10), 0);//4个参数按顺序分别是左上右下
					}
					notice_list_item_root_ll.setLayoutParams(layoutParamsLl);

					NoticeListVo noticeListVo = noticeList.getNotices().get(j);

					final NoticeBaseListVo noticeBaseListVo = noticeListVo.getBase();
					//是否预付
					if (noticeListVo.getPrepayStatus() == 3) {
						pre_payed_icon_iv.setVisibility(View.VISIBLE);
					} else {
						pre_payed_icon_iv.setVisibility(View.GONE);
					}
					String bgUrl = "";
//                for (Map map : shootTypetList) {
//                    if (Integer.parseInt(map.get("typeId") + "") == noticeBaseListVo.getShootType()) {
//                        bgUrl = map.get("noticeListBg") + "";
//                        break;
//                    }
//                }
					for (ShootTypeBean bean : SGApplication.getInstance().getTypeList()) {
//                    List<ShootSubTypeBean> subTypeBeanList = bean.getSubTypes();
//                    for (ShootSubTypeBean subTypeBean : subTypeBeanList){
//                        if (subTypeBean.getTypeId() == noticeBaseListVo.getShootType()) {
//                            bgUrl = bean.getNoticeListBg();
//                            break;
//                        }
//                    }
						if (bean.getTypeId() == (noticeBaseListVo.getShootType()/100)*100) {
							bgUrl = bean.getNoticeListBg();
							break;
						}
					}
					if (!StringUtils.isEmpty(bgUrl)) {
//                    imageLoader.loadImageNoThume(bgUrl, holder.notice_top_bg_iv, true);
						com.nostra13.universalimageloader.core.ImageLoader.getInstance()
								.displayImage(bgUrl, notice_top_bg_iv, options);

					}
					//截止时间
					Date joinStartTime = noticeBaseListVo.getJoinStartTime();
					Date joinEndTime = noticeBaseListVo.getJoinEndTime();
					int max = (int)((joinEndTime.getTime()-joinStartTime.getTime())/(1000*60));
					time_progress.setMax(max);
					long curTime = System.currentTimeMillis();
					int progress = 0;
					String remainTimeStr = "报名截止：已截止";
					if ((joinEndTime.getTime()-curTime)>0){
						remainTimeStr = "报名截止："+ DateTime.getLimitTimeStr(joinEndTime.getTime() - curTime);
						progress = (int)((joinEndTime.getTime()-curTime)/(1000*60));
					}
					remain_time_tv.setText(remainTimeStr);
					time_progress.setProgress(progress);

					if(noticeBaseListVo.getPgrapherCount()>0){
						cm_num_tv.setText(noticeBaseListVo.getPgrapherCount() + "位摄影师");
					} else {
						cm_num_tv.setVisibility(View.GONE);
					}

					//已报名人数
					signed_num_tv.setText(noticeListVo.getJoin().getMemCount() + "人");
					//拍摄主题
					shoot_content_tv.setText(noticeBaseListVo.getSubject());
					if (!StringUtils.isEmpty(noticeBaseListVo.getRemark())){
						message_tv.setText(noticeBaseListVo.getRemark());
					} else {
						message_rl.setVisibility(View.GONE);
					}
					final NoticePublisher publisher = noticeBaseListVo.getPublisher();
					cm_nick_name_tv.setText(publisher.getUsernick());
					String cmIcon = publisher.getUserIcon();
					if (!StringUtils.isEmpty(cmIcon)) {
//                    imageLoader.loadImage(cmIcon, holder.cm_avatar_iv, true);
						String path;
						if (cmIcon.contains(Constants.ALIY_IMAGE_DMO)){
							cmIcon = cmIcon.replace(Constants.ALIY_IMAGE_DMO, Constants.LOCAL_IMAGE_DMO);
						}
						if (cmIcon.contains(Constants.WEB_IMAGE_DMO)){
							path=cmIcon.concat(Constants.WEB_SMALL_IMAGE_DMO);
						}else{
							path=cmIcon.concat(Constants.LOCAL_SMAL_IMAGE);
						}
						cm_avatar_iv.setImageResource(R.drawable.moren_icon);
						com.nostra13.universalimageloader.core.ImageLoader.getInstance()
								.displayImage(path, cm_avatar_iv,options);
					}
					if (publisher.getAuthStatus() == 2) {
						identy_iv.setVisibility(View.VISIBLE);
					} else {
						identy_iv.setVisibility(View.GONE);
					}

					NoticeShootListVo noticeShootListVo = noticeListVo.getShoot();
					//开始时间结束时间
					String startTimeStr = new DateTime(noticeShootListVo.getStartTime()).toDateTimeString(DateTime.SLASH_DATE_TIME_HHmm_FORMAT_PATTERN);
					String endTimeStr = new DateTime(noticeShootListVo.getEndTime()).toDateTimeString(DateTime.SLASH_DATE_TIME_HHmm_FORMAT_PATTERN);
					time_limit_tv.setText(startTimeStr + "--" + endTimeStr);
					CityBean it = AssetDataBaseManager.getManager().queryCityById(noticeShootListVo.getAddrCity());
					address_tv.setText(it.getName() + " " + noticeShootListVo.getAddress());

					expand_arrow_ll.setVisibility(View.GONE);
					collapse_arrow_ll.setVisibility(View.GONE);
					lastLine.setVisibility(View.GONE);
					//招募要求
//					List<NoticeRecruitVo> recruitList = noticeListVo.getRecruit();
//
//					if (recruitList!= null&& recruitList.size()>0){
//						request_lv.bindLinearLayout(recruitList,noticeListVo.isExpand());
//						ExpandAndCollapseListener listener = new ExpandAndCollapseListener(j);
//						expand_arrow_ll.setOnClickListener(listener);
//						collapse_arrow_ll.setOnClickListener(listener);
//					}
//
//					if(noticeListVo.isExpand()){//要展开
//						expand_arrow_ll.setVisibility(View.GONE);
//						collapse_arrow_ll.setVisibility(View.VISIBLE);
//					} else {
//						expand_arrow_ll.setVisibility(View.VISIBLE);
//						collapse_arrow_ll.setVisibility(View.GONE);
//					}

					notice_list_item_root_ll.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							Intent intent = new Intent(context, NoticeDetailActivity.class);
							intent.putExtra("noticeId",noticeBaseListVo.getNoticeId());
							intent.putExtra("isQuery",isQuery);
							context.startActivity(intent);
						}
					});
					noticeViews.addView(convertView);
					nullView=new View(context);
					nullView.setLayoutParams(lparms);
					noticeViews.addView(nullView);
				}
			}
		}else if(Constants.CARRE_MERCHANT.equals(userType)) {
			showLine.setVisibility(View.GONE);
			job_type_rl.setVisibility(View.GONE);
			record_tip_tv.setVisibility(View.GONE);
			llView.setVisibility(View.GONE);
			commentView.setVisibility(View.GONE);
			noticeJJRViews.setVisibility(View.VISIBLE);
			noticeViews.setVisibility(View.GONE);
			noticeTip.setVisibility(View.GONE);
			record_rl1.setVisibility(View.VISIBLE);
			List<ModelResumeVo> resumeList = dataMap.getResumes();
			if (resumeList != null && !resumeList.isEmpty()) {
				tsResumeBean = new TransResumeBean();
				tsResumeBean.setResumeList(resumeList);
				for (int j = 0; j < resumeList.size(); j++) {
					ModelResumeVo map = resumeList.get(j);
					View mView = LayoutInflater.from(context).inflate(R.layout.sg_resume_item, null);
					View record_arrow_iv = mView.findViewById(R.id.record_arrow_iv);
					record_arrow_iv.setVisibility(View.GONE);
					TextView resumeText = (TextView) mView.findViewById(R.id.resumeText);
					TextView resumeTimer = (TextView) mView.findViewById(R.id.resumeTimer);
					View bottomLineView = mView.findViewById(R.id.bottomLineView);
					bottomLineView.setVisibility(View.GONE);
					String startTime = "", endTime = "";
					if (map.getStartDate() != null) {
						startTime = CalendarUtils.getXieGongYMDStr(map.getStartDate());
					}
					if (map.getEndDate() != null) {
						endTime = CalendarUtils.getXieGongYMDStr(map.getEndDate());
					}
					String timer = startTime.concat("—").concat(endTime);
					String context = map.getExperDesc();
					resumeTimer.setText(timer);
					resumeText.setText(context);
					record_rl1.addView(mView);
				}

			} else {
				if (isQuery) {
					View mView = LayoutInflater.from(context).inflate(R.layout.sg_resume_item, null);
					TextView resumeTimer = (TextView) mView.findViewById(R.id.resumeTimer);
					View bottomLineView = mView.findViewById(R.id.bottomLineView);
					View record_arrow_iv = mView.findViewById(R.id.record_arrow_iv);
					record_arrow_iv.setVisibility(View.GONE);
					bottomLineView.setVisibility(View.GONE);
					resumeTimer.setHint("暂无履历");
					record_rl1.addView(mView);
				} else {
					View mView = LayoutInflater.from(context).inflate(R.layout.sg_resume_item, null);
					View bottomLineView = mView.findViewById(R.id.bottomLineView);
					View record_arrow_iv = mView.findViewById(R.id.record_arrow_iv);
					record_arrow_iv.setVisibility(View.VISIBLE);
					bottomLineView.setVisibility(View.GONE);
					record_rl1.addView(mView);
				}
			}

			if (jjrDatas!=null){
				noticeTip.setVisibility(View.VISIBLE);
				for (final JJRNoticeBean item:jjrDatas){
					View convertView = LayoutInflater.from(context).inflate(R.layout.jjr_notice_list_item, null);
					View notice_list_item_root_ll=convertView.findViewById(R.id.notice_list_item_root_ll);
					View lastLine=convertView.findViewById(R.id.lastLine);
					SelectableRoundedImageView notice_top_bg_iv = (SelectableRoundedImageView) convertView.findViewById(R.id.notice_top_bg_iv);
					TextView jjrSubject = (TextView) convertView.findViewById(R.id.jjrSubject);
					TextView signed_num_tv = (TextView) convertView.findViewById(R.id.signed_num_tv);
					TextView jjrContent = (TextView) convertView.findViewById(R.id.jjrContent);
					TextView jjrContent1 = (TextView) convertView.findViewById(R.id.jjrContent1);
					ImageView cm_avatar_iv = (ImageView) convertView.findViewById(R.id.cm_avatar_iv);
					TextView cm_nick_name_tv = (TextView) convertView.findViewById(R.id.cm_nick_name_tv);
					LinearLayout expand_arrow_ll = (LinearLayout) convertView.findViewById(R.id.expand_arrow_ll);
					LinearLayout collapse_arrow_ll = (LinearLayout) convertView.findViewById(R.id.collapse_arrow_ll);
					notice_top_bg_iv.setImageResource(R.drawable.notice_top_bg);
					jjrSubject.setText(item.getSubject());
					signed_num_tv.setText(item.getApplyCount().concat("人"));
					cm_nick_name_tv.setText(item.getPublisher().getUsernick());
					String userImage=item.getPublisher().getUserIcon();
					if (!StringUtils.isEmpty(userImage)) {
						com.nostra13.universalimageloader.core.ImageLoader.getInstance()
								.displayImage(userImage, cm_avatar_iv,options);
					}else{
						cm_avatar_iv.setImageResource(R.drawable.new_system_icon);
					}
					jjrContent.setVisibility(View.GONE);
					jjrContent1.setVisibility(View.VISIBLE);
					jjrContent1.setText(item.getNoticeContent());
					expand_arrow_ll.setVisibility(View.GONE);
					collapse_arrow_ll.setVisibility(View.GONE);
					lastLine.setVisibility(View.GONE);
//					ExpandAndCollapseListener listener = new ExpandAndCollapseListener(i);
//					expand_arrow_ll.setOnClickListener(listener);
//					collapse_arrow_ll.setOnClickListener(listener);
//					if(item.isExpand()){//要展开
//						jjrContent.setVisibility(View.GONE);
//						jjrContent1.setVisibility(View.VISIBLE);
//						jjrContent1.setText(item.getNoticeContent());
//						expand_arrow_ll.setVisibility(View.GONE);
//						collapse_arrow_ll.setVisibility(View.VISIBLE);
//					} else {
//						jjrContent1.setVisibility(View.GONE);
//						jjrContent.setVisibility(View.VISIBLE);
//						jjrContent.setText(item.getNoticeContent());
//						jjrContent.setLines(3);
//						expand_arrow_ll.setVisibility(View.VISIBLE);
//						collapse_arrow_ll.setVisibility(View.GONE);
//					}
					cm_avatar_iv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							Intent intent = new Intent(context, CMPersonInfoActivity.class);
							if (UserPreferencesUtil.isOnline(context)&&UserPreferencesUtil.getUserId(context).equals(item.getPublisher().getUserId()+"")){
								intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.EDIT_INFO_FLAG);
							} else {
								intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.QUERY_INFO_FLAG);
								intent.putExtra("userId",item.getPublisher().getUserId()+"");
								intent.putExtra("userNick",item.getPublisher().getUsernick());
							}
							context.startActivity(intent);
						}
					});
					notice_list_item_root_ll.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context, JJRNoticeDetailActivity.class);
							intent.putExtra("noticeId",item.getNoticeId());
							context.startActivity(intent);
						}
					});
					noticeJJRViews.addView(convertView);
					nullView=new View(context);
					nullView.setLayoutParams(lparms);
					noticeJJRViews.addView(nullView);
				}
			}
		}

		if (isQuery) {
			record_tip_tv.setText("履历");
			summary_tip_tv.setText("简介");
			like_male_model_iv.setVisibility(View.GONE);
			like_female_model_iv.setVisibility(View.GONE);
			summary_arrow_iv.setVisibility(View.GONE);
			nick_name_iv.setVisibility(View.GONE);
			age_iv.setVisibility(View.GONE);
			country_iv.setVisibility(View.GONE);
			area_iv.setVisibility(View.GONE);
			job_type_iv.setVisibility(View.GONE);
		}

		TextView like_male_model_tv = (TextView) view.findViewById(R.id.like_male_model_tv);
		LinearLayout like_male_model_ll = (LinearLayout) view.findViewById(R.id.like_male_model_ll);
		TextView like_female_model_tv = (TextView) view.findViewById(R.id.like_female_model_tv);
		LinearLayout like_female_model_ll = (LinearLayout) view.findViewById(R.id.like_female_model_ll);
		View like_female_model_rl = view.findViewById(R.id.like_female_model_rl);
		View like_male_model_rl = view.findViewById(R.id.like_male_model_rl);

		if (isQuery) {
			like_male_model_tv.setHint("还未选择");
			like_female_model_tv.setHint("还未选择");
		}

		List<ModelStyleVo> styleList = dataMap.getStyles();
		tsBean.setStyleList(styleList);
		if (styleList != null && !styleList.isEmpty()) {

			boolean manStyle = false, womanStyle = false;
			for (ModelStyleVo it : styleList) {

				if ("1".equals(it.getExclusType()+"")) {
					manStyle = true;
					View convertView = LayoutInflater.from(context).inflate(R.layout.sg_sytle_item_, null);
					RoundTextView styleText = (RoundTextView) convertView.findViewById(R.id.styleText);
					styleText.setText(it.getStyleName());
					styleText.setTextColor(Color.rgb(it.getHueR(), it.getHueG(), it.getHueB()));
					styleText.getDelegate().setStrokeColor(Color.rgb(it.getHueR(), it.getHueG(), it.getHueB()));
					View mView = new View(context);
					mView.setLayoutParams(lp);
					like_male_model_ll.addView(convertView);
					like_male_model_ll.addView(mView);
				} else if ("0".equals(it.getExclusType()+"")) {
					womanStyle = true;
					View convertView = LayoutInflater.from(context).inflate(R.layout.sg_sytle_item_, null);
					RoundTextView styleText = (RoundTextView) convertView.findViewById(R.id.styleText);
					styleText.setText(it.getStyleName());
					styleText.setTextColor(Color.rgb(it.getHueR(), it.getHueG(), it.getHueB()));
					styleText.getDelegate().setStrokeColor(Color.rgb(it.getHueR(), it.getHueG(), it.getHueB()));
					View mView = new View(context);
					mView.setLayoutParams(lp);
					like_female_model_ll.addView(convertView);
					like_female_model_ll.addView(mView);
				}

				if (manStyle) {
					like_male_model_tv.setVisibility(View.GONE);
				}
				if (womanStyle) {
					like_female_model_tv.setVisibility(View.GONE);
				}
			}
		}



		LinearLayout record_rl = (LinearLayout) view.findViewById(R.id.record_rl);
		List<ModelResumeVo> resumeList = dataMap.getResumes();
		if (resumeList != null && !resumeList.isEmpty()) {
			if (isQuery) {
				record_rl_arrow_iv.setVisibility(View.GONE);
			} else {
				record_rl_arrow_iv.setVisibility(View.VISIBLE);
			}

			tsResumeBean = new TransResumeBean();
			tsResumeBean.setResumeList(resumeList);
			for (int j = 0; j < resumeList.size(); j++) {
				ModelResumeVo map = resumeList.get(j);
				View mView = LayoutInflater.from(context).inflate(R.layout.sg_resume_item, null);
				View record_arrow_iv = mView.findViewById(R.id.record_arrow_iv);
				record_arrow_iv.setVisibility(View.GONE);
				TextView resumeText = (TextView) mView.findViewById(R.id.resumeText);
				TextView resumeTimer = (TextView) mView.findViewById(R.id.resumeTimer);
				View bottomLineView = mView.findViewById(R.id.bottomLineView);
				bottomLineView.setVisibility(View.GONE);
				String startTime = "", endTime = "";
				if (map.getStartDate() != null) {
					startTime = CalendarUtils.getXieGongYMDStr(map.getStartDate());
				}
				if (map.getEndDate() != null) {
					endTime = CalendarUtils.getXieGongYMDStr(map.getEndDate());
				}
				String timer = startTime.concat("—").concat(endTime);
				String context = map.getExperDesc();
				resumeTimer.setText(timer);
				resumeText.setText(context);
				record_rl.addView(mView);
			}

		} else {
			if (isQuery) {
				record_rl_arrow_iv.setVisibility(View.GONE);
				View mView = LayoutInflater.from(context).inflate(R.layout.sg_resume_item, null);
				TextView resumeTimer = (TextView) mView.findViewById(R.id.resumeTimer);
				View bottomLineView = mView.findViewById(R.id.bottomLineView);
				View record_arrow_iv = mView.findViewById(R.id.record_arrow_iv);
				record_arrow_iv.setVisibility(View.GONE);
				bottomLineView.setVisibility(View.GONE);
				resumeTimer.setHint("暂无履历");
				record_rl.addView(mView);
			} else {
				record_rl_arrow_iv.setVisibility(View.GONE);
				View mView = LayoutInflater.from(context).inflate(R.layout.sg_resume_item, null);
				View bottomLineView = mView.findViewById(R.id.bottomLineView);
				View record_arrow_iv = mView.findViewById(R.id.record_arrow_iv);
				record_arrow_iv.setVisibility(View.VISIBLE);
				bottomLineView.setVisibility(View.GONE);
				record_rl.addView(mView);
			}
		}
		TextView summary_tv = (TextView) view.findViewById(R.id.summary_tv);
		if (isQuery) {
			summary_tv.setHint("暂无简介");
		}
		final String summary = dataMap.getIntroDesc();
		if (!TextUtils.isEmpty(summary) && !summary.equals("null")) {
			summary_tv.setText(summary);
		}


		TextView nickname_tv = (TextView) view.findViewById(R.id.nickname_tv);
		TextView mofan_num_tv = (TextView) view.findViewById(R.id.mofan_num_tv);
		TextView sex_tv = (TextView) view.findViewById(R.id.sex_tv);
		TextView age_tv = (TextView) view.findViewById(R.id.age_tv);
		TextView country_tv = (TextView) view.findViewById(R.id.country_tv);
		TextView area_tv = (TextView) view.findViewById(R.id.area_tv);
		TextView job_type_tv = (TextView) view.findViewById(R.id.job_type_tv);


		String usernick = dataMap.getUsernick();
		if (!TextUtils.isEmpty(usernick) && !usernick.equals("null")) {
			nickname_tv.setText(usernick);
		}
		String mofan_num = dataMap.getUserPin();
		if (!TextUtils.isEmpty(mofan_num) && !mofan_num.equals("null")) {
			mofan_num_tv.setText(mofan_num);
		}
		String sex = dataMap.getGender()+"";
		if (!TextUtils.isEmpty(sex) && !sex.equals("null")) {

			if (sex.equals(Constants.SEX_MAN)) {
				sex_tv.setText("男");
			} else {
				sex_tv.setText("女");
			}
		}


		String age = dataMap.getAge()+"";
		if (!TextUtils.isEmpty(age) && !age.equals("null")) {
			age_tv.setText(age.concat("岁"));
		}
		String country = dataMap.getCountry();
		if (!TextUtils.isEmpty(country) && !country.equals("null")) {
			List<Map<String, String>> datas = Constants.COUNTRY_DATA;
			for (Map<String, String> it : datas) {
				if (it.get("country_code").equals(country)) {
					countryName = it.get("country_name");
					country_tv.setText(countryName);
				}
			}
		}
		String area = dataMap.getCity()+"";
		if (!TextUtils.isEmpty(area) && !area.equals("null")) {
			CityBean it = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(area));
			area_tv.setText(it.getName());
		}
		String job_type = dataMap.getOccupType()+"";
		List<CarrierBean> datas = SGApplication.getInstance().getCmCarriers();
		if (!"null".equals(datas) && datas != null) {
			if (!TextUtils.isEmpty(job_type) && !job_type.equals("null")) {
				for (CarrierBean it : datas) {
					if (it.getTypeId().equals(job_type)) {
						job_type_tv.setText(it.getCarrierName());
					}
				}
			}
		}



		if (!isQuery) {
			like_male_model_rl.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(context, EditorStyleActivity.class);
					intent.putExtra("bean", tsBean);
					intent.putExtra("typeFlag", "male");
					context.startActivity(intent);
				}
			});

			like_female_model_rl.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(context, EditorStyleActivity.class);
					intent.putExtra("bean", tsBean);
					intent.putExtra("typeFlag", "female");
					context.startActivity(intent);
				}
			});

			RelativeLayout nick_name_rl = (RelativeLayout) view.findViewById(R.id.nick_name_rl);
			nick_name_rl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(context, EditNickNameActivity.class);
					context.startActivity(intent);
				}
			});

			job_type_rl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(context, SelectJobTypeActivity.class);
					intent.putExtra("strFlag", dataMap.getOccupType()+"");
					context.startActivity(intent);
				}
			});
			View userinfoAge = view.findViewById(R.id.userinfoAge);
			userinfoAge.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, EditorBirthdayActivity.class);
					context.startActivity(intent);
				}
			});
			View userInfoCountry = view.findViewById(R.id.userInfoCountry);
			userInfoCountry.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, EditorCountryActivity.class);
					intent.putExtra("data", countryName);
					context.startActivity(intent);
				}
			});
			View userInfoCity = view.findViewById(R.id.userInfoCity);
			userInfoCity.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, EditorProvinceActivity.class);
					intent.putExtra("noLimitFlag", true);
					context.startActivity(intent);
				}
			});
			record_rl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(context, STSEditRecordActivity.class);
					intent.putExtra("data", tsResumeBean);
					intent.putExtra("summary", summary);
					context.startActivity(intent);
				}
			});
			record_rl1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(context, EditRecordActivity.class);
					intent.putExtra("data", tsResumeBean);
					intent.putExtra("summary", summary);
					context.startActivity(intent);
				}
			});
			if (Constants.CARRE_PHOTOR.equals(userType)){
				summary_rl.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(context, EditSummaryActivity.class);
						intent.putExtra("summary", summary);
						context.startActivity(intent);
					}
				});
			}else if(Constants.CARRE_MERCHANT.equals(userType)) {
				summary_rl.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(context, EditRecordActivity.class);
						intent.putExtra("summary", summary);
						intent.putExtra("data", tsResumeBean);
						context.startActivity(intent);
					}
				});
			}
		}

		return view;
	}

//	class ExpandAndCollapseListener implements View.OnClickListener {
//		private int i;
//		public ExpandAndCollapseListener(int i) {
//			this.i = i;
//		}
//		@Override
//		public void onClick(View view) {
//			if (view.getId() == R.id.expand_arrow_ll) {
//				noticeList.getNotices().get(i-1).setIsExpand(true);
//			} else if (view.getId() == R.id.collapse_arrow_ll) {
//				noticeList.getNotices().get(i-1).setIsExpand(false);
//			}
//			notifyDataSetChanged();
//		}
//	}
}
