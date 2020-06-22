package exchange.velox.commonutils;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.Serializable;

public class StyleCellDTO implements Serializable {
    private FontCellDTO font;
    private short backgroundColor = 0;
    private FillPatternType pattern;
    private HorizontalAlignment alignment;
    private BorderStyle borderTop;
    private BorderStyle borderBottom;
    private BorderStyle borderLeft;
    private BorderStyle borderRight;
    private short borderTopColor = 0;
    private short borderBottomColor = 0;
    private short borderLeftColor = 0;
    private short borderRightColor = 0;
    private boolean haveFilter = false;


    public short getBorderTopColor() {
        return borderTopColor;
    }

    public void setBorderTopColor(short borderTopColor) {
        this.borderTopColor = borderTopColor;
    }

    public short getBorderBottomColor() {
        return borderBottomColor;
    }

    public void setBorderBottomColor(short borderBottomColor) {
        this.borderBottomColor = borderBottomColor;
    }

    public short getBorderLeftColor() {
        return borderLeftColor;
    }

    public void setBorderLeftColor(short borderLeftColor) {
        this.borderLeftColor = borderLeftColor;
    }

    public short getBorderRightColor() {
        return borderRightColor;
    }

    public void setBorderRightColor(short borderRightColor) {
        this.borderRightColor = borderRightColor;
    }

    public BorderStyle getBorderTop() {
        return borderTop;
    }

    public void setBorderTop(BorderStyle borderTop) {
        this.borderTop = borderTop;
    }

    public BorderStyle getBorderBottom() {
        return borderBottom;
    }

    public void setBorderBottom(BorderStyle borderBottom) {
        this.borderBottom = borderBottom;
    }

    public BorderStyle getBorderLeft() {
        return borderLeft;
    }

    public void setBorderLeft(BorderStyle borderLeft) {
        this.borderLeft = borderLeft;
    }

    public BorderStyle getBorderRight() {
        return borderRight;
    }

    public void setBorderRight(BorderStyle borderRight) {
        this.borderRight = borderRight;
    }

    public FontCellDTO getFont() {
        return font;
    }

    public void setFont(FontCellDTO font) {
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

    public boolean isHaveFilter() {
        return haveFilter;
    }

    public void setHaveFilter(boolean haveFilter) {
        this.haveFilter = haveFilter;
    }
}
