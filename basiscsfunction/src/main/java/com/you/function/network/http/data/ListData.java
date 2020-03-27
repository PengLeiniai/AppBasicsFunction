package com.you.function.network.http.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaiwang on 2017/11/23.
 */

public class ListData<T> {

    private String total;

    public String getTotal() {
        return total;
    }

    private boolean endPage;
    private String notice;

    private int version;
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    public boolean isEndPage() {
        return endPage;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public boolean getEndPage() {
        return endPage;
    }

    public void setEndPage(boolean endPage) {
        this.endPage = endPage;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    private List<T> list;

    public List<T> getList() {
        return list == null ? list = new ArrayList<>() : list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
