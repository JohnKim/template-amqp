package io.stalk.sample.client;

import io.stalk.amqp.RPCClient;
import io.stalk.sample.SampleParam;
import io.stalk.sample.SampleResult;

public class SampleClient {

    public static void main(String[] argv) {

        RPCClient sampleRPC = null;
        try {

            sampleRPC = new RPCClient("queue.sample");

            /** [START] Create Request Object **/
            SampleParam param = new SampleParam();
            param.setId("IIIID");
            param.setReorderKey("RRRReorderKey");
            param.setSvcid("SSSSSvcid");
            /** [END] Create Request Object **/


            SampleResult result = (SampleResult) sampleRPC.execute(param);


            /** [START] Implementing process with the response from MQ Broker **/
            System.out.println(result);
            /** [END] Implementing process with the response from MQ Broker **/

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sampleRPC != null) {
                try {
                    sampleRPC.close();
                } catch (Exception ignore) {
                }
            }
        }
    }
}
