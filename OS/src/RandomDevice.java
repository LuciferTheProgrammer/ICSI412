import java.util.Random;

// Random Devices implementes Device interface. Implements Open, Close, Read, Seek, and Write.
public class RandomDevice implements Device {

    // An array of 10 Random items.
    private Random[] randomizers = new Random[10];

    /**
     * This function implements Open, creates a new Random device and puts it in an empty spot in the array when the supplied String is not null or not empty.
     * The Random class is seeded where the String is converted to an integer. Returns the corresponding index in the Random array.
     *
     * @param s String.
     * @return The index, the device entry.
     */
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

    /**
     * This function implements Close which nulls the device entry.
     *
     * @param id The id.
     */
    @Override
    public void Close(int id) {
        if(id < 0 || id >= 10) {
            return;
        }
        randomizers[id] = null;
    }

    /**
     * This function implements Read which will create/fill an array with random values.
     *
     * @param id The id, device slot.
     * @param size The size of data.
     * @return The array of random values, data.
     */
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

    /**
     * This function implements Write which only returns 0 length and does nothing.
     *
     * @param id The id, index.
     * @param data The data.
     * @return 0.
     */
    @Override
    public int Write(int id, byte[] data) {
        return 0;
    }

    /**
     * This function implements Seek which will read random bytes of data and not return them.
     *
     * @param id The id, device slot.
     * @param to pointer, where to read from.
     */
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
