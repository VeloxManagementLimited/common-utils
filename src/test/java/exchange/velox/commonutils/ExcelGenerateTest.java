package exchange.velox.commonutils;

import jdk.internal.util.xml.impl.Input;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

public class ExcelGenerateTest {
    @Test
    public void generateExcel() throws IOException {
        List<String> headers = new ArrayList<>();
        headers.add("Name");
        headers.add("Date");
        headers.add("Salary");

        List<List<Object>> data = new ArrayList<>();
        List<Object> row1 = new ArrayList<>();
        row1.add("Jame");
        row1.add(DateTimeUtils.switchToHKTWithoutChangingValue(new Date()));
        row1.add(new BigDecimal("100"));

        List<Object> row2 = new ArrayList<>();
        row2.add("David");
        row2.add(DateTimeUtils.switchToHKTWithoutChangingValue(new Date()));
        row2.add(BigDecimal.valueOf(109.1));

        List<Object> row3 = new ArrayList<>();
        row3.add("Tony");
        row3.add(DateTimeUtils.switchToHKTWithoutChangingValue(new Date()));
        row3.add(BigDecimal.valueOf(209.1));

        data.add(row1);
        data.add(row2);
        data.add(row3);

        SheetDTO sheetDTO = new SheetDTO();
        sheetDTO.setHeaders(headers);
        sheetDTO.setRows(data);

        LinkedHashMap<String, SheetDTO> excel = new LinkedHashMap<>();

        List<String> headers1 = new ArrayList<>();
        headers1.add("Name1");
        headers1.add("Date1");
        headers1.add("Salary1");

        List<List<Object>> data1 = new ArrayList<>();
        List<Object> row11 = new ArrayList<>();
        row11.add(null);
        row11.add(DateTimeUtils.switchToHKTWithoutChangingValue(new Date()));
        row11.add(new BigDecimal("100"));

        List<Object> row21 = new ArrayList<>();
        row21.add("David");
        row21.add(DateTimeUtils.switchToHKTWithoutChangingValue(new Date()));
        row21.add(BigDecimal.valueOf(1093.1));

        List<Object> row31 = new ArrayList<>();
        row31.add("Tony");
        row31.add(DateTimeUtils.switchToHKTWithoutChangingValue(new Date()));
        row31.add(BigDecimal.valueOf(2090.1));

        data1.add(row11);
        data1.add(row21);
        data1.add(row31);

        SheetDTO sheetDTO1 = new SheetDTO();
        sheetDTO1.setHeaders(headers1);
        sheetDTO1.setRows(data1);

        excel.put("New User", sheetDTO1);
        excel.put("User", sheetDTO);

        byte[] file = ExcelUtils.generateExcelFile(excel);

        HyperLinkDTO hyperLinkDTO = new HyperLinkDTO();
        hyperLinkDTO.setSheetName("User");
        hyperLinkDTO.setColumnName("Name");
        hyperLinkDTO.setRowIndex(2);
        hyperLinkDTO.setNavigateSheetName("New User");

        List<HyperLinkDTO> linkDTOS = new ArrayList<>();
        linkDTOS.add(hyperLinkDTO);


        ExcelUtils.setHyperLink(linkDTOS, file);
    }

    private byte[] generateExcelFile() {
        LinkedHashMap<String, SheetDTO> excelData = new LinkedHashMap<>();

        String headerName = "header";

        List<String> headers = new ArrayList<>();
        headers.add("Name");
        headers.add("Date");
        headers.add("Salary");

        List<Object> subRows = new ArrayList<>();
        subRows.add("Test1");
        subRows.add("Date1");
        subRows.add("Salary1");
        subRows.add("Test2");
        subRows.add("Date2");
        subRows.add("Salary2");

        List<List<Object>> rows = new ArrayList<>();
        rows.add(subRows);

        SheetDTO sheetDTO = new SheetDTO();
        sheetDTO.setHeaders(headers);
        sheetDTO.setRows(rows);

        excelData.put(headerName, sheetDTO);
        return ExcelUtils.generateExcelFile(excelData);
    }

    @Test
    public void validateHeader() {
        byte[] bytes = generateExcelFile();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        Set<String> headers = new HashSet<>(Arrays.asList("Name", "Date", "Salary"));
        boolean isValid = ExcelUtils.validateHeaders(inputStream, headers);
        Assert.assertTrue(isValid);
    }
}
