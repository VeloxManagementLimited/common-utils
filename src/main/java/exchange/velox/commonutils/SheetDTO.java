package exchange.velox.commonutils;

import java.util.ArrayList;
import java.util.List;

public class SheetDTO {
    private List<String> headers;
    private List<List<Object>> rows = new ArrayList<>();

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
}
