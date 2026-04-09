public class Hardware {
    private static int[][] TLB = new int[2][2];
    private static byte[] memory = new byte[1024 * 1024];
    static {
         TLBClean();
     }

    public static byte Read(int address) {
        int virtualPage = address / 1024;
        int offset = address % 1024;
        int physicalPage = searchPhysicalPage(virtualPage);
        if (physicalPage == -1) {
            OS.GetMapping(virtualPage);
            physicalPage = searchPhysicalPage(virtualPage);
        }
        int physicalAddress = physicalPage * 1024 + offset;
        return memory[physicalAddress];
    }

    public static void Write(int address, byte value) {
        int virtualPage = address / 1024;
        int offset = address % 1024;
        int physicalPage = searchPhysicalPage(virtualPage);
        if (physicalPage == -1) {
            OS.GetMapping(virtualPage);
            physicalPage = searchPhysicalPage(virtualPage);
        }
        int physicalAddress = physicalPage * 1024 + offset;
        memory[physicalAddress] = value;
    }

    public static int searchPhysicalPage(int virtualPage) {
        for (int i = 0; i < 2; i++) {
            if (TLB[0][i] == virtualPage) {
                int physicalPage = TLB[1][i];
                return physicalPage;
            }
        }
        return -1;
    }
    public static void TLBClean() {
        for (int i = 0; i < 2; i++) {
            TLB[0][i] = -1;
            TLB[1][i] = -1;
        }
    }

    public static void modifyTLB(int virtualPage, int physicalPage, int i) {
        TLB[0][i] = virtualPage;
        TLB[1][i] = physicalPage;
    }
}