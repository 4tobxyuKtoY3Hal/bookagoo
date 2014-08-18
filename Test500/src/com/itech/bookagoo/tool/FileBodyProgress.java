package com.itech.bookagoo.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.content.AbstractContentBody;

public class FileBodyProgress extends AbstractContentBody {

    private final File file;
    
    private OnProgressListener mProgressListener = null;
    
    public FileBodyProgress(final File file, final String mimeType) {
        super(mimeType);
        if (file == null) {
            throw new IllegalArgumentException("File may not be null");
        }
        this.file = file;
    }
    
    public FileBodyProgress(final File file) {
        this(file, "application/octet-stream");
    }
    
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }
    
    public void setProgressListener(OnProgressListener l) {
    	mProgressListener = l;
    }
    
    public interface OnProgressListener {
    	public void onStart(FileBodyProgress sender) ;
    	public void onSend(FileBodyProgress sender, long loaded, long length) ;
    	public void onFinish(FileBodyProgress sender) ;
    }

    /**
     * @deprecated use {@link #writeTo(OutputStream)}
     */
    @Deprecated
    public void writeTo(final OutputStream out, int mode) throws IOException {
        writeTo(out);
    }

    @Override
    public void writeTo(final OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        InputStream in = new FileInputStream(this.file);
        try {
            byte[] tmp = new byte[4096];
            int l;
            if (mProgressListener != null)
            	mProgressListener.onStart(this);
            long loaded = 0;
            long all = this.file.length();
			int blockCount = 0;
            while ((l = in.read(tmp)) != -1) {
                out.write(tmp, 0, l);
                loaded += l;
				blockCount++;
                if (mProgressListener != null && (blockCount %= 100) == 0)
                	mProgressListener.onSend(this, loaded, all);
            }
            out.flush();
            if (mProgressListener != null)
            	mProgressListener.onSend(this, all, all);
        } finally {
            in.close();
            if (mProgressListener != null)
            	mProgressListener.onFinish(this);
        }
    }

    public String getTransferEncoding() {
        return MIME.ENC_BINARY;
    }

    public String getCharset() {
        return null;
    }

    public long getContentLength() {
        return this.file.length();
    }
    
    public String getFilename() {
        return this.file.getName();
    }
    
    public File getFile() {
        return this.file;
    }

}