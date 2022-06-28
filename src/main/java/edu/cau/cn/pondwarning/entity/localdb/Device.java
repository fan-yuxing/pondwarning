package edu.cau.cn.pondwarning.entity.localdb;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

//水质设备
public class Device {
    private String wdevid;
    private String useruuid;
    private String imguuid;
    private String wdevname;
    private Date lastCommTime;
    private int doxSts=0;
    private int thwSts=0;
    private int ecSts=0;
    private int phSts=0;
    private int adSts=0;
    private int zdSts=0;
    private int ywSts=0;
    private int ylsSts=0;
    private int onlineSts;
    private String description;

    private String ponduuid;
    private int centertype;
    private BigDecimal localx;
    private BigDecimal localy;
    private BigDecimal localz;
    private Date createTime;

    public String getWdevid() {
        return wdevid;
    }

    public void setWdevid(String wdevid) {
        this.wdevid = wdevid;
    }

    public String getUseruuid() {
        return useruuid;
    }

    public void setUseruuid(String useruuid) {
        this.useruuid = useruuid;
    }

    public String getImguuid() {
        return imguuid;
    }

    public void setImguuid(String imguuid) {
        this.imguuid = imguuid;
    }

    public String getWdevname() {
        return wdevname;
    }

    public void setWdevname(String wdevname) {
        this.wdevname = wdevname;
    }

    public Date getLastCommTime() {
        return lastCommTime;
    }

    public void setLastCommTime(Date lastCommTime) {
        this.lastCommTime = lastCommTime;
    }

    public int getDoxSts() {
        return doxSts;
    }

    public void setDoxSts(int doxSts) {
        this.doxSts = doxSts;
    }

    public int getThwSts() {
        return thwSts;
    }

    public void setThwSts(int thwSts) {
        this.thwSts = thwSts;
    }

    public int getEcSts() {
        return ecSts;
    }

    public void setEcSts(int ecSts) {
        this.ecSts = ecSts;
    }

    public int getPhSts() {
        return phSts;
    }

    public void setPhSts(int phSts) {
        this.phSts = phSts;
    }

    public int getAdSts() {
        return adSts;
    }

    public void setAdSts(int adSts) {
        this.adSts = adSts;
    }

    public int getZdSts() {
        return zdSts;
    }

    public void setZdSts(int zdSts) {
        this.zdSts = zdSts;
    }

    public int getYwSts() {
        return ywSts;
    }

    public void setYwSts(int ywSts) {
        this.ywSts = ywSts;
    }

    public int getYlsSts() {
        return ylsSts;
    }

    public void setYlsSts(int ylsSts) {
        this.ylsSts = ylsSts;
    }

    public int getOnlineSts() {
        return onlineSts;
    }

    public void setOnlineSts(int onlineSts) {
        this.onlineSts = onlineSts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPonduuid() {
        return ponduuid;
    }

    public void setPonduuid(String ponduuid) {
        this.ponduuid = ponduuid;
    }

    public int getCentertype() {
        return centertype;
    }

    public void setCentertype(int centertype) {
        this.centertype = centertype;
    }

    public BigDecimal getLocalx() {
        return localx;
    }

    public void setLocalx(BigDecimal localx) {
        this.localx = localx;
    }

    public BigDecimal getLocaly() {
        return localy;
    }

    public void setLocaly(BigDecimal localy) {
        this.localy = localy;
    }

    public BigDecimal getLocalz() {
        return localz;
    }

    public void setLocalz(BigDecimal localz) {
        this.localz = localz;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Device{" +
                "wdevid='" + wdevid + '\'' +
                ", useruuid='" + useruuid + '\'' +
                ", imguuid='" + imguuid + '\'' +
                ", wdevname='" + wdevname + '\'' +
                ", lastCommTime=" + lastCommTime +
                ", doxSts=" + doxSts +
                ", thwSts=" + thwSts +
                ", ecSts=" + ecSts +
                ", phSts=" + phSts +
                ", adSts=" + adSts +
                ", zdSts=" + zdSts +
                ", ywSts=" + ywSts +
                ", ylsSts=" + ylsSts +
                ", onlineSts=" + onlineSts +
                ", description='" + description + '\'' +
                ", ponduuid='" + ponduuid + '\'' +
                ", centertype=" + centertype +
                ", localx=" + localx +
                ", localy=" + localy +
                ", localz=" + localz +
                '}';
    }
}
