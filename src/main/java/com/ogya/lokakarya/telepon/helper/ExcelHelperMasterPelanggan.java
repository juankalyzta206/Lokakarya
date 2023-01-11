package com.ogya.lokakarya.telepon.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ogya.lokakarya.telepon.entity.MasterPelanggan;

public class ExcelHelperMasterPelanggan {
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	  static String[] HEADERs = { "idPelanggan", "nama", "noTelp", "alamat","users" };
	  static String SHEET = "MasterPelanggan";

	  public static ByteArrayInputStream tutorialsToExcel(List<MasterPelanggan> masterPelanggan) {

	    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
	      Sheet sheet = workbook.createSheet(SHEET);

	      // Header
	      Row headerRow = sheet.createRow(0);

	      for (int col = 0; col < HEADERs.length; col++) {
	        Cell cell = headerRow.createCell(col);
	        cell.setCellValue(HEADERs[col]);
	      }

	      int rowIdx = 1;
	      for (MasterPelanggan entity : masterPelanggan) {
	        Row row = sheet.createRow(rowIdx++);

	        row.createCell(0).setCellValue(entity.getIdPelanggan());
	        row.createCell(1).setCellValue(entity.getNama());
	        row.createCell(2).setCellValue(entity.getAlamat());
	        row.createCell(3).setCellValue(entity.getNoTelp());
	        row.createCell(4).setCellValue(entity.getUsers().getUserId());
	      }

	      workbook.write(out);
	      return new ByteArrayInputStream(out.toByteArray());
	    } catch (IOException e) {
	      throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
	    }
	  }
}

