package edu.cau.cn.pondwarning.entity.localdb;

import java.util.Date;

public class Vdevice {
    private String vdevid;
    private String useruuid;
    private String imguuid;
    private String vdevname;
    private String ponduuid;
    private String url;
    private String setarea;
    private int settime;
    private String description;
    private Date createTime;

    public String getVdevid() {
        return vdevid;
    }

    public void setVdevid(String vdevid) {
        this.vdevid = vdevid;
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

    public String getVdevname() {
        return vdevname;
    }

    public void setVdevname(String vdevname) {
        this.vdevname = vdevname;
    }

    public String getPonduuid() {
        return ponduuid;
    }

    public void setPonduuid(String ponduuid) {
        this.ponduuid = ponduuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSetarea() {
        return setarea;
    }

    public void setSetarea(String setarea) {
        this.setarea = setarea;
    }

    public int getSettime() {
        return settime;
    }

    public void setSettime(int settime) {
        this.settime = settime;
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
        return "Vdevice{" +
                "vdevid='" + vdevid + '\'' +
                ", useruuid='" + useruuid + '\'' +
                ", imguuid='" + imguuid + '\'' +
                ", vdevname='" + vdevname + '\'' +
                ", ponduuid='" + ponduuid + '\'' +
                ", url='" + url + '\'' +
                ", setarea='" + setarea + '\'' +
                ", settime=" + settime +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
