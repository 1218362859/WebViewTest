package com.example.a.domain;

import java.util.List;

/**
 * Created by Administrator on 2018/4/27.
 */

public class JsonRootBean {
    private int rows;
    private String code;
    private String info;
    private List<DataBean> data;

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "JsonRootBean{" +
                "rows=" + rows +
                ", code='" + code + '\'' +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }

    public class DataBean {
        private String expect;
        private String opencode;
        private String opentime;
        private long opentimestamp;

        @Override
        public String toString() {
            return "DataBean{" +
                    "expect='" + expect + '\'' +
                    ", opencode='" + opencode + '\'' +
                    ", opentime='" + opentime + '\'' +
                    ", opentimestamp=" + opentimestamp +
                    '}';
        }

        public String getExpect() {
            return expect;
        }

        public void setExpect(String expect) {
            this.expect = expect;
        }

        public String getOpencode() {
            return opencode;
        }

        public void setOpencode(String opencode) {
            this.opencode = opencode;
        }

        public String getOpentime() {
            return opentime;
        }

        public void setOpentime(String opentime) {
            this.opentime = opentime;
        }

        public long getOpentimestamp() {
            return opentimestamp;
        }

        public void setOpentimestamp(long opentimestamp) {
            this.opentimestamp = opentimestamp;
        }
    }


}
