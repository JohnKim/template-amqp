package com.gsshop.payment.mobilians.server;

import com.gsshop.amqp.RPCServer;
import com.gsshop.payment.mobilians.MobiliansParam;
import com.gsshop.payment.mobilians.MobiliansResult;

public class MobiliansServer extends RPCServer<MobiliansParam, MobiliansResult> {

    private static String QUEUE_NAME = "payment.mobilians";

    public static void main(String[] args) {
        MobiliansServer process = new MobiliansServer();
        process.execute(QUEUE_NAME);
    }

    @Override
    public MobiliansResult message(MobiliansParam input) {


        /** [START] Handling request parameters **/
        System.out.println(input);
        /** [END] Handling request parameters **/


        /** [START] Execute process **/
        // Do Something.
        /** [END] Execute process **/


        /** [START] Create Response Object **/
        MobiliansResult result = new MobiliansResult();
        result.setMode("OK");
        result.setErrorMessage("");
        /** [END] Create Response Object **/


        return result;

    }


}
