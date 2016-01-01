package io.stalk.sample.server;

import io.stalk.amqp.rpc.RPCServer;
import io.stalk.sample.SampleParam;
import io.stalk.sample.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class SampleServer extends RPCServer<SampleParam, SampleResult> {

    private static Logger logger = LoggerFactory.getLogger(SampleServer.class);
    private static String QUEUE_NAME = "queue.sample";

    public static void main(String[] args) {
        SampleServer process = new SampleServer();
        process.execute(QUEUE_NAME);
    }

    @Override
    public SampleResult message(SampleParam input) {


        MDC.put(LOG_FILE_NAME, "sample"); // log file name.

        /** [START] Handling request parameters **/
        logger.debug(input.toString());
        /** [END] Handling request parameters **/


        /** [START] Execute process **/
        // Do Something.
        /** [END] Execute process **/


        /** [START] Create Response Object **/
        SampleResult result = new SampleResult();
        result.setMode("OK");
        result.setErrorMessage("");
        /** [END] Create Response Object **/

        MDC.remove(LOG_FILE_NAME);

        return result;

    }


}
