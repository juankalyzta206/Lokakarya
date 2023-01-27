package com.ogya.lokakarya.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class ParsingColumn<T> {

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

    public PdfPTable ParsePdf(List<String> columnNames, List<T> data, PdfPTable pdfTable) {
        /* Iterate through the data and add it to the table */
        for (T entity : data) {
            for (String columnName : columnNames) {
                String value = "-";
                try {
                    String columnNameNoSpace = columnName.replaceAll("\\s", "");
                    String[] methodArray = columnNameNoSpace.split(":", 5);
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
}

	
