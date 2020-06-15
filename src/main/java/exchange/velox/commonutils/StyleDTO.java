package exchange.velox.commonutils;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.Serializable;

public class StyleDTO implements Serializable {
    private FontDTO font;
    private short backgroundColor;
    private FillPatternType pattern;
    private HorizontalAlignment alignment;

    public FontDTO getFont() {
        return font;
    }

    public void setFont(FontDTO font) {
        this.font = font;
    }

    public short getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(short backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public FillPatternType getPattern() {
        return pattern;
    }

    public void setPattern(FillPatternType pattern) {
        this.pattern = pattern;
    }

    public HorizontalAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(HorizontalAlignment alignment) {
        this.alignment = alignment;
    }
}
