import java.util.Map;
import java.util.HashMap;

// This is the Virtual File System which implements the Device interface. Implements Open, Close, Read, Seek, and Write.
public class VirtualFileSystem implements Device {

    // Array of devices of size 10.
    private Device[] devices = new Device[10];

    // Array of ids of size 10.
    private int[] id = new int[10];

    // The Mapping of the device and its given name.
     private Map<String, Device> deviceMap = new HashMap<>();


    /**
     * This is the constructor, creates a Fake File System and Random Device then puts in the device map.
     *
     */
    public VirtualFileSystem() {
        deviceMap.put("file", new FakeFileSystem());
        deviceMap.put("random", new RandomDevice());
    }

    /**
     * This is a helper function which returns an empty slot in the device array.
     *
     * @return empty slot.
     */
    private int emptyEntry() {
        for (int i = 0; i < 10; i++) {
            if (devices[i] == null) {
                return i;
            }
        }
        return -1;
    }

    /**
     * This function implements Open which looks at the first word to determine the device, then removes that from the String and passes the remainder to
     * the Open call on the device. Also returns the index for the device and id.
     *
     * @param s The String.
     * @return The index.
     */
    @Override
    public int Open(String s) {
        if(s == null || s.isEmpty()) {
            throw new IllegalArgumentException("Empty string filename/filepath");
        }
        String[] container = s.trim().split("\\s+", 2);
        String deviceName = container[0];
        String devicePath = "";
        if (container.length == 2) {
            devicePath = container[1];
        }
        Device deviceContainer = deviceMap.get(deviceName);
        if(deviceContainer == null) {
            throw new IllegalArgumentException("Unknown device detected " + deviceName);
        }
        int idPair = deviceContainer.Open(devicePath);
        int index = emptyEntry();
        if(index == -1) {
            deviceContainer.Close(idPair);
            return -1;
        }
        devices[index] = deviceContainer;
        id[index] = idPair;
        return index;
    }

    /**
     * This function implements Close which will remove the Device and id entries.
     *
     * @param index The id, index.
     */
    @Override
    public void Close(int index) {
        if(index < 0 || index >= 10) {
            return;
        }
        if(devices[index] == null) {
            return;
        }
        Device device = devices[index];
        int holderID = id[index];
        device.Close(holderID);
        devices[index] = null;
        id[index] = -1;
    }

    /**
     * This function implements Read which reads data from a device based on the given device id and size of data to be read. Also returns the data
     * read from the device.
     *
     * @param vfsID The id, index.
     * @param size The size of data to be read.
     * @return The read data.
     */
    @Override
    public byte[] Read(int vfsID, int size) {
        if(vfsID < 0 || vfsID >= 10) {
            return null;
        }
        if(devices[vfsID] == null) {
            return null;
        }
        byte[] buffer = devices[vfsID].Read(id[vfsID], size);
        return buffer;
    }

    /**
     * This function implements Write which writes data to a device based on the given device id and the given data. Also returns a corresponding integer/id
     *
     * @param vfsID The id, index.
     * @param data The data to be written.
     * @return id, index.
     */
    @Override
    public int Write(int vfsID, byte[] data) {
        if(vfsID < 0 || vfsID >= 10) {
            return -1;
        }
        if(devices[vfsID] == null) {
            return -1;
        }
        int result = devices[vfsID].Write(id[vfsID], data);
        return result;
    }

    /**
     * This function implements Seek or reads data from a device based on the given device id and up to a certain point without returning the
     * read data.
     *
     * @param vfsID The id, index.
     * @param to up to a certain point.
     */
    @Override
    public void Seek(int vfsID, int to) {
        if (vfsID < 0 || vfsID >= 10) {
            return;
        }
        if(devices[vfsID] == null) {
            return;
        }
        devices[vfsID]. Seek(id[vfsID], to);
    }
}
