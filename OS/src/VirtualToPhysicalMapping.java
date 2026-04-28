// This is the virtual to physical mapping data structure class, to store physical page
// and disk page numbers.
public class VirtualToPhysicalMapping {

    // To store physical page number.
    public int physicalPageNumber;

    // To store disk page number.
    public int diskPageNumber;

    /**
     * This is to create a physical to virtual mapping object which also initializes
     * its fields: physical page number and disk page number to -1.
     *
     */
    public VirtualToPhysicalMapping() {
        physicalPageNumber = -1;
        diskPageNumber = -1;
    }
}
