public class KernelMessage {
    private int senderPID;
    private int targetPID;
    private int indicator;
    private byte[] data;
    public KernelMessage(int senderPID, int targetPID, int indicator, byte[] data) {
        this.senderPID = senderPID;
        this.targetPID = targetPID;
        this.indicator = indicator;
        if(data != null) {
            this.data = new byte[data.length];
            for(int i = 0; i < data.length; i++) {
                this.data[i] = data[i];
            }
        }
        else {
            this.data = null;
        }
    }
    public KernelMessage(KernelMessage km) {
        this.senderPID = km.senderPID;
        this.targetPID = km.targetPID;
        this.indicator = km.indicator;
        if(km.data != null) {
            this.data = new byte[km.data.length];
            for(int i = 0; i < km.data.length; i++) {
                this.data[i] = km.data[i];
            }
        }
        else {
            this.data = null;
        }
    }
    public int getSenderPID() {
        return senderPID;
    }
    public int getTargetPID() {
        return targetPID;
    }
    public int getIndicator() {
        return indicator;
    }
    public byte[] getData() {
        if(data == null) {
            return null;
        }
        else {
            byte[] copy = new byte[data.length];
            for(int i = 0; i < data.length; i++) {
                copy[i] = data[i];
            }
            return copy;
        }
    }
    public void setSenderPID(int senderPID) {
        this.senderPID = senderPID;
    }
    public void setTargetPID(int targetPID) {
        this.targetPID = targetPID;
    }
    public void setIndicator(int indicator) {
        this.indicator = indicator;
    }
    public void setData(byte[] data) {
        if(data == null) {
            this.data = null;
        }
        else {
            this.data = new byte[data.length];
            for(int i = 0; i < data.length; i++) {
                this.data[i] = data[i];
            }
        }
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("From: ").append(senderPID).append(" ");
        builder.append("To: ").append(targetPID).append(" ");
        builder.append("What: ").append(indicator).append(" ");
        if (data != null) {
            builder.append("Size: ").append(data.length);
        }
        else {
            builder.append("Size is 0");
        }
        return builder.toString();
    }
}
