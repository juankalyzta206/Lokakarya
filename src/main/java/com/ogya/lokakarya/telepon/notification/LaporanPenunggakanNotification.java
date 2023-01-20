package com.ogya.lokakarya.telepon.notification;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.thymeleaf.context.Context;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ogya.lokakarya.bankadm.service.TransaksiNasabahService;
import com.ogya.lokakarya.telepon.entity.TransaksiTelkom;
import com.ogya.lokakarya.telepon.repository.MasterPelangganRepository;
import com.ogya.lokakarya.telepon.repository.TransaksiTelkomRepository;
import com.ogya.lokakarya.util.CurrencyData;

public class LaporanPenunggakanNotification {
	@Autowired
	private TransaksiNasabahService transaksiNasabahService;
	@Autowired
	MasterPelangganRepository masterPelangganRepository;
	@Autowired
	TransaksiTelkomRepository transaksiTelkomRepository;
	
	
	
	//setiap tanggal 1 jam 7
	@Scheduled(cron = "* 58 * * * ?")
	public void historyNotificationMonthly() throws MessagingException, IOException, DocumentException {
		try {
//			Date harini = new Date();
			Calendar cal = Calendar.getInstance();
	        cal.add(Calendar.MONTH, -1);
	        
//	        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM");
//	        String startDate = date.format(cal.getTime());
//	        String endDate = date.format(harini);
//	        String start = startDate+"-01";
//	        String end = endDate+"-01";
			
			SimpleDateFormat tanggal = new SimpleDateFormat("MMMM yyyy", new Locale("in", "ID"));
			String bulan = tanggal.format(cal.getTime());

			List<TransaksiTelkom> data = transaksiTelkomRepository.laporanPenunggakanMonthly(bulan);
			Long jumlah = transaksiTelkomRepository.jumlahLaporanPenunggakanMonthly(bulan);
			
			Long total = transaksiTelkomRepository.totalLaporanPenunggakanMonthly(bulan);
			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
			CurrencyData currencyNominal = new CurrencyData();
			currencyNominal.setValue(total);
			
			ByteArrayOutputStream laporanPenunggakan = ExportToPdfSetor(data, jumlah, numberFormat.format(currencyNominal.getValue()).toString(), "Periode Bulan "+bulan);

			Context ctx = new Context();
			ctx.setVariable("bulan", bulan);
			ctx.setVariable("jumlah", jumlah.toString());
			ctx.setVariable("total", numberFormat.format(currencyNominal.getValue()).toString());

			transaksiNasabahService.sendEmail("haha1hihi2huhu3@gmail.com", "Laporan Penunggakan bulan "+bulan, 
					 ".pdf", "SetorBulanan", ctx, laporanPenunggakan);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public PdfPCell Align(String title) {
		PdfPCell cell = new PdfPCell(new Phrase(title));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		return cell;
	}
	public ByteArrayOutputStream ExportToPdfSetor(List<TransaksiTelkom> data, Long jumlah, String total, String subJudul) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		Document pdfDoc = new Document(PageSize.A4.rotate());
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, outputStream);
		pdfDoc.open();

		Paragraph title = new Paragraph("Laporan Penunggakan Telkom BANK XYZ",
				new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
		title.setAlignment(Element.ALIGN_CENTER);
		pdfDoc.add(title);
		Paragraph subTitle = new Paragraph(subJudul, new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
		subTitle.setAlignment(Element.ALIGN_CENTER);
		pdfDoc.add(subTitle);

		// Add the generation date
		pdfDoc.add(new Paragraph(" "));
		pdfDoc.add(new Paragraph(
				"Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

		// Create a table
		PdfPTable pdfTable = new PdfPTable(6);

		pdfTable.setWidthPercentage(100);
		pdfTable.setSpacingBefore(10f);
		pdfTable.setSpacingAfter(10f);

		pdfTable.addCell(Align("ID Transaksi"));
		pdfTable.addCell(Align("Nama"));
		pdfTable.addCell(Align("Bulan Tagihan"));
		pdfTable.addCell(Align("Tahun Tagihan"));
		pdfTable.addCell(Align("Uang"));
		pdfTable.addCell(Align("Status"));
		for (int i = 0; i <= 5; i++) {
			pdfTable.getRow(0).getCells()[i].setGrayFill(0.5f);
		}

		// Iterate through the data and add it to the table
		for (TransaksiTelkom entity : data) {
			pdfTable.addCell(Align(String
					.valueOf(entity.getIdTransaksi() != null ? String.valueOf(entity.getIdTransaksi()) : "-")));
			//MasterPelanggan masterPelanggan = masterPelangganRepository.findByIdPelanggan(entity.getIdPelanggan().getIdPelanggan());
			pdfTable.addCell(Align(String
					.valueOf(entity.getIdPelanggan().getNama() != null ? String.valueOf(entity.getIdPelanggan().getNama()) : "-")));
			pdfTable.addCell(Align(String.valueOf(
					entity.getBulanTagihan() != null ? String.valueOf(entity.getBulanTagihan()) : "-")));
			pdfTable.addCell(Align(String.valueOf(entity.getTahunTagihan() != null ? String.valueOf(entity.getTahunTagihan()) : "-")));
			pdfTable.addCell(Align(String.valueOf(entity.getUang() != null ? String.valueOf(entity.getUang()) : "-")));
			pdfTable.addCell(Align(String.valueOf(entity.getStatus() != null ? "Belum Lunas" : "-")));

			//SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String formattedDate = "-";
//			if (entity.getTanggal() != null) {
//				formattedDate = formatter.format(entity.getTanggal());
//			}
			pdfTable.addCell(Align(formattedDate));

			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
			CurrencyData currencyNominal = new CurrencyData();
			currencyNominal.setValue(entity.getUang());
			pdfTable.addCell(Align(String.valueOf(numberFormat.format(currencyNominal.getValue()) != null
					? String.valueOf(numberFormat.format(currencyNominal.getValue()))
					: "-")));
		}

		// Add the table to the pdf document
		pdfDoc.add(pdfTable);
		pdfDoc.add(new Paragraph(""));
		pdfDoc.add(new Paragraph("Jumlah laporan penunggakan selama "+ subJudul +" sebanyak " + jumlah+", dengan total uang menunggakan sebesar "+total+"."));

		pdfDoc.close();
		pdfWriter.close();

		return outputStream;
	}
	
}
