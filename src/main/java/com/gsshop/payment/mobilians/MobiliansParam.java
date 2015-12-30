package com.gsshop.payment.mobilians;

import com.gsshop.amqp.RPCSerializable;

/**
 * Created by johnkim on 12/30/15.
 */
public class MobiliansParam extends RPCSerializable {

    private String mode;
    private String reorderKey;
    private String svcid;
    private String id;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getReorderKey() {
        return reorderKey;
    }

    public void setReorderKey(String reorderKey) {
        this.reorderKey = reorderKey;
    }

    public String getSvcid() {
        return svcid;
    }

    public void setSvcid(String svcid) {
        this.svcid = svcid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MobiliansParam{" +
                "mode='" + mode + '\'' +
                ", reorderKey='" + reorderKey + '\'' +
                ", svcid='" + svcid + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
