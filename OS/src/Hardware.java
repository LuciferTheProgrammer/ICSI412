// Hardware class which simulates the machine's physical memory and TLB.
// In addition, it also translates virtual addresses to physical addresses for
// both Read and Write functions.
public class Hardware {

    // Simulates a TLB, for caching purposes.
    private static int[][] TLB = new int[2][2];

    // Simulates physical memory.
    private static byte[] memory = new byte[1024 * 1024];

    // Calls on to clean TLB and initialize the mappings to -1.
    static {
         TLBClean();
     }

    /**
     * This function takes in a virtual address, computes the virtual page and offset. Then
     * searches for the physical page corresponding to the virtual page, lookup done in TLB. If not there,
     * it calls GetMapping and then does the lookup again to get the physical page. Then
     * computes the physical address with the given physical page and returns
     * the data in bytes from the that physical address in memory.
     *
     * @param address The virtual address.
     * @return The data to read from memory.
     */
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

    /**
     * This function takes in a virtual address and data in bytes.  Then computes the virtual
     * page and the offset. Then searches for the physical page corresponding to the
     * virtual page, lookup done in TLB. If not there, it calls GetMapping and then does the lookup
     * again to get the physical page. It also uses the physical page to
     * compute the physical address which is used as an index to write data into memory.
     *
     * @param address The virtual address.
     * @param value The data to write to memory.
     */
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

    /**
     * This function searches/translates a virtual page to its corresponding
     * physical page and returns it.
     *
     * @param virtualPage The page to translate to a given physical page.
     * @return The physical page.
     */
    public static int searchPhysicalPage(int virtualPage) {
        for (int i = 0; i < 2; i++) {
            if (TLB[0][i] == virtualPage) {
                int physicalPage = TLB[1][i];
                return physicalPage;
            }
        }
        return -1;
    }

    /**
     * This function cleans and initializes the TLB by filling its slots with -1.
     */
    public static void TLBClean() {
        for (int i = 0; i < 2; i++) {
            TLB[0][i] = -1;
            TLB[1][i] = -1;
        }
    }

    /**
     * This function takes in a virtual page, physical page, and given index. Then using that
     * index fills the corresponding slots for a virtual page to physical page mapping for the
     * TLB.
     *
     * @param virtualPage The virtual page.
     * @param physicalPage The physical page.
     * @param i The index in the TLB to store the mapping of a virtual page to a physical page.
     */
    public static void modifyTLB(int virtualPage, int physicalPage, int i) {
        TLB[0][i] = virtualPage;
        TLB[1][i] = physicalPage;
    }
}