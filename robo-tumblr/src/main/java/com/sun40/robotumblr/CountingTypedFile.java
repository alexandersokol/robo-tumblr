package com.sun40.robotumblr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import retrofit.mime.TypedFile;

/**
 * Created by Alexander Sokol
 * on 10.09.15 8:52.
 */
class CountingTypedFile extends TypedFile {

    private static final int BUFFER_SIZE = 4096;

    private FileProgressListener mProgressListener;

    public CountingTypedFile(String mimeType, File file, FileProgressListener listener) {
        super(mimeType, file);
        mProgressListener = listener;
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        FileInputStream in = new FileInputStream(file());
        try {
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
                if (mProgressListener != null)
                    mProgressListener.onFileDataTransferred(read);
            }
        } finally {
            in.close();
        }
    }


    public interface FileProgressListener {
        void onFileDataTransferred(long transferred);
    }
}
