package com.vv.personal.mkt.eq.responses;

import com.vv.personal.twm.artifactory.generated.equitiesMarket.EquitiesMarketProto;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class IntraPnL {
    private String s;
    private List<Data> data;
    private EquitiesMarketProto.Holding holding;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("s", s)
                .append("data", data)
                .toString();
    }

    public class Data {
        private Long time;
        private Double value;

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                    .append("time", time)
                    .append("value", value)
                    .toString();
        }
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public EquitiesMarketProto.Holding getHolding() {
        return holding;
    }

    public void setHolding(EquitiesMarketProto.Holding holding) {
        this.holding = holding;
    }
}
