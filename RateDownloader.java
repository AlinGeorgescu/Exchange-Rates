import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * (C) Copyright 2020
 */
class RateDownloader {
    private TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
    };

    void download() {

        // Open a secured connection.
        URL url = null;
        try {
            url = new URL("https://www.bnr.ro:443/nbrfxrates.xml");
        } catch (MalformedURLException e) {
            System.err.println("Creating URL failed!");
            e.printStackTrace();
            System.exit(-1);
        }

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Creating sslContext failed!");
            e.printStackTrace();
            System.exit(-1);
        }
        try {
            sslContext.init(null, trustAllCerts, new SecureRandom());
        } catch (KeyManagementException e) {
            System.err.println("Initializing sslContext failed!");
            e.printStackTrace();
            System.exit(-1);
        }

        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
        } catch (IOException e) {
            System.err.println("Initializing connection failed!");
            e.printStackTrace();
            System.exit(-1);
        }
        connection.setSSLSocketFactory(sslContext.getSocketFactory());

        ReadableByteChannel readableByteChannel = null;
        try {
            readableByteChannel = Channels.newChannel(connection.getInputStream());
        } catch (IOException e) {
            System.err.println("Initializing ReadableChannel failed!");
            e.printStackTrace();
            System.exit(-1);
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("tmp_rates.xml");
        } catch (FileNotFoundException e) {
            System.err.println("Opening file failed!");
            e.printStackTrace();
            System.exit(-1);
        }
        FileChannel fileChannel = fileOutputStream.getChannel();

        System.out.println("Starting transfer!");
        try {
            fileOutputStream.getChannel()
                    .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            System.err.println("Transfer failed!");
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("Transfer completed!");

        try {
            fileOutputStream.close();
        } catch (IOException e) {
            System.out.println("Failed to close file!");
            e.printStackTrace();
        }
    }
}
