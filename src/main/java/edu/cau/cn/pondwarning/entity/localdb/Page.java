package edu.cau.cn.pondwarning.entity.localdb;

/**
 * 封装分页相关的信息.
 */
public class Page {

    // 当前页码，网页传参
    private int current = 1;
    // 每页显示条数上限，网页传参
    private int limit = 10;
    // 数据总数(用于计算总页数)，服务器计算
    private int rows;
    //总页面大小
    private int total;
    //起始页码
    private int from;
    //结束页码
    private int to;
    // 查询路径(用于复用分页链接)，服务器传
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     *
     * @return
     */
    public int getOffset() {
        // current * limit - limit
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     *
     * @return
     */
    public int getTotal() {
        // rows / limit [+1]
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }
    public void setTotal(int total){
        this.total=total;
    }

    /**
     * 获取起始页码
     *
     * @return
     */
    public int getFrom() {
        int from = current - 2;
        return from < 1 ? 1 : from;
    }
    public void setFrom(int from){
        this.from=from;
    }

    /**
     * 获取结束页码
     *
     * @return
     */
    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;
    }

    public void setTo(int to){
        this.to=to;
    }

}
