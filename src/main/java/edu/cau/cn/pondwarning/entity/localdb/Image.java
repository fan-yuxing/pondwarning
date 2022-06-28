package edu.cau.cn.pondwarning.entity.localdb;

import java.util.Date;

public class Image {
    private String imguuid;
    private String imgname;
    private String imgpath;
    private String note;
    private String tablename;
    private Date createTime;

    public String getImguuid() {
        return imguuid;
    }

    public void setImguuid(String imguuid) {
        this.imguuid = imguuid;
    }

    public String getImgname() {
        return imgname;
    }

    public void setImgname(String imgname) {
        this.imgname = imgname;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imguuid='" + imguuid + '\'' +
                ", imgname='" + imgname + '\'' +
                ", imgpath='" + imgpath + '\'' +
                ", note='" + note + '\'' +
                ", tablename='" + tablename + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
