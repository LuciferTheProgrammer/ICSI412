import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.Arrays;

// This is the Fake File System which implements the Device interface. Implements Open, Close, Read, Seek, and Write.
public class FakeFileSystem implements Device {

    // An array of Random Access Files of size 10.
    private RandomAccessFile[]  randFiles = new RandomAccessFile[10];

    /**
     * This function implements Open. This will create and record a new Random Access File in the array. If the filename is empty or null
     * this will throw an exception.
     *
     * @param filename The file.
     * @return The slot or index.
     */
    @Override
    public int Open(String filename) {
        if(filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be empty or null");
        }
        for (int i = 0; i < randFiles.length; i++) {
            if (randFiles[i] == null) {
                try {
                    RandomAccessFile randomFile = new RandomAccessFile(filename, "rw");
                    randFiles[i] = randomFile;
                    return i;
                }
                catch(IOException e) {
                    System.err.println("Error opening file: " + filename);
                    System.exit(-1);
                }
            }
        }
        return -1;
    }

    /**
     * This function implements Close. This closes a slot in the Random Access File array by giving it a value of null.
     *
     * @param id The index in the array of Random Access Files.
     */
    @Override
    public void Close(int id) {
        if(id < 0 || id >= randFiles.length) {
            return;
        }
        if (randFiles[id] == null) {
            return;
        }
        try {
            randFiles[id].close();
        }
        catch(IOException e) {
            System.err.println("Error closing file: " + id);
            System.exit(-1);
        }
        randFiles[id] = null;
    }

    /**
     * This function implements Read which reads data from the device based on the given device id and the size of data that needs to be read.
     * Also returns the read data in bytes.
     *
     * @param id The id, index.
     * @param size The size of data that needs to be read.
     * @return The read data in bytes.
     */
    @Override
    public byte[] Read(int id, int size) {
        if(id < 0 || id >= randFiles.length) {
            return null;
        }
        if(randFiles[id] == null) {
            return null;
        }
        if(size <= 0) {
            return new byte[0];
        }
        byte[] buffer = new byte[size];
        int reader = 0;
        try {
            reader = randFiles[id].read(buffer);
        } catch (IOException e) {
            System.err.println("Error reading file: " + id);
            System.exit(-1);
        }
        if (reader == -1) {
            return new byte[0];
        }
        else if(reader < size) {
            byte[] temp = Arrays.copyOf(buffer, reader);
            return temp;
        }
        else {
            return buffer;
        }
    }

    /**
     * This function Writes data to the device based on the given device id and data to be written. Also returns the length of the written data.
     *
     * @param id The id, index.
     * @param data The data to be written.
     * @return The length of the written data.
     */
    @Override
    public int Write(int id, byte[] data) {
        if(id < 0 || id >= randFiles.length) {
            return -1;
        }
        if(data == null) {
            return 0;
        }
        try {
            randFiles[id].write(data);
        }
        catch(IOException e) {
            System.err.println("Error writing file: " + id);
            System.exit(-1);
        }
        return data.length;
    }

    /**
     * This function Seeks or reads from a device based on the given device id and up to a certain point without returning the read data from the device.
     *
     * @param id The id, index.
     * @param to up to a certain point.
     */
    @Override
    public void Seek(int id, int to) {
        if(id < 0 || id >= randFiles.length) {
            return;
        }
        if (randFiles[id] == null) {
            return;
        }
        try {
            randFiles[id].seek(to);
        }
        catch(IOException e) {
            System.err.println("Error seeking file: " + id);
            System.exit(-1);
        }
    }
}
