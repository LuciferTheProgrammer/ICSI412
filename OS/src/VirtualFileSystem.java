import java.util.Map;
import java.util.HashMap;

public class VirtualFileSystem implements Device {
    private Device[] devices = new Device[10];
    private int[] id = new int[10];
    private Map<String, Device> deviceMap = new HashMap<>();

    public VirtualFileSystem() {
        deviceMap.put("file", new FakeFileSystem());
        deviceMap.put("random", new RandomDevice());
    }

    private int emptyEntry() {
        for (int i = 0; i < 10; i++) {
            if (devices[i] == null) {
                return i;
            }
        }
        return -1;
    }
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
            throw new IllegalArgumentException("Unknown device detected" + deviceName);
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
