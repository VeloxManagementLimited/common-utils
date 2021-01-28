package exchange.velox.commonutils;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class ExcelGenerateTest {
    private LinkedHashMap<String, SheetDTO> createExcelData() {
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
        return excel;
    }

    @Test
    public void generateExcel() throws IOException {
        LinkedHashMap<String, SheetDTO> excel = createExcelData();

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

    private byte[] generateExcelFileWithHeaders() {
        LinkedHashMap<String, SheetDTO> excelData = new LinkedHashMap<>();

        String sheetName = "header";

        List<String> headers = new ArrayList<>();
        headers.add("Name");
        headers.add("Date");
        headers.add("Salary");

        SheetDTO sheetDTO = new SheetDTO();
        sheetDTO.setHeaders(headers);

        excelData.put(sheetName, sheetDTO);
        return ExcelUtils.generateExcelFile(excelData);
    }

    private byte[] generateExcelFileWithHeadersWithOldFilteringWay() {
        LinkedHashMap<String, SheetDTO> excelData = new LinkedHashMap<>();

        String sheetName = "header";

        List<String> headers = new ArrayList<>();
        headers.add("Name");
        headers.add("Date");
        headers.add("Salary");

        SheetDTO sheetDTO = new SheetDTO();
        sheetDTO.setHeaders(headers);

        excelData.put(sheetName, sheetDTO);
        return ExcelUtils.generateNewExcelFile(excelData, true);
    }

    private byte[] generateExcelFileWithEmptyHeaders() {
        LinkedHashMap<String, SheetDTO> excelData = new LinkedHashMap<>();

        String sheetName = "header";

        List<String> headers = new ArrayList<>();
        headers.add("Name");
        headers.add("Date");
        headers.add("Salary");
        headers.add("");
        headers.add("");

        SheetDTO sheetDTO = new SheetDTO();
        sheetDTO.setHeaders(headers);

        excelData.put(sheetName, sheetDTO);
        return ExcelUtils.generateExcelFile(excelData);
    }

    private byte[] generateNormalExcelFile() {
        LinkedHashMap<String, SheetDTO> excelData = new LinkedHashMap<>();

        String sheetName = "header";

        List<String> headers = new ArrayList<>();
        headers.add("Invoice Number");
        headers.add("Currency");
        headers.add("Amount");
        headers.add("Expected Payment Date\nYYYY-MM-DD");
        headers.add("Debtor ID");

        List<Object> subRows1 = new ArrayList<>();
        subRows1.add("INV123456");
        subRows1.add("USD");
        subRows1.add(2000);
        subRows1.add("2020-03-18");
        subRows1.add("D0001");
        subRows1.add("*This row is an example");

        List<Object> subRows2 = new ArrayList<>();
        subRows2.add("INV123456");
        subRows2.add("USD");
        subRows2.add(2000);
        subRows2.add("2020-03-18");
        subRows2.add("D0001");

        List<List<Object>> rows = new ArrayList<>();
        rows.add(subRows1);
        rows.add(subRows2);

        SheetDTO sheetDTO = new SheetDTO();
        sheetDTO.setHeaders(headers);
        sheetDTO.setRows(rows);

        excelData.put(sheetName, sheetDTO);
        return ExcelUtils.generateExcelFile(excelData);
    }

    private byte[] generateExcelFileWithOver64000CellHaveStyle() {
        LinkedHashMap<String, SheetDTO> excelData = new LinkedHashMap<>();

        String sheetName = "header";

        List<String> headers = new ArrayList<>();
        headers.add("Date");
        headers.add("Money");

        List<List<Object>> rows = new ArrayList<>();
        for (int i = 0; i < 65000; i++) {
            List<Object> row = new ArrayList<>();
            row.add(new Date());
            row.add(new BigDecimal(i));

            rows.add(row);
        }

        SheetDTO sheetDTO = new SheetDTO();
        sheetDTO.setHeaders(headers);
        sheetDTO.setRows(rows);

        excelData.put(sheetName, sheetDTO);
        return ExcelUtils.generateExcelFile(excelData);
    }

    @Test
    public void validateHeaderWithEmptyCellTest() {
        byte[] bytes = generateExcelFileWithEmptyHeaders();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        Set<String> headers = new HashSet<>(Arrays.asList("Name", "Date", "Salary"));
        boolean isValid = ExcelUtils.validateHeaders(inputStream, headers);
        Assert.assertTrue(isValid);
    }

    @Test
    public void validateHeaderTest() {
        byte[] bytes = generateExcelFileWithHeaders();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        Set<String> headers = new HashSet<>(Arrays.asList("Name", "Date", "Salary"));
        boolean isValid = ExcelUtils.validateHeaders(inputStream, headers);
        Assert.assertTrue(isValid);
    }

    @Test
    public void validateHeaderTestWithOldFilteringWay() {
        byte[] bytes = generateExcelFileWithHeadersWithOldFilteringWay();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        Set<String> headers = new HashSet<>(Arrays.asList("Name", "Date", "Salary"));
        boolean isValid = ExcelUtils.validateHeaders(inputStream, headers);
        Assert.assertTrue(isValid);
    }

    @Test
    public void validateFailedHeaderTest() {
        byte[] bytes = generateExcelFileWithHeaders();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        Set<String> headers = new HashSet<>(Arrays.asList("Name", "Date", "Salary", "Failed Cell"));
        boolean isValid = ExcelUtils.validateHeaders(inputStream, headers);
        Assert.assertFalse(isValid);
    }

    @Test
    public void validateGenerateExcelFileWithOver64000CellHaveStyle() throws IOException {
        byte[] bytes = generateExcelFileWithOver64000CellHaveStyle();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        List<Map<String, Object>> rows = ExcelUtils.parseExcelFileToList(inputStream);
        Assert.assertNotNull(rows);
        Assert.assertEquals(65000, rows.size());
        for (Map<String, Object> map : rows) {
            Assert.assertNotNull(map.get("Date"));
            Assert.assertNotNull(map.get("Money"));
        }
    }

    @Test
    public void parseExcelFileToListTest() throws IOException {
        byte[] bytes = generateNormalExcelFile();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        List<Map<String, Object>> rows = ExcelUtils.parseExcelFileToList(inputStream);
        Assert.assertNotNull(rows);
        Assert.assertEquals(2, rows.size());
        rows.forEach(map -> Assert.assertNotNull(map));
    }

    @Test
    public void parseRealExcelFileToListTest() throws IOException {
        InputStream inputStream = ExcelGenerateTest.class.getClassLoader().getResourceAsStream("Passthrough_Template.xlsx");
        List<Map<String, Object>> rows = ExcelUtils.parseExcelFileToList(inputStream);
        Assert.assertNotNull(rows);
        Assert.assertEquals(20, rows.size());
        for (Map<String, Object> map : rows) {
            Assert.assertNotNull(map.get("Invoice Number"));
            Assert.assertNotNull(map.get("Currency"));
            Assert.assertNotNull(map.get("Amount"));
            Assert.assertNotNull(map.get("Expected Payment Date\nYYYY-MM-DD"));
            Assert.assertNotNull(map.get("Debtor ID"));

        }
    }

}
