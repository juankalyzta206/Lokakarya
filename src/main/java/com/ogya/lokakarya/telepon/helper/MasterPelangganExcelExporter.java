package com.ogya.lokakarya.telepon.helper;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ogya.lokakarya.telepon.wrapper.MasterPelangganWrapper;

public class MasterPelangganExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<MasterPelangganWrapper> listUsers;
    public MasterPelangganExcelExporter(List<MasterPelangganWrapper> listUsers) {
        this.listUsers = listUsers;
        workbook = new XSSFWorkbook();
    }
    private void writeHeaderLine() {
        sheet = workbook.createSheet("MasterPelanggan");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "ID Pelanggan", style);      
        createCell(row, 1, "Nama", style);       
        createCell(row, 2, "No telepon", style);    
        createCell(row, 3, "Alamat", style);
        createCell(row, 4, "User ID", style);
         
    }
     
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else if(value instanceof Integer) {
        	cell.setCellValue((Integer) value);
        }
        else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        for (MasterPelangganWrapper entity : listUsers) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, entity.getIdPelanggan(), style);
            createCell(row, columnCount++, entity.getNama(), style);
            createCell(row, columnCount++, entity.getNoTelp(), style);
            createCell(row, columnCount++, entity.getAlamat(), style);
            createCell(row, columnCount++, entity.getUserId(), style);
             
        }
    }
     
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
}
