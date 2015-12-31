package io.stalk.sample.server;

import io.stalk.amqp.RPCServer;
import io.stalk.sample.SampleParam;
import io.stalk.sample.SampleResult;

public class SampleServer extends RPCServer<SampleParam, SampleResult> {

    private static String QUEUE_NAME = "queue.sample";

    public static void main(String[] args) {
        SampleServer process = new SampleServer();
        process.execute(QUEUE_NAME);
    }

    @Override
    public SampleResult message(SampleParam input) {


        /** [START] Handling request parameters **/
        System.out.println(input);
        /** [END] Handling request parameters **/


        /** [START] Execute process **/
        // Do Something.
        /** [END] Execute process **/


        /** [START] Create Response Object **/
        SampleResult result = new SampleResult();
        result.setMode("OK");
        result.setErrorMessage("");
        /** [END] Create Response Object **/


        return result;

    }


}
