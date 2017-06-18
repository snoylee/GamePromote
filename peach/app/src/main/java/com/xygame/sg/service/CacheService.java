package com.xygame.sg.service;

import com.xygame.second.sg.Group.bean.GroupBean;
import com.xygame.second.sg.Group.bean.GroupNoticeTip;
import com.xygame.second.sg.biggod.bean.PriceBean;
import com.xygame.second.sg.comm.bean.GiftBean;
import com.xygame.second.sg.comm.bean.NoticeStatusManagerBean;
import com.xygame.second.sg.comm.bean.SystemServiceBean;
import com.xygame.second.sg.jinpai.JinPaiLowerPriceBean;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.jinpai.bean.PriceStepBean;
import com.xygame.second.sg.personal.bean.MingXinCacheBean;
import com.xygame.second.sg.personal.bean.StoneMXItemBean;
import com.xygame.second.sg.personal.blacklist.BlackMemberBean;
import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupBean;
import com.xygame.second.sg.xiadan.bean.GodQiangDanRebackBean;
import com.xygame.sg.activity.model.bean.BannerBean;
import com.xygame.sg.bean.comm.ProvinceBean;

import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tony on 2016/6/15.
 */
public class CacheService {
    private static CacheService instance;

    public static CacheService getInstance() {
        if (instance==null){
            synchronized (CacheService.class){
                if (instance==null){
                    instance=new CacheService();
                }
            }
        }
        return instance;
    }

    private final Map<String, List<JinPaiBigTypeBean>> cacheActTypes = new HashMap<>();
    private final Map<String, MingXinCacheBean> cacheDatas = new HashMap<>();
    private final Map<String, List<StoneMXItemBean>> cacheDatas2 = new HashMap<>();
    private final Map<String,String> cacheVideo = new HashMap<>();
    private final Map<String, List<JinPaiBigTypeBean>> cacheActTypes_ = new HashMap<>();
    private final Map<String, ProvinceBean> cacheAreaDatas= new HashMap<>();
    private final Map<String, ProvinceBean> giftProviceBean= new HashMap<>();
    private final Map<String, ProvinceBean> cacheCommenAreaDatas= new HashMap<>();
    private final Map<String, List<GiftBean>> GiftDatas= new HashMap<>();
    private final Map<String, List<PriceStepBean>> priceStepDatas= new HashMap<>();
    private final Map<String, List<JinPaiLowerPriceBean>> jbLowerPriceDatas= new HashMap<>();
    private final Map<String, NoticeStatusManagerBean> noticeStatusManagerBean= new HashMap<>();
    private final Map<String, List<GroupBean>> groupDatas= new HashMap<>();
    private final Map<String, Map<String,MultiUserChat>> groupRooms= new HashMap<>();
    private final Map<String, List<BlackMemberBean>> blackListDatas= new HashMap<>();
    private final Map<String, Boolean> userStatus= new HashMap<>();
    private final Map<String, String> accessToken= new HashMap<>();
    private final Map<String, String> userId= new HashMap<>();
    private final Map<String, SystemServiceBean> serviceBean= new HashMap<>();
    private final Map<String, List<GZ_GroupBean>> gzGroupDatas= new HashMap<>();
    private final Map<String, List<GroupNoticeTip>> groupNoticeTip= new HashMap<>();
    private final Map<String, List<GroupBean>> discGroupDatas= new HashMap<>();
    private final Map<String, Map<String,MultiUserChat>> discFroupRooms= new HashMap<>();
    private final Map<String, List<PriceBean>> cachePriceDatas = new HashMap<>();
    private final Map<String, List<BannerBean>> cacheBannerBeanDatas = new HashMap<>();
    private final Map<String, List<GodQiangDanRebackBean>> godQiangDanRebackBeanDatas = new HashMap<>();

    public void clearGodQiangDanRebackBeanCache(){
        godQiangDanRebackBeanDatas.clear();
    }

    public void cacheGodQiangDanRebackBean(String key,List<GodQiangDanRebackBean> value){
        godQiangDanRebackBeanDatas.put(key,value);
    }

    public List<GodQiangDanRebackBean> getGodQiangDanRebackBean(String key) {
        return godQiangDanRebackBeanDatas.get(key);
    }

    public void cacheBannerBeanDatas(String key,List<BannerBean> value){
        cacheBannerBeanDatas.put(key,value);
    }

    public List<BannerBean> getCacheBannerBeanDatas(String key) {
        return cacheBannerBeanDatas.get(key);
    }

    public void cachePriceDatas(String key,List<PriceBean> value){
        cachePriceDatas.put(key,value);
    }

    public List<PriceBean> getCachePriceDatas(String key) {
        return cachePriceDatas.get(key);
    }

    public void cacheDiscGroupRoomDatas(String key,Map<String,MultiUserChat> value){
        discFroupRooms.put(key,value);
    }

    public Map<String,MultiUserChat> getCacheDiscGroupRoomDatas(String key) {
        return discFroupRooms.get(key);
    }

    public void cacheDiscGroupDatas(String key,List<GroupBean> value){
        discGroupDatas.put(key,value);
    }

    public List<GroupBean> getCacheDiscGroupDatas(String key) {
        return discGroupDatas.get(key);
    }

    public void cacheGroupNoticeTip(String key,List<GroupNoticeTip> value){
        groupNoticeTip.put(key,value);
    }

    public List<GroupNoticeTip> getCacheGroupNoticeTip(String key) {
        return groupNoticeTip.get(key);
    }

    public void cacheGroupDatasDatas(String key,List<GZ_GroupBean> value){
        gzGroupDatas.put(key,value);
    }

    public List<GZ_GroupBean> getCacheGroupDatasDatas(String key) {
        return gzGroupDatas.get(key);
    }

    public void cacheSystemServiceBean(String key,SystemServiceBean value){
        serviceBean.put(key,value);
    }

    public SystemServiceBean getCacheSystemServiceBean(String key) {
        return serviceBean.get(key);
    }

    public void cacheUserId(String key,String value){
        userId.put(key,value);
    }

    public String getCacheUserId(String key) {
        return userId.get(key);
    }

    public void cacheAccessToken(String key,String value){
        accessToken.put(key,value);
    }

    public String getCacheAccessToken(String key) {
        return accessToken.get(key);
    }

    public void cacheUserStatus(String key,Boolean value){
        userStatus.put(key,value);
    }

    public Boolean getCacheUserStatus(String key) {
        boolean flag=false;
        if (userStatus.containsKey(key)){
            flag=userStatus.get(key);
        }
        return flag;
    }

    public void cacheBlackListDatasDatas(String key,List<BlackMemberBean> value){
        blackListDatas.put(key,value);
    }

    public List<BlackMemberBean> getCacheBlackListDatasDatas(String key) {
        return blackListDatas.get(key);
    }

    public void cacheGroupRoomDatas(String key,Map<String,MultiUserChat> value){
        groupRooms.put(key,value);
    }

    public Map<String,MultiUserChat> getCacheGroupRoomDatas(String key) {
        return groupRooms.get(key);
    }

    public void cacheGroupDatas(String key,List<GroupBean> value){
        groupDatas.put(key,value);
    }

    public List<GroupBean> getCacheGroupDatas(String key) {
        return groupDatas.get(key);
    }

    public void cacheNoticeStatusManagerBean(String key,NoticeStatusManagerBean value){
        noticeStatusManagerBean.put(key,value);
    }

    public NoticeStatusManagerBean getCacheNoticeStatusManagerBean(String key) {
        return noticeStatusManagerBean.get(key);
    }

    public void cacheDatas(String key, MingXinCacheBean value) {
        cacheDatas.put(key, value);
    }

    public void cacheDatas2(String key,List<StoneMXItemBean> value){
        cacheDatas2.put(key,value);
    }

    public MingXinCacheBean getCacheDatas(String key) {
        return cacheDatas.get(key);
    }

    public List<StoneMXItemBean> getCacheDatas2(String key) {
        return cacheDatas2.get(key);
    }

    public void cacheActDatas(String key,List<JinPaiBigTypeBean> value){
        cacheActTypes.put(key,value);
    }

    public List<JinPaiBigTypeBean> getCacheActDatas(String key) {
        return cacheActTypes.get(key);
    }

    public void cacheVideo(String key,String value){
        cacheVideo.put(key,value);
    }

    public String getCacheVideo(String key) {
        return cacheVideo.get(key);
    }

    public void cacheActDatas_(String key,List<JinPaiBigTypeBean> value){
        cacheActTypes_.put(key,value);
    }

    public List<JinPaiBigTypeBean> getCacheActDatas_(String key) {
        return cacheActTypes_.get(key);
    }

    public void cacheCommenAreaBean(String key,ProvinceBean value){
        cacheCommenAreaDatas.put(key,value);
    }

    public ProvinceBean getCacheCommenAreaBean(String key) {
        return cacheCommenAreaDatas.get(key);
    }

    public void cacheJPProvinceBean(String key,ProvinceBean value){
        cacheAreaDatas.put(key,value);
    }

    public ProvinceBean getCacheJPProvinceBean(String key) {
        return cacheAreaDatas.get(key);
    }

    public void cacheGiftProvinceBean(String key,ProvinceBean value){
        giftProviceBean.put(key,value);
    }

    public ProvinceBean getCacheGiftProvinceBean(String key) {
        return giftProviceBean.get(key);
    }


    public void cacheGiftDatas(String key,List<GiftBean> value){
        GiftDatas.put(key,value);
    }

    public List<GiftBean> getCacheGiftDatas(String key) {
        return GiftDatas.get(key);
    }

    public void cachePriceStep(String key,List<PriceStepBean> value){
        priceStepDatas.put(key,value);
    }

    public List<PriceStepBean> getCachePriceStep(String key) {
        return priceStepDatas.get(key);
    }

    public void cacheJbLowerPricePrice(String key,List<JinPaiLowerPriceBean> value){
        jbLowerPriceDatas.put(key,value);
    }

    public List<JinPaiLowerPriceBean> getCacheJbLowerPrice(String key) {
        return jbLowerPriceDatas.get(key);
    }

    public void clearCacheMingXiDatas(){
        cacheVideo.clear();
        cacheDatas.clear();
        cacheDatas2.clear();
        cacheActTypes.clear();
        cacheActTypes_.clear();
        cacheAreaDatas.clear();
        GiftDatas.clear();
        priceStepDatas.clear();
        jbLowerPriceDatas.clear();
        noticeStatusManagerBean.clear();
        groupRooms.clear();
        blackListDatas.clear();
        serviceBean.clear();
//        gzGroupDatas.clear();
        groupNoticeTip.clear();
    }
}
