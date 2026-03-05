import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.Arrays;

public class FakeFileSystem implements Device {
    private RandomAccessFile[]  randFiles = new RandomAccessFile[10];
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
