package com.ogya.lokakarya.service.telepon;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ogya.lokakarya.configuration.telepon.LaporanPelunasanConfiguration;
import com.ogya.lokakarya.entity.telepon.HistoryTelkom;
import com.ogya.lokakarya.entity.telepon.MasterPelanggan;
import com.ogya.lokakarya.repository.telepon.HistoryRepository;
import com.ogya.lokakarya.repository.telepon.MasterPelangganRepository;
import com.ogya.lokakarya.repository.telepon.criteria.HistoryTelkomCriteriaRepository;
import com.ogya.lokakarya.util.PaginationList;
import com.ogya.lokakarya.util.PagingRequestWrapper;
import com.ogya.lokakarya.wrapper.telepon.HistoryWrapper;

@Service
@Transactional
public class HistoryService {
	@Autowired
	HistoryRepository historyRepository;
	@Autowired
	MasterPelangganRepository masterPelangganRepository;
	@Autowired
	HistoryTelkomCriteriaRepository historyTelkomCriteriaRepository;
	@Autowired
	private LaporanPelunasanConfiguration laporanPelunasanConfiguration;

	public Long sumAll() {
		Long sumAll = historyRepository.sumAll();
		return sumAll;
	}

	// service untuk menampilkan semua list
	public List<HistoryWrapper> findAll() {
		List<HistoryTelkom> historyTelkomList = historyRepository.findAll(Sort.by(Order.by("idHistory")).descending());
		return toWrapperList(historyTelkomList);
	}

	// service untuk memasukkan/mengubah entity
	public HistoryWrapper save(HistoryWrapper wrapper) {
		HistoryTelkom historyTelkom = historyRepository.save(toEntity(wrapper));
		return toWrapper(historyTelkom);
	}

	// service untuk menghapus entity
	public void deleteById(Long historyId) {
		historyRepository.deleteById(historyId);
	}

	// method dalam service untuk mengubah entity ke wrapper
	private HistoryWrapper toWrapper(HistoryTelkom entity) {
		HistoryWrapper wrapper = new HistoryWrapper();
		wrapper.setIdHistory(entity.getIdHistory());
		wrapper.setTanggalBayar(entity.getTanggalBayar());
		wrapper.setBulanTagihan(entity.getBulanTagihan());
		wrapper.setTahunTagihan(entity.getTahunTagihan());
		wrapper.setUang(entity.getUang());
		wrapper.setIdPelanggan(entity.getIdPelanggan() != null ? entity.getIdPelanggan().getIdPelanggan() : null);
		Optional<MasterPelanggan> optionalMaster = masterPelangganRepository.findById(wrapper.getIdPelanggan());
		MasterPelanggan masterPelanggan = optionalMaster.isPresent() ? optionalMaster.get() : null;
		wrapper.setNama(masterPelanggan.getNama());
		return wrapper;
	}

	// method dalam service untuk memasukkan nilai kedalam entity
	private HistoryTelkom toEntity(HistoryWrapper wrapper) {
		HistoryTelkom entity = new HistoryTelkom();
		if (wrapper.getIdHistory() != null) {
			entity = historyRepository.getReferenceById(wrapper.getIdHistory());
		}
		Optional<MasterPelanggan> optionalMaster = masterPelangganRepository.findById(wrapper.getIdPelanggan());
		MasterPelanggan masterPelanggan = optionalMaster.isPresent() ? optionalMaster.get() : null;
		entity.setIdPelanggan(masterPelanggan);
		entity.setBulanTagihan(wrapper.getBulanTagihan());
		entity.setIdHistory(wrapper.getIdHistory());
		entity.setTahunTagihan(wrapper.getTahunTagihan());
		entity.setTanggalBayar(wrapper.getTanggalBayar());
		entity.setUang(wrapper.getUang());
		return entity;
	}

	// method dalam service untuk menampilkan semua list
	private List<HistoryWrapper> toWrapperList(List<HistoryTelkom> entityList) {
		List<HistoryWrapper> wrapperList = new ArrayList<HistoryWrapper>();
		for (HistoryTelkom entity : entityList) {
			HistoryWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}

	public PaginationList<HistoryWrapper, HistoryTelkom> findAllWithPagination(int page, int size) {
		Pageable paging = PageRequest.of(page, size, Sort.by("tanggalBayar").descending());
		Page<HistoryTelkom> historyPage = historyRepository.findAll(paging);
		List<HistoryTelkom> historyList = historyPage.getContent();
		List<HistoryWrapper> historyWrapperList = toWrapperList(historyList);
		return new PaginationList<HistoryWrapper, HistoryTelkom>(historyWrapperList, historyPage);
	}
	
	public List<HistoryWrapper> testData() {
		List<HistoryTelkom> dataHistory = historyRepository.findAll();
		List<HistoryWrapper> historyList = new ArrayList<HistoryWrapper>();

		for (int i = 0; i < dataHistory.size(); i++) {
			HistoryWrapper wrapper = new HistoryWrapper();
			wrapper.setIdPelanggan(dataHistory.get(i).getIdPelanggan().getIdPelanggan());
			
			MasterPelanggan masterPelanggan = masterPelangganRepository
					.findByIdPelanggan(wrapper.getIdPelanggan());
			
			wrapper.setNama(masterPelanggan.getNama());
			wrapper.setBulanTagihan(dataHistory.get(i).getBulanTagihan());
			wrapper.setTanggalBayar(dataHistory.get(i).getTanggalBayar());
			wrapper.setTahunTagihan(dataHistory.get(i).getTahunTagihan());
			wrapper.setUang(dataHistory.get(i).getUang());
//			wrapper.setIdHistory(historyList.get(i).getIdHistory());
			historyList.add(wrapper);
		}
		return historyList;
	}

//	Export To PDF
	public void ExportToPdf(HttpServletResponse response) throws Exception {
		// Call the findAll method to retrieve the data
		List<HistoryTelkom> dataHistory = historyRepository.findAll();
		List<HistoryWrapper> historyList = new ArrayList<HistoryWrapper>();

		for (int i = 0; i < dataHistory.size(); i++) {
			HistoryWrapper wrapper = new HistoryWrapper();
			wrapper.setIdPelanggan(dataHistory.get(i).getIdPelanggan().getIdPelanggan());
			MasterPelanggan masterPelanggan = masterPelangganRepository
					.findByIdPelanggan(wrapper.getIdPelanggan());
			
			wrapper.setNama(masterPelanggan.getNama());
			wrapper.setBulanTagihan(dataHistory.get(i).getBulanTagihan());
			wrapper.setTanggalBayar(dataHistory.get(i).getTanggalBayar());
			wrapper.setTahunTagihan(dataHistory.get(i).getTahunTagihan());
			wrapper.setUang(dataHistory.get(i).getUang());
			historyList.add(wrapper);
		}

		// Now create a new iText PDF document
		Document pdfDoc = new Document(PageSize.A4.rotate());
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
		pdfDoc.open();

		Paragraph title = new Paragraph("Laporan Pelunasan", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
		title.setAlignment(Element.ALIGN_CENTER);
		pdfDoc.add(title);

		// Add the generation date
		pdfDoc.add(new Paragraph(
				"Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

		// Create a table
		PdfPTable pdfTable = new PdfPTable(5);

		pdfTable.setWidthPercentage(100);
		pdfTable.setSpacingBefore(10f);
		pdfTable.setSpacingAfter(10f);

		pdfTable.addCell("Nama Pelanggan");
		pdfTable.addCell("Tanggal bayar");
		pdfTable.addCell("Bulan Tagihan");
		pdfTable.addCell("Tahun Tagihan");
		pdfTable.addCell("Nominal");
		
		BaseColor color = new BaseColor(135, 206, 235);

		for (int i = 0; i < 5; i++) {
			pdfTable.getRow(0).getCells()[i].setBackgroundColor(color);
		}

		// Iterate through the data and add it to the table
		for (HistoryWrapper entity : historyList) {
			pdfTable.addCell(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-"));
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String tanggalBayar = "-";
			if (entity.getTanggalBayar() != null) {
				tanggalBayar = formatter.format(entity.getTanggalBayar());
			}
			pdfTable.addCell(tanggalBayar);
			
			pdfTable.addCell(String.valueOf(entity.getBulanTagihan() != null ? String.valueOf(entity.getBulanTagihan()) : "-"));
			pdfTable.addCell(String.valueOf(entity.getTahunTagihan() != null ? String.valueOf(entity.getTahunTagihan()) : "-"));
			pdfTable.addCell(String.valueOf(entity.getUang() != null ? String.valueOf(entity.getUang()) : "-"));
		}

		// Add the table to the pdf document
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		pdfWriter.close();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}
	
	public ByteArrayOutputStream ExportToPdfParam(List<HistoryTelkom> dataHistory , String tittle) throws Exception {
		// Call the findAll method to retrieve the data
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		List<HistoryWrapper> historyList = new ArrayList<HistoryWrapper>();

		for (int i = 0; i < dataHistory.size(); i++) {
			HistoryWrapper wrapper = new HistoryWrapper();
			wrapper.setIdPelanggan(dataHistory.get(i).getIdPelanggan().getIdPelanggan());
			MasterPelanggan masterPelanggan = masterPelangganRepository
					.findByIdPelanggan(wrapper.getIdPelanggan());
			
			wrapper.setNama(masterPelanggan.getNama());
			wrapper.setBulanTagihan(dataHistory.get(i).getBulanTagihan());
			wrapper.setTanggalBayar(dataHistory.get(i).getTanggalBayar());
			wrapper.setTahunTagihan(dataHistory.get(i).getTahunTagihan());
			wrapper.setUang(dataHistory.get(i).getUang());
			historyList.add(wrapper);
		}

		// Now create a new iText PDF document
		Document pdfDoc = new Document(PageSize.A4.rotate());
		PdfWriter.getInstance(pdfDoc, outputStream);
		pdfDoc.open();

		Paragraph title = new Paragraph("Laporan Pelunasan", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
		title.setAlignment(Element.ALIGN_CENTER);
		pdfDoc.add(title);

		// Add the generation date
		pdfDoc.add(new Paragraph(
				"Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

		// Create a table
		PdfPTable pdfTable = new PdfPTable(5);

		pdfTable.setWidthPercentage(100);
		pdfTable.setSpacingBefore(10f);
		pdfTable.setSpacingAfter(10f);
		
		List<String> column1 = laporanPelunasanConfiguration.getColumn();
		
//		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("column/columnLaporanPelunasan.properties");
//		Properties properties = new Properties();
//		properties.load(inputStream);
//		List<String> columnNames = new ArrayList<>(properties.stringPropertyNames());
//		int columnLength = columnNames.size();
		for (String columnName : column1) {
	        pdfTable.addCell(Align(columnName));
	    }
//		PdfPCell cell1 = new PdfPCell(new Phrase("Nama Pelanggan"));
//		cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//		pdfTable.addCell(cell1);
//		PdfPCell cell2 = new PdfPCell(new Phrase("Tanggal bayar"));
//		cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//		pdfTable.addCell(cell2);
//		PdfPCell cell3 = new PdfPCell(new Phrase("Bulan Tagihan"));
//		cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//		pdfTable.addCell(cell3);
//		PdfPCell cell4 = new PdfPCell(new Phrase("Tahun Tagihan"));
//		cell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//		pdfTable.addCell(cell4);
//		PdfPCell cell5 = new PdfPCell(new Phrase("Nominal"));
//		cell5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//		pdfTable.addCell(cell5);
		
		BaseColor color = new BaseColor(135, 206, 235);

		for (int i = 0; i < 5; i++) {
			pdfTable.getRow(0).getCells()[i].setBackgroundColor(color);
		}

		// Iterate through the data and add it to the table
		for (HistoryWrapper entity : historyList) {
			pdfTable.addCell(Align(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-")));
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String tanggalBayar = "-";
			if (entity.getTanggalBayar() != null) {
				tanggalBayar = formatter.format(entity.getTanggalBayar());
			}
			pdfTable.addCell(Align(tanggalBayar));
			
			pdfTable.addCell(Align(String.valueOf(entity.getBulanTagihan() != null ? String.valueOf(entity.getBulanTagihan()) : "-")));
			pdfTable.addCell(Align(String.valueOf(entity.getTahunTagihan() != null ? String.valueOf(entity.getTahunTagihan()) : "-")));
			pdfTable.addCell(Align(String.valueOf(entity.getUang() != null ? String.valueOf(entity.getUang()) : "-")));
		}

		// Add the table to the pdf document
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		return outputStream;
	}
	public PdfPCell Align(String title) {
		PdfPCell cell = new PdfPCell(new Phrase(title));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		return cell;
	}
	public PaginationList<HistoryWrapper, HistoryTelkom> ListWithPaging(PagingRequestWrapper request) { 
		List<HistoryTelkom> historyTelkomList = historyTelkomCriteriaRepository.findByFilter(request);
		
		int fromIndex = (request.getPage())* (request.getSize());
		int toIndex = Math.min(fromIndex + request.getSize(), historyTelkomList.size());
		Page<HistoryTelkom> historyTelkomPage = new PageImpl<>(historyTelkomList.subList(fromIndex, toIndex), PageRequest.of(request.getPage(), request.getSize()), historyTelkomList.size());
		List<HistoryWrapper> historyTelkomWrapperList = new ArrayList<HistoryWrapper>();
		for(HistoryTelkom entity : historyTelkomPage) {
			HistoryWrapper wrapper = toWrapper(entity);
		    historyTelkomWrapperList.add(wrapper);
		}
		return new PaginationList<HistoryWrapper, HistoryTelkom>(historyTelkomWrapperList, historyTelkomPage);	
	}
}
