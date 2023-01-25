package com.ogya.lokakarya.service.telepon;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.ogya.lokakarya.configuration.telepon.LaporanPenunggakanConfigurationProperties;
import com.ogya.lokakarya.entity.telepon.HistoryTelkom;
import com.ogya.lokakarya.entity.telepon.MasterPelanggan;
import com.ogya.lokakarya.entity.telepon.TransaksiTelkom;
import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.repository.telepon.HistoryRepository;
import com.ogya.lokakarya.repository.telepon.MasterPelangganRepository;
import com.ogya.lokakarya.repository.telepon.TransaksiTelkomRepository;
import com.ogya.lokakarya.repository.telepon.criteria.TransaksiTelkomCriteriaRepository;
import com.ogya.lokakarya.util.CurrencyData;
import com.ogya.lokakarya.util.PaginationList;
import com.ogya.lokakarya.util.PagingRequestWrapper;
import com.ogya.lokakarya.wrapper.telepon.HistoryWrapper;
import com.ogya.lokakarya.wrapper.telepon.TransaksiTelkomWrapper;

@Service
@Transactional
public class TransaksiTelkomService {

	@Autowired
	TransaksiTelkomRepository transaksiTelkomRepository;
	@Autowired
	MasterPelangganRepository masterPelangganRepository;
	@Autowired
	HistoryRepository historyRepository;
	@Autowired
	TransaksiTelkomCriteriaRepository transaksiTelkomCriteriaRepository;
	@Autowired
	private LaporanPenunggakanConfigurationProperties laporanPenunggakanConfigurationProperties;

	public Long sumAll() {
		Long sumAll = transaksiTelkomRepository.sumAll();
		return sumAll;
	}

	// service untuk menampilkan semua list
	public List<TransaksiTelkomWrapper> findAll() {
		List<TransaksiTelkom> transaksiTelkomList = transaksiTelkomRepository
				.findAll(Sort.by(Order.by("idTransaksi")).descending());
		return toWrapperList(transaksiTelkomList);
	}

	public List<TransaksiTelkomWrapper> findAllSortByMonthAndYear() {
		List<TransaksiTelkom> transaksiTelkomList = transaksiTelkomRepository.findAll(
				Sort.by(Order.by("tahunTagihan")).descending().and(Sort.by(Order.by("bulanTagihan")).descending()));
		return toWrapperList(transaksiTelkomList);
	}

	public List<TransaksiTelkomWrapper> findAllStatus1() {
		List<TransaksiTelkom> transaksiTelkomList = transaksiTelkomRepository
				.findStatus1(Sort.by(Order.by("idTransaksi")).descending());
		return toWrapperList(transaksiTelkomList);
	}
	public List<TransaksiTelkom> findAllStatus1NoWrapper() {
		List<TransaksiTelkom> transaksiTelkomList = transaksiTelkomRepository
				.findStatus1(Sort.by(Order.by("idTransaksi")).descending());
		return transaksiTelkomList;
	}

	// service untuk memasukkan/mengubah entity
	public TransaksiTelkomWrapper save(TransaksiTelkomWrapper wrapper) {
		TransaksiTelkom transaksiTelkom = transaksiTelkomRepository.save(toEntity(wrapper));
		// kondisional jika nilai status 2, maka service juga akan memasukkan nilai
		// kedalam tabel history
		if (wrapper.getStatus() == 2) {
			HistoryWrapper historyWrapper = new HistoryWrapper();
			Date date = new Date();
			historyWrapper.setBulanTagihan(wrapper.getBulanTagihan());
			historyWrapper.setTahunTagihan(wrapper.getTahunTagihan());
			historyWrapper.setIdPelanggan(wrapper.getIdPelanggan());
			historyWrapper.setTanggalBayar(date);
			historyWrapper.setUang(wrapper.getUang());
			historyRepository.save(toEntity(historyWrapper));
		}
		return toWrapper(transaksiTelkom);
	}

	// service untuk menghapus entity
	public void deleteById(Long transaksiId) {
		transaksiTelkomRepository.deleteById(transaksiId);
	}

	// method dalam service untuk mengubah entity ke wrapper
	private TransaksiTelkomWrapper toWrapper(TransaksiTelkom entity) {
		TransaksiTelkomWrapper wrapper = new TransaksiTelkomWrapper();
		wrapper.setBulanTagihan(entity.getBulanTagihan());
		wrapper.setTahunTagihan(entity.getTahunTagihan());
		wrapper.setUang(entity.getUang());
		wrapper.setStatus(entity.getStatus());
		wrapper.setIdTransaksi(entity.getIdTransaksi());
		wrapper.setIdPelanggan(entity.getIdPelanggan() != null ? entity.getIdPelanggan().getIdPelanggan() : null);
		Optional<MasterPelanggan> optionalMaster = masterPelangganRepository.findById(wrapper.getIdPelanggan());
		MasterPelanggan masterPelanggan = optionalMaster.isPresent() ? optionalMaster.get() : null;
		wrapper.setNama(masterPelanggan.getNama());
		return wrapper;
	}

	// method dalam service untuk memasukkan nilai kedalam entity
	private TransaksiTelkom toEntity(TransaksiTelkomWrapper wrapper) {
		TransaksiTelkom entity = new TransaksiTelkom();
		if (wrapper.getIdTransaksi() != null) {
			entity = transaksiTelkomRepository.getReferenceById(wrapper.getIdTransaksi());
			// validasi untuk bulan dan tahun tidak boleh sama
			if (entity.getIdPelanggan().getIdPelanggan().equals(wrapper.getIdPelanggan())
					&& entity.getTahunTagihan().equals(wrapper.getTahunTagihan())
					&& entity.getBulanTagihan().equals(wrapper.getBulanTagihan())) {

			} else {
				List<TransaksiTelkom> transaksiTelkomList = transaksiTelkomRepository
						.findByTagihanPelanggan(wrapper.getIdPelanggan());
				for (TransaksiTelkom entity1 : transaksiTelkomList) {
					if (entity1.getIdPelanggan().getIdPelanggan().equals(wrapper.getIdPelanggan())
							&& entity1.getTahunTagihan().equals(wrapper.getTahunTagihan())
							&& entity1.getBulanTagihan().equals(wrapper.getBulanTagihan())) {
						throw new BusinessException("bulan tagihan tidak boleh sama");
					}
				}
			}
		} else {
			List<TransaksiTelkom> transaksiTelkomList = transaksiTelkomRepository
					.findByTagihanPelanggan(wrapper.getIdPelanggan());
			for (TransaksiTelkom entity1 : transaksiTelkomList) {
				if (entity1.getIdPelanggan().getIdPelanggan().equals(wrapper.getIdPelanggan())
						&& entity1.getTahunTagihan().equals(wrapper.getTahunTagihan())
						&& entity1.getBulanTagihan().equals(wrapper.getBulanTagihan())) {
					throw new BusinessException("bulan tagihan tidak boleh sama");
				}
			}
		}
		if (wrapper.getBulanTagihan() == null) {
			throw new BusinessException("bulan tagihan tidak boleh null");
		}
		Optional<MasterPelanggan> optionalMaster = masterPelangganRepository.findById(wrapper.getIdPelanggan());
		MasterPelanggan masterPelanggan = optionalMaster.isPresent() ? optionalMaster.get() : null;
		entity.setIdPelanggan(masterPelanggan);

		entity.setBulanTagihan(wrapper.getBulanTagihan());
		entity.setTahunTagihan(wrapper.getTahunTagihan());
		entity.setUang(wrapper.getUang());
		if (wrapper.getStatus() == 1 || wrapper.getStatus() == 2) {
			entity.setStatus(wrapper.getStatus());
		} else {
			throw new BusinessException("status harus berisi 1(belumbayar) atau 2(lunas)");
		}
		return entity;
	}

	// method dalam service untuk memasukkan nilai kedalam entity
	private HistoryTelkom toEntity(HistoryWrapper wrapper) {
		HistoryTelkom entity = new HistoryTelkom();
		if (wrapper.getIdHistory() != null) {
			entity = historyRepository.getReferenceById(wrapper.getIdHistory());
		}
		if (wrapper.getBulanTagihan() == null) {
			throw new BusinessException("Bulan tagihan tidak boleh null");
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
	private List<TransaksiTelkomWrapper> toWrapperList(List<TransaksiTelkom> entityList) {
		List<TransaksiTelkomWrapper> wrapperList = new ArrayList<TransaksiTelkomWrapper>();
		for (TransaksiTelkom entity : entityList) {
			TransaksiTelkomWrapper wrapper = toWrapper(entity);
			wrapperList.add(wrapper);
		}
		return wrapperList;
	}

	public PaginationList<TransaksiTelkomWrapper, TransaksiTelkom> findAllWithPagination(int page, int size) {
		Pageable paging = PageRequest.of(page, size);
		Page<TransaksiTelkom> transaksiTelkomPage = transaksiTelkomRepository.findAllWithStatus1(paging);
		List<TransaksiTelkom> transaksiTelkomList = transaksiTelkomPage.getContent();
		List<TransaksiTelkomWrapper> transaksiTelkomWrapperList = toWrapperList(transaksiTelkomList);
		return new PaginationList<TransaksiTelkomWrapper, TransaksiTelkom>(transaksiTelkomWrapperList,
				transaksiTelkomPage);
	}

//	Export To PDF
	public void ExportToPdf(HttpServletResponse response) throws Exception {
		// Call the findAll method to retrieve the data
		List<TransaksiTelkom> dataTransaksi = transaksiTelkomRepository.findStatus1();
		List<TransaksiTelkomWrapper> wrapperList = new ArrayList<TransaksiTelkomWrapper>();

		for (int i = 0; i < dataTransaksi.size(); i++) {
			TransaksiTelkomWrapper wrapper = new TransaksiTelkomWrapper();
			wrapper.setIdPelanggan(dataTransaksi.get(i).getIdPelanggan().getIdPelanggan());
			MasterPelanggan masterPelanggan = masterPelangganRepository.findByIdPelanggan(wrapper.getIdPelanggan());

			wrapper.setNama(masterPelanggan.getNama());
			wrapper.setBulanTagihan(dataTransaksi.get(i).getBulanTagihan());
			wrapper.setTahunTagihan(dataTransaksi.get(i).getTahunTagihan());
			wrapper.setUang(dataTransaksi.get(i).getUang());
			wrapper.setStatus(dataTransaksi.get(i).getStatus());
			wrapper.setIdTransaksi(dataTransaksi.get(i).getIdTransaksi());
			wrapperList.add(wrapper);
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
		PdfPTable pdfTable = new PdfPTable(6);
		pdfTable.setWidthPercentage(100);
		pdfTable.setSpacingBefore(10f);
		pdfTable.setSpacingAfter(10f);

		PdfPCell cell1 = new PdfPCell(new Phrase("ID Transaksi"));
		cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		pdfTable.addCell(cell1);
		PdfPCell cell2 = new PdfPCell(new Phrase("Nama Pelanggan"));
		cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		pdfTable.addCell(cell2);
		PdfPCell cell3 = new PdfPCell(new Phrase("Bulan Tagihan"));
		cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		pdfTable.addCell(cell3);
		PdfPCell cell4 = new PdfPCell(new Phrase("Tahun Tagihan"));
		cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		pdfTable.addCell(cell4);
		PdfPCell cell5 = new PdfPCell(new Phrase("Nominal"));
		cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		pdfTable.addCell(cell5);
		PdfPCell cell6 = new PdfPCell(new Phrase("Status"));
		cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		pdfTable.addCell(cell6);

		BaseColor color = new BaseColor(135, 206, 235);

		for (int i = 0; i < 6; i++) {
			pdfTable.getRow(0).getCells()[i].setBackgroundColor(color);
		}

		// Iterate through the data and add it to the table
		for (TransaksiTelkomWrapper entity : wrapperList) {
			pdfTable.addCell(
					String.valueOf(entity.getIdTransaksi() != null ? String.valueOf(entity.getIdTransaksi()) : "-"));
			pdfTable.addCell(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-"));
			pdfTable.addCell(
					String.valueOf(entity.getBulanTagihan() != null ? String.valueOf(entity.getBulanTagihan()) : "-"));
			pdfTable.addCell(
					String.valueOf(entity.getTahunTagihan() != null ? String.valueOf(entity.getTahunTagihan()) : "-"));
			pdfTable.addCell(String.valueOf(entity.getUang() != null ? String.valueOf(entity.getUang()) : "-"));
			pdfTable.addCell(String.valueOf(entity.getStatus() != null ? "Belum Lunas" : "-"));
		}

		// Add the table to the pdf document
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		pdfWriter.close();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}
	public ByteArrayOutputStream ExportToExcelParam (List<TransaksiTelkom> listUsers) throws Exception{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Laporan Penunggakan");
		Row row = sheet.createRow(0);
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
//        List<String> column2 = laporanPenunggakanConfigurationProperties.getColumn();
//        int i = 0;
//        for (String columnName : column2) {
//        	i++;
//        	createCell(row, i, columnName, style,sheet);
//	    }
        createCell(row, 0, "ID Transaksi", style,sheet);      
        createCell(row, 1, "Nama", style,sheet);       
        createCell(row, 2, "Bulan Tagihan", style,sheet);    
        createCell(row, 3, "Tahun Tagihan", style,sheet);
        createCell(row, 4, "Nominal", style,sheet);
        createCell(row, 5, "Status", style,sheet);
        int rowCount = 1;
        CellStyle style1 = workbook.createCellStyle();
        XSSFFont font1 = workbook.createFont();
        font1.setFontHeight(14);
        style1.setFont(font1);
                 
        for (TransaksiTelkom entity : listUsers) {
            Row row1 = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row1, columnCount++, entity.getIdTransaksi(), style,sheet);
            createCell(row1, columnCount++, entity.getIdPelanggan().getNama(), style,sheet);
            createCell(row1, columnCount++, entity.getBulanTagihan(), style,sheet);
            createCell(row1, columnCount++, entity.getTahunTagihan(), style,sheet);
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
			CurrencyData currencyNominal = new CurrencyData();
			currencyNominal.setValue(entity.getUang());
            createCell(row1, columnCount++, String.valueOf(numberFormat.format(currencyNominal.getValue())), style,sheet);
            createCell(row1, columnCount++, "Belum lunas", style,sheet);
        }
        workbook.write(outputStream);
        workbook.close();
		return outputStream;
	}
	private void createCell(Row row, int columnCount, Object value, CellStyle style,XSSFSheet sheet) {
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
        else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
	public ByteArrayOutputStream ExportToPdfParam(List<TransaksiTelkom> dataTransaksi, String tittle) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		List<TransaksiTelkomWrapper> wrapperList = new ArrayList<TransaksiTelkomWrapper>();

		for (int i = 0; i < dataTransaksi.size(); i++) {
			TransaksiTelkomWrapper wrapper = new TransaksiTelkomWrapper();
			wrapper.setIdPelanggan(dataTransaksi.get(i).getIdPelanggan().getIdPelanggan());
			MasterPelanggan masterPelanggan = masterPelangganRepository.findByIdPelanggan(wrapper.getIdPelanggan());

			wrapper.setNama(masterPelanggan.getNama());
			wrapper.setBulanTagihan(dataTransaksi.get(i).getBulanTagihan());
			wrapper.setTahunTagihan(dataTransaksi.get(i).getTahunTagihan());
			wrapper.setUang(dataTransaksi.get(i).getUang());
			wrapper.setStatus(dataTransaksi.get(i).getStatus());
			wrapper.setIdTransaksi(dataTransaksi.get(i).getIdTransaksi());
			wrapperList.add(wrapper);
		}

		// Now create a new iText PDF document
		Document pdfDoc = new Document(PageSize.A4.rotate());
		PdfWriter.getInstance(pdfDoc, outputStream);
		pdfDoc.open();
		Paragraph title = new Paragraph("Laporan Penunggakan", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
		title.setAlignment(Element.ALIGN_CENTER);
		pdfDoc.add(title);

		// Add the generation date
		pdfDoc.add(new Paragraph(
				"Report generated on: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));

		// Create a table
		PdfPTable pdfTable = new PdfPTable(6);
		pdfTable.setWidthPercentage(100);
		pdfTable.setSpacingBefore(10f);
		pdfTable.setSpacingAfter(10f);
		
		List<String> column1 = laporanPenunggakanConfigurationProperties.getColumn();
 		
//		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("column/columnLaporanPenunggakan.properties");
//		Properties properties = new Properties();
//		properties.load(inputStream);
//		List<String> columnNames = new ArrayList<>(properties.stringPropertyNames());
//		int columnLength = columnNames.size();
		for (String columnName : column1) {
	        pdfTable.addCell(Align(columnName));
	    }
//		PdfPCell cell1 = new PdfPCell(new Phrase(laporanPenunggakanConfigurationProperties.getIdTransaksi()));
//		cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//		pdfTable.addCell(cell1);
//		PdfPCell cell2 = new PdfPCell(new Phrase(laporanPenunggakanConfigurationProperties.getNama()));
//		cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//		pdfTable.addCell(cell2);
//		PdfPCell cell3 = new PdfPCell(new Phrase(laporanPenunggakanConfigurationProperties.getBulanTagihan()));
//		cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//		pdfTable.addCell(cell3);
//		PdfPCell cell4 = new PdfPCell(new Phrase(laporanPenunggakanConfigurationProperties.getTahunTagihan()));
//		cell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//		pdfTable.addCell(cell4);
//		PdfPCell cell5 = new PdfPCell(new Phrase(laporanPenunggakanConfigurationProperties.getNominal()));
//		cell5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//		pdfTable.addCell(cell5);
//		PdfPCell cell6 = new PdfPCell(new Phrase(laporanPenunggakanConfigurationProperties.getStatus()));
//		cell6.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//		pdfTable.addCell(cell6);

		BaseColor color = new BaseColor(135, 206, 235);

		for (int i = 0; i < 6; i++) {
			pdfTable.getRow(0).getCells()[i].setBackgroundColor(color);
		}

		// Iterate through the data and add it to the table
		for (TransaksiTelkomWrapper entity : wrapperList) {
			pdfTable.addCell(Align(
					String.valueOf(entity.getIdTransaksi() != null ? String.valueOf(entity.getIdTransaksi()) : "-")));
			pdfTable.addCell(Align(String.valueOf(entity.getNama() != null ? String.valueOf(entity.getNama()) : "-")));
			pdfTable.addCell(Align(
					String.valueOf(entity.getBulanTagihan() != null ? String.valueOf(entity.getBulanTagihan()) : "-")));
			pdfTable.addCell(Align(
					String.valueOf(entity.getTahunTagihan() != null ? String.valueOf(entity.getTahunTagihan()) : "-")));
			NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
			CurrencyData currencyNominal = new CurrencyData();
			currencyNominal.setValue(entity.getUang());
			pdfTable.addCell(Align(String.valueOf(numberFormat.format(currencyNominal.getValue()) != null
					? String.valueOf(numberFormat.format(currencyNominal.getValue()))
					: "-")));
			pdfTable.addCell(Align(String.valueOf(entity.getStatus() != null ? "Belum Lunas" : "-")));
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

	public PaginationList<TransaksiTelkomWrapper, TransaksiTelkom> ListWithPaging(PagingRequestWrapper request) {
		List<TransaksiTelkom> transaksiTelkomList = transaksiTelkomCriteriaRepository.findByFilter(request);

		int fromIndex = (request.getPage()) * (request.getSize());
		int toIndex = Math.min(fromIndex + request.getSize(), transaksiTelkomList.size());
		Page<TransaksiTelkom> transaksiTelkomPage = new PageImpl<>(transaksiTelkomList.subList(fromIndex, toIndex),
				PageRequest.of(request.getPage(), request.getSize()), transaksiTelkomList.size());
		List<TransaksiTelkomWrapper> transaksiTelkomWrapperList = new ArrayList<TransaksiTelkomWrapper>();
		for (TransaksiTelkom entity : transaksiTelkomPage) {
			TransaksiTelkomWrapper wrapper = toWrapper(entity);
			transaksiTelkomWrapperList.add(wrapper);
		}
		return new PaginationList<TransaksiTelkomWrapper, TransaksiTelkom>(transaksiTelkomWrapperList,
				transaksiTelkomPage);
	}
}
