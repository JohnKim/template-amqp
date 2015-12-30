package com.gsshop.amqp;

import com.sun.xml.internal.ws.encoding.soap.SerializationException;

import java.io.*;

/**
 * Created by johnkim on 12/30/15.
 */
public class SerializationUtils {

    public SerializationUtils() {
        super();
    }

    public static byte[] serialize(final Serializable obj) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        serialize(obj, baos);
        return baos.toByteArray();
    }

    public static void serialize(final Serializable obj, final OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("The OutputStream must not be null");
        }
        ObjectOutputStream out = null;
        try {
            // stream closed in the finally
            out = new ObjectOutputStream(outputStream);
            out.writeObject(obj);

        } catch (final IOException ex) {
            throw new SerializationException(ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (final IOException ex) { // NOPMD
                // ignore close exception
            }
        }
    }


    public static <T> T deserialize(final InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        }
        ObjectInputStream in = null;
        try {
            // stream closed in the finally
            in = new ObjectInputStream(inputStream);
            @SuppressWarnings("unchecked") // may fail with CCE if serialised form is incorrect
            final T obj = (T) in.readObject();
            return obj;

        } catch (final ClassCastException ex) {
            throw new SerializationException(ex);
        } catch (final ClassNotFoundException ex) {
            throw new SerializationException(ex);
        } catch (final IOException ex) {
            throw new SerializationException(ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException ex) { // NOPMD
                // ignore close exception
            }
        }
    }

    public static <T> T deserialize(final byte[] objectData) {
        if (objectData == null) {
            throw new IllegalArgumentException("The byte[] must not be null");
        }
        return SerializationUtils.<T>deserialize(new ByteArrayInputStream(objectData));
    }


}
