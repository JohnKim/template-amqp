package com.gsshop.payment.mobilians.client;

import com.gsshop.amqp.RPCClient;
import com.gsshop.payment.mobilians.MobiliansParam;
import com.gsshop.payment.mobilians.MobiliansResult;

public class MobiliansClient {

    public static void main(String[] argv) {

        RPCClient mobiliansRPC = null;
        try {

            mobiliansRPC = new RPCClient("payment.mobilians");

            /** [START] Create Request Object **/
            MobiliansParam param = new MobiliansParam();
            param.setId("IIIID");
            param.setReorderKey("RRRReorderKey");
            param.setSvcid("SSSSSvcid");
            /** [END] Create Request Object **/


            MobiliansResult result = (MobiliansResult) mobiliansRPC.execute(param);


            /** [START] Implementing process with the response from MQ Broker **/
            System.out.println(result);
            /** [END] Implementing process with the response from MQ Broker **/

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mobiliansRPC != null) {
                try {
                    mobiliansRPC.close();
                } catch (Exception ignore) {
                }
            }
        }
    }
}
