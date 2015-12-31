package io.stalk.sample;

import io.stalk.amqp.rpc.RPCSerializable;

/**
 * Created by johnkim on 12/30/15.
 */
public class SampleResult extends RPCSerializable {

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
        return "SampleResult{" +
                "mode='" + mode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
