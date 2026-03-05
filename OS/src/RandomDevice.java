import java.util.Random;

public class RandomDevice implements Device {
    private Random[] randomizers = new Random[10];
    @Override
    public int Open(String s) {
        for(int i  = 0; i < randomizers.length; i++) {
            if (randomizers[i] == null) {
                if(s != null && !s.isEmpty()) {
                    int value = Integer.parseInt(s);
                    randomizers[i] = new Random(value);
                }
                else {
                    randomizers[i] = new Random();
                }
                return i;
            }
        }
        return -1;
    }
    @Override
    public void Close(int id) {
        if(id < 0 || id >= 10) {
            return;
        }
        randomizers[id] = null;
    }
    @Override
    public byte[] Read(int id, int size) {
        if(id < 0 || id >= 10) {
            return null;
        }
        if(randomizers[id] == null) {
            return null;
        }
        if (size <= 0) {
            return new byte[0];
        }
        byte[] result = new byte[size];
        randomizers[id].nextBytes(result);
        return result;
    }
    @Override
    public int Write(int id, byte[] data) {
        return 0;
    }
    @Override
    public void Seek(int id, int to) {
        if(id < 0 || id >= 10) {
            return;
        }
        if (randomizers[id] == null) {
            return;
        }
        if (to <= 0) {
            return;
        }
        byte[] delta = new byte[to];
        randomizers[id].nextBytes(delta);
    }
}
