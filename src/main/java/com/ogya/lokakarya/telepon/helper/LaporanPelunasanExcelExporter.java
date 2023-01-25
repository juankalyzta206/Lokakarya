package com.ogya.lokakarya.telepon.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ogya.lokakarya.telepon.entity.HistoryTelkom;
import com.ogya.lokakarya.util.CurrencyData;

public class LaporanPelunasanExcelExporter {
	 private XSSFWorkbook workbook;
	    private XSSFSheet sheet;
	    private List<HistoryTelkom> listUsers;
	    public LaporanPelunasanExcelExporter(List<HistoryTelkom> listUsers) {
	        this.listUsers = listUsers;
	        workbook = new XSSFWorkbook();
	    }
	    private void writeHeaderLine() {
	    	
	        sheet = workbook.createSheet("Laporan Penunggakan");
	         
	        Row row = sheet.createRow(0);
	         
	        CellStyle style = workbook.createCellStyle();
	        XSSFFont font = workbook.createFont();
	        font.setBold(true);
	        font.setFontHeight(16);
	        style.setFont(font);
	         
	        createCell(row, 0, "ID History", style);      
	        createCell(row, 1, "Nama", style);       
	        createCell(row, 2, "Bulan Tagihan", style);    
	        createCell(row, 3, "Tahun Tagihan", style);
	        createCell(row, 4, "Nominal", style);
	        createCell(row, 5, "Tanggal Bayar", style);
	         
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
	        else if (value instanceof Byte) {
	        	cell.setCellValue((Byte) value);
	        }
	        else if (value instanceof Date) {
	        	cell.setCellValue((Date) value);
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
	                 
	        for (HistoryTelkom entity : listUsers) {
	            Row row = sheet.createRow(rowCount++);
	            int columnCount = 0;
	             
	            createCell(row, columnCount++, entity.getIdHistory(), style);
	            createCell(row, columnCount++, entity.getIdPelanggan().getNama(), style);
	            createCell(row, columnCount++, entity.getBulanTagihan(), style);
	            createCell(row, columnCount++, entity.getTahunTagihan(), style);
	            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
				CurrencyData currencyNominal = new CurrencyData();
				currencyNominal.setValue(entity.getUang());
	            createCell(row, columnCount++, String.valueOf(numberFormat.format(currencyNominal.getValue())), style);
	            createCell(row, columnCount++, entity.getTanggalBayar().toString(), style);
	             
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
	    public ByteArrayOutputStream export() throws IOException {
	        writeHeaderLine();
	        writeDataLines();
	         
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        workbook.write(outputStream);
	        workbook.close();
	         
	        return outputStream;
	         
	    }
}
