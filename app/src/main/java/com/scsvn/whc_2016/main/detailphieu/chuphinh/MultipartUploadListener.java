package com.scsvn.whc_2016.main.detailphieu.chuphinh;

/**
 * Created by Trần Xuân Lộc on 1/19/2016.
 */
public class MultipartUploadListener/* extends MultipartEntity*/ {
   /* private ProgressListener listener;

    public MultipartUploadListener(ProgressListener listener) {
        this.listener = listener;
    }

    public MultipartUploadListener(HttpMultipartMode mode, ProgressListener listener) {
        super(mode);
        this.listener = listener;
    }

    public MultipartUploadListener(HttpMultipartMode mode, String boundary, Charset charset, ProgressListener listener) {
        super(mode, boundary, charset);
        this.listener = listener;
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        super.writeTo(new CountingOutputStream(outstream, listener));
    }

    class CountingOutputStream extends FilterOutputStream {
        private final ProgressListener listener;
        private long transferred;

        public CountingOutputStream(OutputStream out, ProgressListener listener) {
            super(out);
            this.listener = listener;
        }

        @Override
        public void write(byte[] buffer, int offset, int length) throws IOException {
            transferred += length;
            this.listener.transferred(transferred);
            out.write(buffer, offset, length);
        }

        @Override
        public void write(int oneByte) throws IOException {
            out.write(oneByte);
            this.transferred++;
            this.listener.transferred(this.transferred);
        }

        @Override
        public void write(byte[] buffer) throws IOException {
            out.write(buffer);
        }
    }

    public interface ProgressListener {
        void transferred(long num);
    }*/
}
