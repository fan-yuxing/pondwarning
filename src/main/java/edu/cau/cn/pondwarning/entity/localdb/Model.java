package edu.cau.cn.pondwarning.entity.localdb;

import java.util.Date;

public class Model {
    private int modelid;
    private String useruuid;
    private String mpath;
    private int modeltype;
    private int train;
    private String modelpara;
    private String description;
    private Date createTime;

    public int getModelid() {
        return modelid;
    }

    public void setModelid(int modelid) {
        this.modelid = modelid;
    }

    public String getUseruuid() {
        return useruuid;
    }

    public void setUseruuid(String useruuid) {
        this.useruuid = useruuid;
    }

    public String getMpath() {
        return mpath;
    }

    public void setMpath(String mpath) {
        this.mpath = mpath;
    }

    public int getModeltype() {
        return modeltype;
    }

    public void setModeltype(int modeltype) {
        this.modeltype = modeltype;
    }

    public int getTrain() {
        return train;
    }

    public void setTrain(int train) {
        this.train = train;
    }

    public String getModelpara() {
        return modelpara;
    }

    public void setModelpara(String modelpara) {
        this.modelpara = modelpara;
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
        return "Model{" +
                "modelid='" + modelid + '\'' +
                ", useruuid='" + useruuid + '\'' +
                ", mpath='" + mpath + '\'' +
                ", modeltype=" + modeltype +
                ", train=" + train +
                ", modelpara='" + modelpara + '\'' +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
