package exchange.velox.commonutils;

import org.apache.logging.log4j.util.Strings;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
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
    private enum CellStyleFormat {
        DATE,
        MONEY,
    }

    private static final String DEFAULT_FONT = "Arial";

    public static byte[] generateExcelFile(LinkedHashMap<String, SheetDTO> excelData) {
        try {
            LocaleUtil.setUserTimeZone(DateTimeUtils.DEFAULT_TIMEZONE);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
            Map<CellStyleFormat, CellStyle> cellStyleMap = new HashMap<>();
            for (String sheetName : excelData.keySet()) {
                XSSFSheet sheet = xssfWorkbook.createSheet(sheetName);
                SheetDTO sheetDTO = excelData.get(sheetName);
                Row headerRow = sheet.createRow(0);

                CellStyle headerStyle = null;
                if (null != sheetDTO.getHeaderStyle()) {
                    headerStyle = xssfWorkbook.createCellStyle();
                    XSSFFont headerFont = xssfWorkbook.createFont();
                    createStyle(headerFont, headerStyle, sheetDTO.getHeaderStyle());
                }

                int headerCol = 0;
                for (String header : sheetDTO.getHeaders()) {
                    Cell cellHeader = headerRow.createCell(headerCol++);
                    cellHeader.setCellValue(header);
                    if (null != headerStyle) {
                        cellHeader.setCellStyle(headerStyle);
                    }
                    int headerLength = 0;
                    if (null != header) {
                        headerLength = header.length();
                    }
                    resizeColumnWidth(sheet, headerCol - 1, headerLength);
                }

                int rowCount = 1;
                for (List<Object> row : sheetDTO.getRows()) {
                    Row dataRow = sheet.createRow(rowCount++);
                    int dataColCount = 0;
                    for (Object data : row) {
                        Cell cellData = dataRow.createCell(dataColCount++);
                        setCellData(xssfWorkbook, data, cellData, cellStyleMap);
                    }
                }

                if (sheetDTO.getHeaderStyle() != null && sheetDTO.getHeaderStyle().isHaveFilter()) {
                    sheet.setAutoFilter(new CellRangeAddress(0, rowCount - 1, 0, headerCol - 1));
                }
            }
            return convertWorkBookToByteArray(xssfWorkbook);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            LocaleUtil.resetUserTimeZone();
        }
    }

    /**
     * please use generateExcelFile directly.
     * @param excelData
     * @param haveFilter
     * @return
     */
    @Deprecated
    public static byte[] generateNewExcelFile(LinkedHashMap<String, SheetDTO> excelData, boolean haveFilter) {
        for (String sheetName : excelData.keySet()) {
            SheetDTO sheetDTO = excelData.get(sheetName);
            if (sheetDTO.getHeaderStyle() == null) {
                sheetDTO.setHeaderStyle(createDefaultCellStyle());
            }
            if (haveFilter) {
                sheetDTO.getHeaderStyle().setHaveFilter(true);
            }
        }
        return generateExcelFile(excelData);
    }

    private static void resizeColumnWidth(XSSFSheet sheet, int column, int maxNumCharacters) {
        double defaultWidth = 10 * 256.0D;
        double maxWidth = 255 * 256.0D;
        double width = maxNumCharacters * 1.2 * 256.0D;

        width = Math.max(width, defaultWidth);
        width = Math.min(width, maxWidth);
        sheet.setColumnWidth(column, (int) width);
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

    private static void setCellData(XSSFWorkbook wb, Object data, Cell cellData, Map<CellStyleFormat, CellStyle> cellStyleMap) {
        if (data == null) {
            cellData.setCellValue("");
        } else if (data instanceof Integer) {
            cellData.setCellValue((Integer) data);
        } else if (data instanceof Date) {
            cellData.setCellValue((Date) data);
            cellData.setCellStyle(getCellStyleDate(wb, cellStyleMap));
        } else if (data instanceof BigDecimal) {
            cellData.setCellValue(((BigDecimal)data).doubleValue());
            cellData.setCellStyle(getCellStyleMoney(wb, cellStyleMap));
        } else if (data instanceof Double) {
            cellData.setCellValue((Double) data);
            cellData.setCellStyle(getCellStyleMoney(wb, cellStyleMap));
        } else if (data instanceof String) {
            cellData.setCellValue((String) data);
        } else {
            cellData.setCellValue(data.toString());
        }
    }

    private static CellStyle getCellStyleDate(XSSFWorkbook wb, Map<CellStyleFormat, CellStyle> cellStyleMap) {
        if (!cellStyleMap.containsKey(CellStyleFormat.DATE)) {
            cellStyleMap.put(CellStyleFormat.DATE, createCellStyleFromIndex(wb, 0xf));
        }
        return cellStyleMap.get(CellStyleFormat.DATE);
    }

    private static CellStyle getCellStyleMoney(XSSFWorkbook wb, Map<CellStyleFormat, CellStyle> cellStyleMap) {
        if (!cellStyleMap.containsKey(CellStyleFormat.MONEY)) {
            cellStyleMap.put(CellStyleFormat.MONEY, createCellStyleFromIndex(wb, 4));
        }
        return cellStyleMap.get(CellStyleFormat.MONEY);
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
        List<Map<String, Object>> data = new ArrayList<>();
        LinkedHashMap<String, SheetDTO> map = parseExcelFile(inputStream);

        map.forEach((key, value) -> {
            String sheetName = key;
            SheetDTO sheetDTO = value;
            List<String> headers = sheetDTO.getHeaders().stream()
                    .map(Objects::toString)
                    .filter(header -> Strings.isNotBlank(header))
                    .filter(header -> Strings.isNotEmpty(header))
                    .collect(Collectors.toList());
            List<List<Object>> rows = sheetDTO.getRows();
            rows.forEach(subRows -> {
                Map<String, Object> rowMap = new HashMap<>();
                for (int columnIndex = 0; columnIndex < subRows.size(); columnIndex++) {
                    try {
                        String header = headers.get(columnIndex);
                        String cell = subRows.get(columnIndex).toString();
                        if (!Strings.isEmpty(cell)) {
                            rowMap.put(header, cell);
                        }
                    } catch (IndexOutOfBoundsException ex) {
                        String cell = subRows.get(columnIndex).toString();
                        if (cell != null && !Strings.isEmpty(cell)) {
                            rowMap.put("EMPTY_HEADER", cell);
                        }
                    }
                }
                if (!subRows.isEmpty() && !rowMap.isEmpty()) {
                    data.add(rowMap);
                }
            });
        });

        return data;
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

    public static boolean validateHeaders(InputStream inputStream, Set<String> headers) {
        return headers.equals(getHeaders(inputStream));
    }

    private static Set<String> getHeaders(InputStream inputStream) {
        XSSFWorkbook workbook = null;
        List<List<Object>> headers = new ArrayList<>();
        try {
            workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            if (headerRow != null) {
                List<Object> rowData = new ArrayList<>();
                int colNum = headerRow.getLastCellNum();
                for (int colIndex = 0; colIndex < colNum; colIndex++) {
                    Cell cell = headerRow.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    Object value = handleCellType(cell, cell.getCellTypeEnum());
                    rowData.add(value);
                }
                headers.add(rowData);
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return headers.get(0).stream()
                .map(Object::toString)
                .filter(x -> Strings.isNotEmpty(x))
                .filter(x -> Strings.isNotBlank(x))
                .collect(Collectors.toSet());
    }

    private static void createStyle(XSSFFont font,
                                    CellStyle header,
                                    StyleCellDTO style) {
        if (style.getFont().getFontHeightInPoints() != 0) {
            font.setFontHeightInPoints(style.getFont().getFontHeightInPoints());
        }
        font.setBold(style.getFont().isBold());
        font.setItalic(style.getFont().isItalic());

        header.setFont(font);
        if (style.getBackgroundColor() != 0) {
            header.setFillForegroundColor(style.getBackgroundColor());
        }
        if (null != style.getPattern()) {
            header.setFillPattern(style.getPattern());
        }
        if (null != style.getBorderTop()) {
            header.setBorderTop(style.getBorderTop());
        }
        if (null != style.getBorderBottom()) {
            header.setBorderBottom(style.getBorderBottom());
        }
        if (null != style.getBorderLeft()) {
            header.setBorderLeft(style.getBorderLeft());
        }
        if (null != style.getBorderRight()) {
            header.setBorderRight(style.getBorderRight());
        }
        if (style.getBorderTopColor() != 0) {
            header.setTopBorderColor(style.getBorderTopColor());
        }
        if (style.getBorderBottomColor() != 0) {
            header.setBottomBorderColor(style.getBorderBottomColor());
        }
        if (style.getBorderLeftColor() != 0) {
            header.setLeftBorderColor(style.getBorderLeftColor());
        }
        if (style.getBorderRightColor() != 0) {
            header.setRightBorderColor(style.getBorderRightColor());
        }
        header.setAlignment(style.getAlignment());
    }

    public static StyleCellDTO createDefaultCellStyle() {
        StyleCellDTO style = new StyleCellDTO();
        style.setFont(createDefaultFontStyle());
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private static FontCellDTO createDefaultFontStyle() {
        FontCellDTO font = new FontCellDTO();
        font.setFontHeightInPoints((short) 11);
        font.setFontName(DEFAULT_FONT);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        font.setItalic(false);
        return font;
    }
}
