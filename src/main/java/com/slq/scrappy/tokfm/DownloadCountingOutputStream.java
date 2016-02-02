package com.slq.scrappy.tokfm;

import org.apache.commons.io.output.CountingOutputStream;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;

public class DownloadCountingOutputStream extends CountingOutputStream {

    private final String streamName;
    private ActionListener listener = null;

    public DownloadCountingOutputStream(OutputStream out, String streamName) {
        super(out);
        this.streamName = streamName;
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    @Override
    protected void afterWrite(int n) throws IOException {
        super.afterWrite(n);
        if (listener != null) {
            listener.actionPerformed(new ActionEvent(this, 0, null));
        }
    }

    public String getStreamName() {
        return streamName;
    }

    public double getDownloadedMB() {
        return getByteCount() / 1024.0 / 1024.0;
    }

    public void printDownloadStatus() {
        double byteCount = getDownloadedMB();
        String streamName = getStreamName();
        try {
            System.out.write(String.format("\r%s : %.2f", streamName, byteCount).getBytes());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}