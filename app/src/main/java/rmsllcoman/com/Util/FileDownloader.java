package rmsllcoman.com.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by macmini on 6/10/17.
 */

public class FileDownloader {
    private static final int  MEGABYTE = 1024 * 1024;
    static FileOutputStream fileOutputStream;

    public static String downloadFile(String fileUrl, File fileName) throws IOException {
        try {

            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            fileOutputStream = new FileOutputStream(fileName);
            int totalSize = urlConnection.getContentLength();

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while((bufferLength = inputStream.read(buffer))>0)
            fileOutputStream.write(buffer, 0, bufferLength);
        } catch (IOException e1) {
            e1.printStackTrace();
            return "fail";
        }
        fileOutputStream.close();
        return "success";
    }
}