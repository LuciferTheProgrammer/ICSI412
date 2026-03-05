public interface Device {
    public int Open(String s);
    public void Close(int id);
    public byte[] Read(int id, int size);
    public void Seek(int id, int to);
    public int Write(int id, byte[] data);
}
