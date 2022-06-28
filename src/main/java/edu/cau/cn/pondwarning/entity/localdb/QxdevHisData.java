package edu.cau.cn.pondwarning.entity.localdb;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class QxdevHisData {
    private String hisid;
    private String qdevid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone ="GMT+8")
    private Date collectTime;
    private BigDecimal dqsd;
    private BigDecimal dqwd;
    private BigDecimal dqyl;
    private BigDecimal fs;
    private BigDecimal fx;
    private BigDecimal tyfs;
    private BigDecimal yl;

    public QxdevHisData(){}

    public QxdevHisData(String hisid, String qdevid, Date collectTime, BigDecimal dqsd, BigDecimal dqwd, BigDecimal dqyl, BigDecimal fs, BigDecimal fx, BigDecimal tyfs, BigDecimal yl) {
        this.hisid = hisid;
        this.qdevid = qdevid;
        this.collectTime = collectTime;
        this.dqsd = dqsd;
        this.dqwd = dqwd;
        this.dqyl = dqyl;
        this.fs = fs;
        this.fx = fx;
        this.tyfs = tyfs;
        this.yl = yl;
    }

    public String getHisid() {
        return hisid;
    }

    public void setHisid(String hisid) {
        this.hisid = hisid;
    }

    public String getQdevid() {
        return qdevid;
    }

    public void setQdevid(String qdevid) {
        this.qdevid = qdevid;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public BigDecimal getDqsd() {
        return dqsd;
    }

    public void setDqsd(BigDecimal dqsd) {
        this.dqsd = dqsd;
    }

    public BigDecimal getDqwd() {
        return dqwd;
    }

    public void setDqwd(BigDecimal dqwd) {
        this.dqwd = dqwd;
    }

    public BigDecimal getDqyl() {
        return dqyl;
    }

    public void setDqyl(BigDecimal dqyl) {
        this.dqyl = dqyl;
    }

    public BigDecimal getFs() {
        return fs;
    }

    public void setFs(BigDecimal fs) {
        this.fs = fs;
    }

    public BigDecimal getFx() {
        return fx;
    }

    public void setFx(BigDecimal fx) {
        this.fx = fx;
    }

    public BigDecimal getTyfs() {
        return tyfs;
    }

    public void setTyfs(BigDecimal tyfs) {
        this.tyfs = tyfs;
    }

    public BigDecimal getYl() {
        return yl;
    }

    public void setYl(BigDecimal yl) {
        this.yl = yl;
    }

    @Override
    public String toString() {
        return "QxdevHisData{" +
                "hisid='" + hisid + '\'' +
                ", qdevid='" + qdevid + '\'' +
                ", collectTime=" + collectTime +
                ", dqsd=" + dqsd +
                ", dqwd=" + dqwd +
                ", dqyl=" + dqyl +
                ", fs=" + fs +
                ", fx=" + fx +
                ", tyfs=" + tyfs +
                ", yl=" + yl +
                '}';
    }
}
