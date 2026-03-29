// This is the Kernel Message, for messages that are sent and received from one process to another. Has a sending PID for the sender,
// a target PID for the target/recipient, indicator for what the message is, and the raw data in bytes.
public class KernelMessage {

    // The pid to represent the sender process.
    private int senderPID;

    // The pid to represent the target/receiver process.
    private int targetPID;

    // The 'what' status of the message.
    private int indicator;

    // The raw data of the message in bytes.
    private byte[] data;

    /**
     * This constructor takes in the sender pid, target pid, indicator, and raw data in bytes and sets each appropriate member field for the
     * Kernel Message.
     *
     * @param senderPID The pid of the sending process.
     * @param targetPID The pid of the target or receiving process.
     * @param indicator The 'what' of the message.
     * @param data The raw data in bytes.
     */
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

    /**
     * This constructor copies over a given Kernel Message by retrieving its member fields like sender pid, target pid, indicator,
     * and data and copying/setting those values over to the new Kernel Message.
     *
     * @param km The Kernel Message to copy.
     */
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

    /**
     * This function returns the sender pid.
     *
     * @return The sender pid.
     */
    public int getSenderPID() {
        return senderPID;
    }

    /**
     * This function returns the target pid.
     *
     * @return The target pid.
     */
    public int getTargetPID() {
        return targetPID;
    }

    /**
     * This function returns the indicator.
     *
     * @return The indicator.
     */
    public int getIndicator() {
        return indicator;
    }

    /**
     * This function returns the raw data in bytes.
     *
     * @return The data.
     */
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

    /**
     * This function sets the sender pid.
     *
     * @param senderPID to set.
     */
    public void setSenderPID(int senderPID) {
        this.senderPID = senderPID;
    }

    /**
     * This function sets the target pid.
     *
     * @param targetPID to set.
     */
    public void setTargetPID(int targetPID) {
        this.targetPID = targetPID;
    }

    /**
     * This function sets the indicator.
     *
     * @param indicator to set.
     */
    public void setIndicator(int indicator) {
        this.indicator = indicator;
    }

    /**
     * This function sets the data in bytes.
     *
     * @param data to set.
     */
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

    /**
     * This is the toString method which returns the overall properties of a given Kernel Message. These properties include sender pid, target pid,
     * indicator, and data.
     *
     * @return The String representation of a given Kernel Message.
     */
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
