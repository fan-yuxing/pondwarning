package edu.cau.cn.pondwarning.entity.qxdb;

import java.math.BigDecimal;
import java.util.Date;

public class IotQxdevHisData {
    private String id;
    private Date collectTime;
    private String devId;
    private BigDecimal dqsd;
    private BigDecimal dqwd;
    private BigDecimal dqyl;
    private BigDecimal fs;
    private BigDecimal fx;
    private BigDecimal tyfs;
    private BigDecimal yl;

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
        return "IotQxdevHisData{" +
                "id='" + id + '\'' +
                ", collectTime=" + collectTime +
                ", devId='" + devId + '\'' +
                ", dqsd='" + dqsd + '\'' +
                ", dqwd=" + dqwd +
                ", dqyl=" + dqyl +
                ", fs=" + fs +
                ", fx=" + fx +
                ", tyfs=" + tyfs +
                ", yl=" + yl +
                '}';
    }
}
