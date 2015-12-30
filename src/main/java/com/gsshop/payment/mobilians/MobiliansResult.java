package com.gsshop.payment.mobilians;

import com.gsshop.amqp.RPCSerializable;

/**
 * Created by johnkim on 12/30/15.
 */
public class MobiliansResult extends RPCSerializable {

    private String mode;
    private String errorMessage;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "MobiliansResult{" +
                "mode='" + mode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
