package edu.cau.cn.pondwarning.entity.localdb;

import java.math.BigDecimal;
import java.util.Date;

public class Pond {

    private String ponduuid;
    private String useruuid;
    private String imguuid;
    private String pondname;
    private BigDecimal pondacreage;
    private String pondaddress;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String description;
    private BigDecimal pondlength;
    private BigDecimal pondwidth;
    private BigDecimal ponddepth;
    private Date createTime;

    public String getPonduuid() {
        return ponduuid;
    }

    public void setPonduuid(String ponduuid) {
        this.ponduuid = ponduuid;
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

    public String getPondname() {
        return pondname;
    }

    public void setPondname(String pondname) {
        this.pondname = pondname;
    }

    public BigDecimal getPondacreage() {
        return pondacreage;
    }

    public void setPondacreage(BigDecimal pondacreage) {
        this.pondacreage = pondacreage;
    }

    public String getPondaddress() {
        return pondaddress;
    }

    public void setPondaddress(String pondaddress) {
        this.pondaddress = pondaddress;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPondlength() {
        return pondlength;
    }

    public void setPondlength(BigDecimal pondlength) {
        this.pondlength = pondlength;
    }

    public BigDecimal getPondwidth() {
        return pondwidth;
    }

    public void setPondwidth(BigDecimal pondwidth) {
        this.pondwidth = pondwidth;
    }

    public BigDecimal getPonddepth() {
        return ponddepth;
    }

    public void setPonddepth(BigDecimal ponddepth) {
        this.ponddepth = ponddepth;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Pond{" +
                "ponduuid='" + ponduuid + '\'' +
                ", useruuid='" + useruuid + '\'' +
                ", imguuid='" + imguuid + '\'' +
                ", pondname='" + pondname + '\'' +
                ", pondacreage=" + pondacreage +
                ", pondaddress='" + pondaddress + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", description='" + description + '\'' +
                ", pondlength=" + pondlength +
                ", pondwidth=" + pondwidth +
                ", ponddepth=" + ponddepth +
                ", createTime=" + createTime +
                '}';
    }
}
