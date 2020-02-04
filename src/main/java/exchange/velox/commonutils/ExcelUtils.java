package exchange.velox.commonutils;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.LocaleUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelUtils {
    public static byte[] generateExcelFile(LinkedHashMap<String, SheetDTO> excelData) {
        try {
            LocaleUtil.setUserTimeZone(DateTimeUtils.DEFAULT_TIMEZONE);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
            for (String sheetName : excelData.keySet()) {
                XSSFSheet sheet = xssfWorkbook.createSheet(sheetName);
                SheetDTO sheetDTO = excelData.get(sheetName);
                Row headerRow = sheet.createRow(0);
                int headerCol = 0;
                for (String header : sheetDTO.getHeaders()) {
                    Cell cellHeader = headerRow.createCell(headerCol++);
                    cellHeader.setCellValue(header);
                }

                int rowCount = 1;
                for (List<Object> row : sheetDTO.getRows()) {
                    Row dataRow = sheet.createRow(rowCount++);
                    int dataColCount = 0;
                    for (Object data : row) {
                        Cell cellData = dataRow.createCell(dataColCount++);
                        setCellData(xssfWorkbook, data, cellData);
                    }
                }
            }
            return convertWorkBookToByteArray(xssfWorkbook);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            LocaleUtil.resetUserTimeZone();
        }
    }

    public static byte[] setHyperLink(List<HyperLinkDTO> hyperLinkDTOs, byte[] file) {
        try {
            ZipSecureFile.setMinInflateRatio(0);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new ByteArrayInputStream(file));
            for (HyperLinkDTO hyperLinkDTO : hyperLinkDTOs) {

                Sheet sheet = xssfWorkbook.getSheet(hyperLinkDTO.getSheetName());

                int headerIndex = convertHeaderToIndex(sheet, hyperLinkDTO.getColumnName());

                Cell cell = sheet.getRow(hyperLinkDTO.getRowIndex()).getCell(headerIndex);
                CreationHelper creationHelper = xssfWorkbook.getCreationHelper();
                Hyperlink hyperlink = creationHelper.createHyperlink(HyperlinkType.DOCUMENT);
                StringBuilder address = new StringBuilder("'" + hyperLinkDTO.getNavigateSheetName() + "'");
                if (hyperLinkDTO.getNavigateCellOfSheet() != null) {
                    address.append("!").append(hyperLinkDTO.getNavigateCellOfSheet());
                } else {
                    address.append("!").append("A1");
                }
                hyperlink.setAddress(address.toString());

                CellStyle hyperLinkStyle = xssfWorkbook.createCellStyle();
                XSSFFont hyperLink_font = xssfWorkbook.createFont();
                hyperLink_font.setUnderline(Font.U_SINGLE);
                hyperLink_font.setColor(IndexedColors.BLUE.getIndex());
                hyperLink_font.setFontHeight(10.0d);
                hyperLinkStyle.setFont(hyperLink_font);

                if (cell != null) {
                    cell.setHyperlink(hyperlink);
                    cell.setCellStyle(hyperLinkStyle);
                } else {
                    throw new RuntimeException("Can not find cell in sheet");
                }
            }
            return convertWorkBookToByteArray(xssfWorkbook);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static int convertHeaderToIndex(Sheet sheet, String columnName) {
        Row row = sheet.getRow(0);
        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (row.getCell(i).getStringCellValue().equals(columnName)) {
                return i;
            }
        }
        throw new RuntimeException("Can not find column name " + columnName + "in sheet");
    }

    private static byte[] convertWorkBookToByteArray(XSSFWorkbook xssfWorkbook) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            xssfWorkbook.write(outputStream);
        } catch (IOException e) {
            try {
                outputStream.close();
                xssfWorkbook.close();
            } catch (IOException ioe) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return outputStream.toByteArray();
    }

    private static void setCellData(XSSFWorkbook wb, Object data, Cell cellData) {
        if (data == null) {
            cellData.setCellValue("");
        } else if (data instanceof Integer) {
            cellData.setCellValue((Integer) data);
        } else if (data instanceof Date) {
            cellData.setCellValue((Date) data);
            cellData.setCellStyle(getCellStyleDate(wb));
        } else if (data instanceof BigDecimal) {
            cellData.setCellValue(((BigDecimal)data).doubleValue());
            cellData.setCellStyle(getCellStyleMoney(wb));
        } else if (data instanceof Double) {
            cellData.setCellValue((Double) data);
            cellData.setCellStyle(getCellStyleMoney(wb));
        } else if (data instanceof String) {
            cellData.setCellValue((String) data);
        } else {
            cellData.setCellValue(data.toString());
        }
    }

    private static CellStyle getCellStyleDate(XSSFWorkbook wb) {
        return createCellStyleFromIndex(wb, 0xf);
    }

    private static CellStyle getCellStyleMoney(XSSFWorkbook wb) {
        return createCellStyleFromIndex(wb, 4);
    }

    private static CellStyle getCellStyleInteger(XSSFWorkbook wb) {
        return createCellStyleFromIndex(wb, 1);
    }

    private static CellStyle createCellStyleFromIndex(XSSFWorkbook wb, int index) {
        CellStyle cellStyle = wb.createCellStyle();
        DataFormat poiFormat = wb.createDataFormat();
        final short format = poiFormat.getFormat(BuiltinFormats.getBuiltinFormat(index));
        cellStyle.setDataFormat(format);
        return cellStyle;
    }

    public static List<Map<String, Object>> parseExcelFileToList(InputStream inputStream) throws IOException {
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(inputStream);
            int numberOfSheets = workbook.getNumberOfSheets();
            List<Map<String, Object>> data = new ArrayList<>();
            for (int i = 0; i < numberOfSheets; i++) {
                XSSFSheet sheet = workbook.getSheetAt(i);
                int rowNum = sheet.getLastRowNum();
                for (int rowIndex = 1; rowIndex <= rowNum; rowIndex++) {
                    Map<String, Object> rowMap = new HashMap<>();
                    Row row = sheet.getRow(rowIndex);
                    if (row != null) {
                        int colNum = row.getLastCellNum();
                        for (int colIndex = 0; colIndex < colNum; colIndex++) {
                            String header = sheet.getRow(0).getCell(colIndex).getStringCellValue();
                            Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            Object value = handleCellType(cell, cell.getCellTypeEnum());
                            rowMap.put(header, value);
                        }
                        data.add(rowMap);
                    }
                }
            }
            return data;
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
            } catch (Exception ignored) {
                //ignore
            }
        }
    }

    public static LinkedHashMap parseExcelFile(InputStream inputStream) throws IOException {
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(inputStream);
            int numberOfSheets = workbook.getNumberOfSheets();
            LinkedHashMap map = new LinkedHashMap();
            for (int i = 0; i < numberOfSheets; i++) {
                XSSFSheet sheet = workbook.getSheetAt(i);
                int rowNum = sheet.getLastRowNum();
                List<List<Object>> rows = new ArrayList<>();
                for (int rowIndex = 0; rowIndex <= rowNum; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row != null) {
                        List<Object> rowData = new ArrayList<>();
                        int colNum = row.getLastCellNum();
                        for (int colIndex = 0; colIndex < colNum; colIndex++) {
                            Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            Object value = handleCellType(cell, cell.getCellTypeEnum());
                            rowData.add(value);
                        }
                        rows.add(rowData);
                    }
                }
                SheetDTO sheetDTO = new SheetDTO();
                if (rows.size() != 0) {
                    List<String> headers = rows.get(0).stream().map(x -> Objects.toString(x, "")).collect(Collectors.toList());
                    sheetDTO.setHeaders(headers);
                    sheetDTO.setRows(rows.subList(1, rows.size()));
                }
                map.put(sheet.getSheetName(), sheetDTO);
            }
            return map;
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
            } catch (Exception ignored) {
                //ignore
            }
        }
    }


    private static Object handleCellType(Cell cell, CellType cellType) {
        switch (cellType) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return handleCellType(cell, cell.getCachedFormulaResultTypeEnum());
        }
        return "";
    }

    public static Date convertExcelDate(double val, TimeZone timeZone) {
        return DateUtil.getJavaDate(val, timeZone);
    }
}
