package io.stalk.amqp.rpc;

import java.io.Serializable;

/**
 * Created by johnkim on 12/30/15.
 */
public class RPCSerializable implements Serializable {

    private String status;
    private String error;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
