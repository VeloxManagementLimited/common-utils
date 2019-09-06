package exchange.velox.commonutils;

public class HyperLinkDTO {
    private String sheetName;
    private String columnName;
    private int rowIndex;
    private String navigateSheetName;
    private String navigateCellOfSheet = "A1";

    public String getNavigateCellOfSheet() {
        return navigateCellOfSheet;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public void setNavigateCellOfSheet(String navigateCellOfSheet) {
        this.navigateCellOfSheet = navigateCellOfSheet;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getNavigateSheetName() {
        return navigateSheetName;
    }

    public void setNavigateSheetName(String navigateSheetName) {
        this.navigateSheetName = navigateSheetName;
    }
}
