package edu.cau.cn.pondwarning.entity.localdb;

public class DevList {
    private String devid;
    private String ponduuid;
    private String devname;
    private String pondname="--";//设置默认值--
    private int type;

    public String getDevid() {
        return devid;
    }

    public void setDevid(String devid) {
        this.devid = devid;
    }

    public String getPonduuid() {
        return ponduuid;
    }

    public void setPonduuid(String ponduuid) {
        this.ponduuid = ponduuid;
    }

    public String getDevname() {
        return devname;
    }

    public void setDevname(String devname) {
        this.devname = devname;
    }

    public String getPondname() {
        return pondname;
    }

    public void setPondname(String pondname) {
        this.pondname = pondname;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DevList{" +
                "devid='" + devid + '\'' +
                ", ponduuid='" + ponduuid + '\'' +
                ", devname='" + devname + '\'' +
                ", pondname='" + pondname + '\'' +
                ", type=" + type +
                '}';
    }
}
