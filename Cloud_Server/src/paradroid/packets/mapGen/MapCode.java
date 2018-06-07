package paradroid.packets.mapGen;

import java.awt.*;

/**
 * @author dxf209
 *
 */
public enum MapCode {

    WALL (new Color(0,0,0).getRGB()),
    SPACE (new Color(255,255,255).getRGB()),
    START (new Color(0,255,0).getRGB()),
    GOAL (new Color(255,0,0).getRGB()),
    EXP (new Color(16,36,68).getRGB()),
    PATH (new Color(68,16,67).getRGB()),
    OPEN (new Color(66,41,229).getRGB()),
    SPAWN (new Color(255,255,0).getRGB()),
    BOX (new Color(86,35,13).getRGB()),
	BOMB (new Color(1,1,1).getRGB());

    private final int val;
    MapCode(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

}
