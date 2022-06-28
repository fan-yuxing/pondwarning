package edu.cau.cn.pondwarning.entity.localdb;

import java.math.BigDecimal;
import java.util.Date;

public class Qxdevice {
    private String qdevid;
    private String useruuid;
    private String imguuid;
    private String qdevname;
    private Date lastCommTime;
    private BigDecimal dqsd;
    private BigDecimal dqwd;
    private BigDecimal dqyl;
    private BigDecimal fs;
    private BigDecimal fx;
    private BigDecimal tyfs;
    private BigDecimal yl;
    private String description;
    private Date createTime;

    public String getUseruuid() {
        return useruuid;
    }

    public void setUseruuid(String useruuid) {
        this.useruuid = useruuid;
    }

    public String getQdevid() {
        return qdevid;
    }

    public void setQdevid(String qdevid) {
        this.qdevid = qdevid;
    }

    public String getImguuid() {
        return imguuid;
    }

    public void setImguuid(String imguuid) {
        this.imguuid = imguuid;
    }

    public String getQdevname() {
        return qdevname;
    }

    public void setQdevname(String qdevname) {
        this.qdevname = qdevname;
    }

    public Date getLastCommTime() {
        return lastCommTime;
    }

    public void setLastCommTime(Date lastCommTime) {
        this.lastCommTime = lastCommTime;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Qxdevice{" +
                "qdevid='" + qdevid + '\'' +
                ", useruuid='" + useruuid + '\'' +
                ", imguuid='" + imguuid + '\'' +
                ", qdevname='" + qdevname + '\'' +
                ", lastCommTime=" + lastCommTime +
                ", dqsd=" + dqsd +
                ", dqwd=" + dqwd +
                ", dqyl=" + dqyl +
                ", fs=" + fs +
                ", fx=" + fx +
                ", tyfs=" + tyfs +
                ", yl=" + yl +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
