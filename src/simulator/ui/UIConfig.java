package simulator.ui;

import java.util.HashSet;
import javax.swing.table.TableModel;
import simulator.core.MemoryIO;

/**
 *
 * @author LeeThree
 */
public class UIConfig {

    public short registers[];
    public short PC;
    public short SP;
    public short IH;
    public short RA;
    public short T;
    public TableModel instTable;
    public boolean compiled = false;
    public boolean running = false;
    public boolean stepping = false;
    public MemoryIO memory;
    public HashSet<Integer> breakpoints = new HashSet<Integer>();
}
