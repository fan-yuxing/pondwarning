package edu.cau.cn.pondwarning.entity.wqdb;

import java.math.BigDecimal;
import java.util.Date;

public class IotDevHisData {
    private String id;
    private Date collectTime;
    private String devId;
    private BigDecimal dox;
    private BigDecimal thw;
    private BigDecimal ec;
    private BigDecimal ph;
    private BigDecimal ad;
    private BigDecimal zd;
    private BigDecimal yw;
    private BigDecimal yls;

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public BigDecimal getDox() {
        return dox;
    }

    public void setDox(BigDecimal dox) {
        this.dox = dox;
    }

    public BigDecimal getThw() {
        return thw;
    }

    public void setThw(BigDecimal thw) {
        this.thw = thw;
    }

    public BigDecimal getEc() {
        return ec;
    }

    public void setEc(BigDecimal ec) {
        this.ec = ec;
    }

    public BigDecimal getPh() {
        return ph;
    }

    public void setPh(BigDecimal ph) {
        this.ph = ph;
    }

    public BigDecimal getAd() {
        return ad;
    }

    public void setAd(BigDecimal ad) {
        this.ad = ad;
    }

    public BigDecimal getZd() {
        return zd;
    }

    public void setZd(BigDecimal zd) {
        this.zd = zd;
    }

    public BigDecimal getYw() {
        return yw;
    }

    public void setYw(BigDecimal yw) {
        this.yw = yw;
    }

    public BigDecimal getYls() {
        return yls;
    }

    public void setYls(BigDecimal yls) {
        this.yls = yls;
    }


    @Override
    public String toString() {
        return "IotDevHisData{" +
                "id='" + id + '\'' +
                ", collectTime=" + collectTime +
                ", devId='" + devId + '\'' +
                ", dox=" + dox +
                ", thw=" + thw +
                ", ec=" + ec +
                ", ph=" + ph +
                ", ad=" + ad +
                ", zd=" + zd +
                ", yw=" + yw +
                ", yls=" + yls +
                '}';
    }
}
