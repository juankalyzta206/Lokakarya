package com.ogya.lokakarya.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class ExportData<T> {

    private T t;

    public PdfPCell Align(String title) {
        PdfPCell cell = new PdfPCell(new Phrase(title));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
        return cell;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public PdfPTable exportPdf(List<String> columnNames, List<T> data, PdfPTable pdfTable) {
    	pdfTable.setWidthPercentage(100);
		pdfTable.setSpacingBefore(10f);
		pdfTable.setSpacingAfter(10f);
		
		
		for (String columnName : columnNames) {
			String[] columnAlias = columnName.split(">>>", 2);
			if (columnAlias.length > 1) {
				pdfTable.addCell(Align(columnAlias[1]));
			} else {
				pdfTable.addCell(Align(columnName));
			}
		}
		BaseColor color = new BaseColor(135, 206, 235);
		for (int i = 0; i < columnNames.size(); i++) {
			pdfTable.getRow(0).getCells()[i].setBackgroundColor(color);
		}
    	
    	/* Iterate through the data and add it to the table */
        for (T entity : data) {
            for (String columnName : columnNames) {
                String value = "-";
                try {
                    String columnNameNoSpace = columnName.replaceAll("\\s", "");
                    String[] columnNotAlias = columnNameNoSpace.split(">>>", 2);
                    String[] methodArray = columnNotAlias[0].split(":", 5);
                    String previousMethod = "";
                    Object finalResult = "";
                    for (String methodName : methodArray) {
                            if (previousMethod.equals("")) {
                                Method method = entity.getClass().getMethod("get" + methodName);
                                Object result = method.invoke(entity);
                                finalResult = result;
                                value = finalResult != null ? finalResult.toString() : "-";
                                previousMethod = methodName;
                            } else {
                                Method method = finalResult.getClass().getMethod("get" + methodName);
                                Object result = method.invoke(finalResult);
                                finalResult = result;
                                value = finalResult != null ? finalResult.toString() : "-";
                                previousMethod = methodName;
                            }

                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    /* Handle the exception if the method is not found or cannot be invoked */
                }
                pdfTable.addCell(Align(value));
            }
        }
        return pdfTable;
    }
    
    public Sheet exportExcel(List<String> columnNames, List<T> data, Sheet sheet) {
    	/* Write data to the sheet */
		int rowNum = 1;
		int columnNum = 0;
		for (T entity : data) {
			Row row = sheet.createRow(rowNum++);
			columnNum = 0;
			for (String columnName : columnNames) {
				String value = "-";
				try {
                    String columnNameNoSpace = columnName.replaceAll("\\s", "");
                    String[] methodArray = columnNameNoSpace.split(".", 5);
                    String previousMethod = "";
                    Object finalResult = "";
                    for (String methodName : methodArray) {
                
                            if (previousMethod.equals("")) {
                                Method method = entity.getClass().getMethod("get" + methodName);
                                Object result = method.invoke(entity);
                                finalResult = result;
                                value = finalResult != null ? finalResult.toString() : "-";
                                previousMethod = methodName;
                            } else {
                                Method method = finalResult.getClass().getMethod("get" + methodName);
                                Object result = method.invoke(finalResult);
                                finalResult = result;
                                value = finalResult != null ? finalResult.toString() : "-";
                                previousMethod = methodName;
                            }

                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    /* Handle the exception if the method is not found or cannot be invoked */
                }
				row.createCell(columnNum).setCellValue(value);
				columnNum++;
			}
		}
		/* Resize the columns to fit the contents */
		for (int i = 0; i < columnNames.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		return sheet;
    }
}

	
