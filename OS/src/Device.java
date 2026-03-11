// This is the Device interface with methods Open, Close, Read, Seek, and Write to be implemented.
public interface Device {

    /**
     * This function Opens a device/file and returns the corresponding device id.
     *
     * @param s The String.
     * @return The id, index.
     */
    public int Open(String s);

    /**
     * This function Closes the device based on the given id.
     *
     * @param id The id, index.
     */
    public void Close(int id);

    /**
     * This function Reads data from a device and returns data in bytes, uses the corresponding device id and size of data to be read.
     *
     * @param id The id, index.
     * @param size The size of data to be read.
     * @return The data.
     */
    public byte[] Read(int id, int size);

    /**
     * This function Seeks or Reads data from a device based on the id and up to a certain point/index.
     *
     * @param id The id, index.
     * @param to up to a certain point.
     */
    public void Seek(int id, int to);

    /**
     * This function Writes data to the device. Uses the specific device id and writes the given data.
     *
     * @param id The id, index.
     * @param data The data to be written.
     * @return The index.
     */
    public int Write(int id, byte[] data);
}
