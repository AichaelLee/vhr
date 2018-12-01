package net.cnki.bean;

import java.io.Serializable;
import java.util.Date;

public class TblPlan implements Serializable {
    private Integer planId;

    private Integer schoolNum;

    private String schoolYear;

    private String defaultYear;

    private String opentimenode;

    private String allowset;

    private Date createtime;

    private Date begintime;

    private Date endtime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Integer getSchoolNum() {
        return schoolNum;
    }

    public void setSchoolNum(Integer schoolNum) {
        this.schoolNum = schoolNum;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear == null ? null : schoolYear.trim();
    }

    public String getDefault() {
        return defaultYear;
    }

    public void setDefault(String defaultYear) {
        this.defaultYear = defaultYear == null ? null : defaultYear.trim();
    }

    public String getOpentimenode() {
        return opentimenode;
    }

    public void setOpentimenode(String opentimenode) {
        this.opentimenode = opentimenode == null ? null : opentimenode.trim();
    }

    public String getAllowset() {
        return allowset;
    }

    public void setAllowset(String allowset) {
        this.allowset = allowset == null ? null : allowset.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getBegintime() {
        return begintime;
    }

    public void setBegintime(Date begintime) {
        this.begintime = begintime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}