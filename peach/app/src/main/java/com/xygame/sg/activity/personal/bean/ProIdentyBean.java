package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;

/**
 * Created by xy on 2016/1/21.
 */
public class ProIdentyBean implements Serializable {
    private String userId;
    private String brokerName;
    private String brokerPhone;
    private String agencyCompany;
    private String companyPhone;
    private String companyAddress;
    private String modelCardPath;
    private String modelCardUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public String getBrokerPhone() {
        return brokerPhone;
    }

    public void setBrokerPhone(String brokerPhone) {
        this.brokerPhone = brokerPhone;
    }

    public String getAgencyCompany() {
        return agencyCompany;
    }

    public void setAgencyCompany(String agencyCompany) {
        this.agencyCompany = agencyCompany;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getModelCardPath() {
        return modelCardPath;
    }

    public void setModelCardPath(String modelCardPath) {
        this.modelCardPath = modelCardPath;
    }

    public String getModelCardUrl() {
        return modelCardUrl;
    }

    public void setModelCardUrl(String modelCardUrl) {
        this.modelCardUrl = modelCardUrl;
    }
}
