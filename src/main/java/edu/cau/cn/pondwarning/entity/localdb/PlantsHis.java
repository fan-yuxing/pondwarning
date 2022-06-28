package edu.cau.cn.pondwarning.entity.localdb;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class PlantsHis {
    private String hisid;
    private String vdevid;
    private String imguuid;
    private BigDecimal area;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone ="GMT+8")
    private Date collectTime;

    public String getHisid() {
        return hisid;
    }

    public void setHisid(String hisid) {
        this.hisid = hisid;
    }

    public String getVdevid() {
        return vdevid;
    }

    public void setVdevid(String vdevid) {
        this.vdevid = vdevid;
    }

    public String getImguuid() {
        return imguuid;
    }

    public void setImguuid(String imguuid) {
        this.imguuid = imguuid;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    @Override
    public String toString() {
        return "PlantsHis{" +
                "hisid='" + hisid + '\'' +
                ", vdevid='" + vdevid + '\'' +
                ", imguuid='" + imguuid + '\'' +
                ", area=" + area +
                ", collectTime='" + collectTime + '\'' +
                '}';
    }
}
