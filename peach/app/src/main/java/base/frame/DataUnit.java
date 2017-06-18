package base.frame;

import com.xygame.sg.utils.Constants;

import java.util.Map;
import java.util.TreeMap;

import base.action.CenterRepo;

/**
 * Created by minhua on 2015/10/26.
 */
public class DataUnit {
    private Map<String, Object> repo = new TreeMap<String, Object>();

    public Map<String, Object> getRepo() {
        return repo;
    }

    public void putinRepo(Map<String, Object> addeddata) {
        repo.putAll(addeddata);
    }

    public String baseUrl = Constants.baseUrl;
    public Constants.Configure configure = Constants.Configure.getInstance();
    {
        configure.setDataUnit(this);
    }
    public void clear(){
        getRepo().clear();
        init();
    }

    public void init() {

        getRepo().put("baseurl", baseUrl);
        // 获取注册验证码
        getRepo()
                .put("user_sendRegVerifyCode",
                        baseUrl + "/user/sendRegVerifyCode");
        // 验证验证与手机号是否合法
        getRepo()
                .put("user_verifyPhoneCode", baseUrl + "/user/verifyPhoneCode");
        // 注册接口
        getRepo()
                .put("user_register", baseUrl + "/user/register");
        // 阿里云密钥信息接口
        getRepo()
                .put("ali_params", baseUrl + "/sys/queryAliOSSKey");
        // 登录接口
        getRepo()
                .put("sg_login", baseUrl + "/user/login");
        // 重置密码验证码获取
        getRepo()
                .put("user_forgetVerifyCode",
                        baseUrl + "/user/sendResetVerifyCode");
        // 提交新密码
        getRepo()
                .put("user_commitnewpwssword", baseUrl + "/user/resetPassword");

        // 百度定位服务地址
        getRepo()
                .put("baidu_location_service", baseUrl + "/user/reportCoord");
        getRepo()
                .put("authModelInfo", baseUrl + "/auth/authModelInfo");
        //编辑头像
        getRepo()
                .put("editUserLogo", baseUrl + "/me/editUserLogo");

        //模特创建作品
        getRepo()
                .put("createModelGallery", baseUrl + "/me/createModelGallery");

        //查询模特作品集
        getRepo()
                .put("queryModelGallery", baseUrl + "/me/queryModelGallery");
        //查看模特基本详情
        getRepo()
                .put("modelData", baseUrl + "/model/modelData");
        //查看模特基本详情
        getRepo()
                .put("queryMyOverview", baseUrl + "/me/queryMyOverview");
        //模特资料(比较全)
        getRepo()
                .put("echoModelInfo", baseUrl + "/me/echoModelInfo");
        //报价
        getRepo()
                .put("echoModelPrice", baseUrl + "/me/echoModelPrice");
        //操作模特报价（增加、编辑、删除）
        getRepo()
                .put("cudModelPrice", baseUrl + "/me/cudModelPrice");
        //拍摄大类
        getRepo()
                .put("queryModelShootType", baseUrl + "/sys/queryModelShootType");
        //编辑用户身体信息
        getRepo()
                .put("editUserBody", baseUrl + "/me/editUserBody");

        //编辑用户生日
        getRepo()
                .put("editUser", baseUrl + "/me/editUser");

        //编辑用户风格
        getRepo()
                .put("addModelStyle", baseUrl + "/me/addModelStyle");

        //编辑用户简介信息
        getRepo()
                .put("editUserIntro", baseUrl + "/me/editUserIntro");

        //操作模特作品（增加、删除）
        getRepo()
                .put("cudModelGalleryPic", baseUrl + "/me/cudModelGalleryPic");

        //操作用户简历（增加、编辑、删除）
        getRepo()
                .put("cudModelResume", baseUrl + "/me/cudModelResume");

        //查询用户职业类型
        getRepo()
                .put("queryUserOccupType", baseUrl + "/sys/queryUserOccupType");

        //查询模特风格类型
        getRepo()
                .put("queryModelStyleType", baseUrl + "/sys/queryModelStyleType");

        //编辑相册
        getRepo()
                .put("editModelGallery", baseUrl + "/me/editModelGallery");

        //查询模特某作品集详情
        getRepo()
                .put("queryModelGalleryDetail", baseUrl + "/me/queryModelGalleryDetail");
        //添加或取消关注
        getRepo()
                .put("editAttention", baseUrl + "/model/editAttention");

        //获取模特相册信息
        getRepo()
                .put("modelDataGalleryInfo", baseUrl + "/model/modelDataGalleryInfo");

        //获取模特相册信息
        getRepo()
                .put("praiseModelGallery", baseUrl + "/me/praiseModelGallery");

        //获取模特相册信息
        getRepo()
                .put("cancelPraiseModelGallery", baseUrl + "/me/cancelPraiseModelGallery");

        //查询模特作品集点赞者信息
        getRepo()
                .put("queryGalleryPraiseUser", baseUrl + "/me/queryGalleryPraiseUser");

        //查询模特某作品点赞者信息
        getRepo()
                .put("queryGalleryPicPraiseUser", baseUrl + "/me/queryGalleryPicPraiseUser");
    }
}
