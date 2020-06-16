package exchange.velox.commonutils;

import java.util.ArrayList;
import java.util.List;

public class SheetDTO {
    private List<String> headers;
    private List<List<Object>> rows = new ArrayList<>();
    private StyleCellDTO headerStyle;

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }


    public List<List<Object>> getRows() {
        return rows;
    }

    public void setRows(List<List<Object>> rows) {
        this.rows = rows;
    }

    public StyleCellDTO getHeaderStyle() {
        return headerStyle;
    }

    public void setHeaderStyle(StyleCellDTO headerStyle) {
        this.headerStyle = headerStyle;
    }
}
