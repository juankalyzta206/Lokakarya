package com.ogya.lokakarya.bankadm.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ogya.lokakarya.bankadm.entity.HistoryBank;
import com.ogya.lokakarya.bankadm.entity.MasterBank;
import com.ogya.lokakarya.bankadm.entity.TransaksiNasabah;
import com.ogya.lokakarya.bankadm.repository.HistoryBankRepository;
import com.ogya.lokakarya.bankadm.repository.MasterBankRepository;
import com.ogya.lokakarya.bankadm.repository.TransaksiNasabahRepository;
import com.ogya.lokakarya.bankadm.wrapper.MasterBankWrapper;
import com.ogya.lokakarya.bankadm.wrapper.SetorAmbilWrapper;
import com.ogya.lokakarya.bankadm.wrapper.TransferWrapper;
import com.ogya.lokakarya.exception.BusinessException;
import com.ogya.lokakarya.telepon.entity.HistoryTelkom;
import com.ogya.lokakarya.telepon.entity.MasterPelanggan;
import com.ogya.lokakarya.telepon.entity.TransaksiTelkom;
import com.ogya.lokakarya.telepon.repository.HistoryTelkomRepository;
import com.ogya.lokakarya.telepon.repository.MasterPelangganRepository;
import com.ogya.lokakarya.telepon.repository.TransaksiTelkomRepository;
import com.ogya.lokakarya.telepon.wrapper.BayarTeleponWrapper;

@Service
@Transactional
public class TransaksiNasabahService {
	@Autowired
	MasterBankRepository masterBankRepo;
	@Autowired
	TransaksiNasabahRepository transaksiNasabahRepo;
	@Autowired
	HistoryBankRepository historyBankRepo;
	@Autowired
	MasterPelangganRepository masterPelangganRepo;
	@Autowired
	TransaksiTelkomRepository transaksiTelkomRepo;
	@Autowired
	HistoryTelkomRepository historyTelkomRepo;

	// -------------------------------------------ceksaldo----------------------------------------
	public MasterBankWrapper cekSaldo(Long rekening) {
		if (masterBankRepo.findById(rekening).isPresent()) {
			MasterBank nasabah = masterBankRepo.getReferenceById(rekening);
			MasterBankWrapper wrapper = new MasterBankWrapper();
			wrapper.setNorek(nasabah.getNorek());
			wrapper.setNama(nasabah.getNama());
			wrapper.setSaldo(nasabah.getSaldo());
			return wrapper;
		} else {
			throw new BusinessException("Nomor rekening tidak terdaftar.");
		}
	}

	// -----------------------------------findByNoTelp-----------------------------------
	public List<BayarTeleponWrapper> findByNoTelpon(Long rekAsal, Long noTelpon) {
		List<BayarTeleponWrapper> wrapperList = new ArrayList<BayarTeleponWrapper>();

		if (masterBankRepo.findById(rekAsal).isPresent()) {
			MasterBank masterBank = masterBankRepo.getReferenceById(rekAsal);

			if (masterPelangganRepo.findByNoTelp(noTelpon) != null) {
				MasterPelanggan masterPelanggan = masterPelangganRepo.findByNoTelp(noTelpon);
				List<TransaksiTelkom> transaksiTelkom = transaksiTelkomRepo
						.findByTagihanPelanggan(masterPelanggan.getIdPelanggan());

				for (int i = 0; i < transaksiTelkom.size(); i++) {
					if (transaksiTelkom.get(i).getStatus() == 1) {
						BayarTeleponWrapper wrapper = new BayarTeleponWrapper();
						wrapper.setIdTransaksi(transaksiTelkom.get(i).getIdTransaksi());
						wrapper.setIdPelanggan(masterPelanggan.getIdPelanggan());
						wrapper.setNamaPelanggan(masterPelanggan.getNama());
						wrapper.setNoTelepon(masterPelanggan.getNoTelp());
						wrapper.setBulanTagihan(transaksiTelkom.get(i).getBulanTagihan());
						wrapper.setTahunTagihan(transaksiTelkom.get(i).getTahunTagihan());
						wrapper.setTagihan(transaksiTelkom.get(i).getUang());
						wrapper.setStatus(transaksiTelkom.get(i).getStatus());
						wrapper.setNoRekening(rekAsal);
						wrapper.setNamaRekening(masterBank.getNama());
						wrapper.setSaldo(masterBank.getSaldo());
						wrapperList.add(wrapper);
					}
				}
			} else {
				throw new BusinessException("Nomor telepon tidak terdaftar");
			}
		} else {
			throw new BusinessException("Nomor rekening tidak terdaftar");
		}
		return wrapperList;
	}

	// -----------------------------------findTotalTagihan-----------------------------------
	public List<BayarTeleponWrapper> findTotalTagihan(Long rekAsal, Long noTelpon) {
		List<BayarTeleponWrapper> wrapperList = new ArrayList<BayarTeleponWrapper>();

		if (masterBankRepo.findById(rekAsal).isPresent()) {
			MasterBank masterBank = masterBankRepo.getReferenceById(rekAsal);

			if (masterPelangganRepo.findByNoTelp(noTelpon) != null) {
				MasterPelanggan masterPelanggan = masterPelangganRepo.findByNoTelp(noTelpon);
				Long totalTagihan = transaksiTelkomRepo.tagihanTelpon(masterPelanggan.getIdPelanggan());

				BayarTeleponWrapper wrapper = new BayarTeleponWrapper();
				wrapper.setIdPelanggan(masterPelanggan.getIdPelanggan());
				wrapper.setNamaPelanggan(masterPelanggan.getNama());
				wrapper.setNoTelepon(masterPelanggan.getNoTelp());
				wrapper.setTagihan(totalTagihan);
				wrapper.setNoRekening(rekAsal);
				wrapper.setNamaRekening(masterBank.getNama());
				wrapper.setSaldo(masterBank.getSaldo());
				wrapperList.add(wrapper);

			} else {
				throw new BusinessException("Nomor telepon tidak terdaftar");
			}
		} else {
			throw new BusinessException("Nomor rekening tidak terdaftar");
		}
		return wrapperList;
	}

	// ------------------------------------setor----------------------------------------
	public SetorAmbilWrapper setor(Long rekening, Long nominal) {
		if (masterBankRepo.findById(rekening).isPresent()) {

			if (nominal >= 10000) {

				MasterBank nasabah = masterBankRepo.getReferenceById(rekening);
				TransaksiNasabah transaksi = new TransaksiNasabah();

				nasabah.setSaldo(nasabah.getSaldo() + nominal);
				masterBankRepo.save(nasabah);

				transaksi.setMasterBank(nasabah);
				transaksi.setStatus("D");
				transaksi.setUang(nominal);
				transaksi.setStatusKet((byte) 1);
				transaksiNasabahRepo.save(transaksi);

				HistoryBank historyBank = new HistoryBank();
				historyBank.setNama(nasabah.getNama());
				historyBank.setRekening(nasabah);
				historyBank.setStatusKet((byte) 1);
				historyBank.setUang(nominal);
				historyBankRepo.save(historyBank);

				SetorAmbilWrapper wrapper = new SetorAmbilWrapper();
				wrapper.setNamaNasabah(nasabah.getNama());
				wrapper.setNominal(nominal);
				wrapper.setNomorRekening(rekening);
				wrapper.setSaldo(nasabah.getSaldo());
				wrapper.setTanggal(transaksi.getTanggal());

				return wrapper;

			} else {
				throw new BusinessException("Nominal transaksi minimal Rp10.000,00.");
			}
		} else {
			throw new BusinessException("Nomor rekening tidak terdaftar");
		}
	}

	// ----------------------------------tarik ---------------------------
	public SetorAmbilWrapper tarik(Long rekening, Long nominal) {

		if (masterBankRepo.findById(rekening).isPresent()) {
			MasterBank nasabah = masterBankRepo.getReferenceById(rekening);
			TransaksiNasabah transaksi = new TransaksiNasabah();
			HistoryBank historyBank = new HistoryBank();

			if (nominal >= 10000) {

				if (nasabah.getSaldo() - nominal >= 50000) {
					nasabah.setSaldo(nasabah.getSaldo() - nominal);
					masterBankRepo.save(nasabah);

					transaksi.setMasterBank(nasabah);
					transaksi.setStatus("K");
					transaksi.setUang(nominal);
					transaksi.setStatusKet((byte) 2);
					transaksiNasabahRepo.save(transaksi);

					historyBank.setNama(nasabah.getNama());
					historyBank.setRekening(nasabah);
					historyBank.setStatusKet((byte) 2);
					historyBank.setUang(nominal);
					historyBankRepo.save(historyBank);

					SetorAmbilWrapper wrapper = new SetorAmbilWrapper();
					wrapper.setNamaNasabah(nasabah.getNama());
					wrapper.setNominal(nominal);
					wrapper.setNomorRekening(rekening);
					wrapper.setSaldo(nasabah.getSaldo());
					wrapper.setTanggal(transaksi.getTanggal());
					return wrapper;

				} else {
					throw new BusinessException("Saldo Anda tidak cukup");
				}
			} else {
				throw new BusinessException("Nominal transaksi minimal Rp10.000,00.");
			}
		} else {
			throw new BusinessException("Nomor rekening tidak terdaftar");
		}
	}

	// -------------------------------------Transfer-------------------------------------------------
	public TransferWrapper transfer(Long rekTujuan, Long rekAsal, Long nominal) {

		if (masterBankRepo.findById(rekAsal).isPresent()) {
			if (masterBankRepo.findById(rekTujuan).isPresent()) {

				MasterBank pengirim = masterBankRepo.getReferenceById(rekAsal);
				MasterBank tujuan = masterBankRepo.getReferenceById(rekTujuan);
				TransaksiNasabah transaksiNasabah = new TransaksiNasabah();

				if (pengirim != tujuan) {

					if (nominal >= 10000) {

						if (pengirim.getSaldo() - nominal >= 50000) {

							transaksiNasabah.setNorekDituju(rekTujuan);
							transaksiNasabah.setStatus("K");
							transaksiNasabah.setStatusKet((byte) 3);
							transaksiNasabah.setUang(nominal);
							transaksiNasabah.setMasterBank(pengirim);
							transaksiNasabah.setNoTlp(pengirim.getNotlp());
							transaksiNasabahRepo.save(transaksiNasabah);

							pengirim.setSaldo(pengirim.getSaldo() - nominal);
							masterBankRepo.save(pengirim);

							masterBankRepo.getReferenceById(rekTujuan).setSaldo(tujuan.getSaldo() + nominal);

							HistoryBank historyBank = new HistoryBank();
							historyBank.setNama(pengirim.getNama());
							historyBank.setRekening(pengirim);
							historyBank.setNoRekTujuan(rekTujuan);
							historyBank.setStatusKet((byte) 3);
							historyBank.setUang(nominal);
							historyBank.setNamaTujuan(tujuan.getNama());
							historyBankRepo.save(historyBank);

							TransferWrapper transfer = new TransferWrapper();
							transfer.setRekAsal(rekAsal);
							transfer.setNamaPengirim(pengirim.getNama());
							transfer.setRekTujuan(rekTujuan);
							transfer.setNamaPenerima(tujuan.getNama());
							transfer.setNominal(nominal);
							transfer.setTanggal(transaksiNasabah.getTanggal());
							transfer.setSaldoPengirim(pengirim.getSaldo());
							transfer.setSaldoPenerima(tujuan.getSaldo());
							return transfer;

						} else {
							throw new BusinessException("Saldo Anda tidak cukup");
						}
					} else {
						throw new BusinessException("Nominal transaksi minimal 10.000");
					}
				} else {
					throw new BusinessException("Nomor rekening pemilik dan tujuan tidak boleh sama");
				}
			} else {
				throw new BusinessException("Nomor rekening tujuan tidak terdaftar");
			}
		} else {
			throw new BusinessException("Nomor rekening pengirim tidak terdaftar");
		}
	}

	// --------------------------------------BayarTelponTotal----------------------------------------------
	public List<BayarTeleponWrapper> bayarTelpon(Long rekAsal, Long noTelpon) {
		List<BayarTeleponWrapper> wrapperList = new ArrayList<BayarTeleponWrapper>();

		if (masterBankRepo.findById(rekAsal).isPresent()) {
			MasterBank masterBank = masterBankRepo.getReferenceById(rekAsal);

			if (masterPelangganRepo.findByNoTelp(noTelpon) != null) {
				MasterPelanggan masterPelanggan = masterPelangganRepo.findByNoTelp(noTelpon);
				List<TransaksiTelkom> transaksiTelkom = transaksiTelkomRepo
						.findByTagihanPelanggan(masterPelanggan.getIdPelanggan());

				Long tagihan = transaksiTelkomRepo.tagihanTelpon(masterPelanggan.getIdPelanggan());

				if (masterBank.getSaldo() - tagihan >= 50000) {

					masterBank.setSaldo(masterBank.getSaldo() - tagihan);
					masterBankRepo.save(masterBank);

					HistoryBank historyBank = new HistoryBank();
					historyBank.setNama(masterBank.getNama());
					historyBank.setRekening(masterBank);
					historyBank.setStatusKet((byte) 4);
					historyBank.setUang(tagihan);
					historyBank.setNoTlp(masterBank.getNotlp());
					historyBankRepo.save(historyBank);

					for (int i = 0; i < transaksiTelkom.size(); i++) {
						if (transaksiTelkom.get(i).getStatus() == 1) {

							HistoryTelkom historyTelkom = new HistoryTelkom();
							historyTelkom.setBulanTagihan(transaksiTelkom.get(i).getBulanTagihan());
							historyTelkom.setIdPelanggan(masterPelanggan);
							historyTelkom.setTahunTagihan(transaksiTelkom.get(i).getTahunTagihan());
							historyTelkom.setUang(transaksiTelkom.get(i).getUang());
							historyTelkom.setIdHistory(historyTelkom.getIdHistory());
							historyTelkomRepo.save(historyTelkom);

							TransaksiNasabah transaksiNasabah = new TransaksiNasabah();
							transaksiNasabah.setStatus("K");
							transaksiNasabah.setStatusKet((byte) 4);
							transaksiNasabah.setUang(transaksiTelkom.get(i).getUang());
							transaksiNasabah.setMasterBank(masterBank);
							transaksiNasabahRepo.save(transaksiNasabah);

							transaksiTelkom.get(i).setStatus((byte) 2);
							transaksiTelkomRepo.save(transaksiTelkom.get(i));
						}
					}
					BayarTeleponWrapper wrapper = new BayarTeleponWrapper();
					wrapper.setIdPelanggan(masterPelanggan.getIdPelanggan());
					wrapper.setNamaPelanggan(masterPelanggan.getNama());
					wrapper.setNoTelepon(masterPelanggan.getNoTelp());
					wrapper.setTagihan(tagihan);
					wrapper.setNoRekening(rekAsal);
					wrapper.setNamaRekening(masterBank.getNama());
					wrapper.setSaldo(masterBank.getSaldo());
					wrapper.setTanggal(historyBank.getTanggal());
					wrapperList.add(wrapper);

				} else {
					throw new BusinessException("Saldo Anda tidak cukup");
				}
			} else {
				throw new BusinessException("Nomor telepon tidak terdaftar");
			}
		} else {
			throw new BusinessException("Nomor rekening tidak terdaftar");
		}

		return wrapperList;
	}

	// --------------------------------------BayarTelponPerBulan----------------------------------------------
	public List<BayarTeleponWrapper> bayarTelponPerbulan(Long rekAsal, Long noTelpon, Byte bulanTagihan) {
		List<BayarTeleponWrapper> wrapperList = new ArrayList<BayarTeleponWrapper>();

		if (masterBankRepo.findById(rekAsal).isPresent()) {
			MasterBank masterBank = masterBankRepo.getReferenceById(rekAsal);

			if (masterPelangganRepo.findByNoTelp(noTelpon) != null) {
				MasterPelanggan masterPelanggan = masterPelangganRepo.findByNoTelp(noTelpon);
				List<TransaksiTelkom> transaksiTelkom = transaksiTelkomRepo
						.findByTagihanPelanggan(masterPelanggan.getIdPelanggan());

				Long tagihan = transaksiTelkomRepo.tagihanTelpon(masterPelanggan.getIdPelanggan());

				if (masterBank.getSaldo() - tagihan >= 50000) {

					masterBank.setSaldo(masterBank.getSaldo() - tagihan);
					masterBankRepo.save(masterBank);

					HistoryBank historyBank = new HistoryBank();
					historyBank.setNama(masterBank.getNama());
					historyBank.setRekening(masterBank);
					historyBank.setStatusKet((byte) 4);
					historyBank.setUang(tagihan);
					historyBank.setNoTlp(masterBank.getNotlp());
					historyBankRepo.save(historyBank);

					for (int i = 0; i < transaksiTelkom.size(); i++) {
						if (transaksiTelkom.get(i).getStatus() == 1) {
							if (transaksiTelkom.get(i).getBulanTagihan() == bulanTagihan) {

								HistoryTelkom historyTelkom = new HistoryTelkom();
								historyTelkom.setBulanTagihan(transaksiTelkom.get(i).getBulanTagihan());
								historyTelkom.setIdPelanggan(masterPelanggan);
								historyTelkom.setTahunTagihan(transaksiTelkom.get(i).getTahunTagihan());
								historyTelkom.setUang(transaksiTelkom.get(i).getUang());
								historyTelkom.setIdHistory(historyTelkom.getIdHistory());
								historyTelkomRepo.save(historyTelkom);

								TransaksiNasabah transaksiNasabah = new TransaksiNasabah();
								transaksiNasabah.setStatus("K");
								transaksiNasabah.setStatusKet((byte) 4);
								transaksiNasabah.setUang(transaksiTelkom.get(i).getUang());
								transaksiNasabah.setMasterBank(masterBank);
								transaksiNasabahRepo.save(transaksiNasabah);

								transaksiTelkom.get(i).setStatus((byte) 2);
								transaksiTelkomRepo.save(transaksiTelkom.get(i));

								BayarTeleponWrapper wrapper = new BayarTeleponWrapper();
								wrapper.setIdTransaksi(transaksiTelkom.get(i).getIdTransaksi());
								wrapper.setIdPelanggan(masterPelanggan.getIdPelanggan());
								wrapper.setNamaPelanggan(masterPelanggan.getNama());
								wrapper.setNoTelepon(masterPelanggan.getNoTelp());
								wrapper.setBulanTagihan(transaksiTelkom.get(i).getBulanTagihan());
								wrapper.setTahunTagihan(transaksiTelkom.get(i).getTahunTagihan());
								wrapper.setTagihan(transaksiTelkom.get(i).getUang());
								wrapper.setStatus(transaksiTelkom.get(i).getStatus());
								wrapper.setNoRekening(rekAsal);
								wrapper.setNamaRekening(masterBank.getNama());
								wrapper.setSaldo(masterBank.getSaldo());
								wrapper.setTanggal(historyBank.getTanggal());
								wrapperList.add(wrapper);
							}
						}
					}

				} else {
					throw new BusinessException("Saldo Anda tidak cukup");
				}
			} else {
				throw new BusinessException("Nomor telepon tidak terdaftar");
			}
		} else {
			throw new BusinessException("Nomor rekening tidak terdaftar");
		}

		return wrapperList;
	}

	public PdfPCell Left(String title) {
		PdfPCell cell = new PdfPCell(new Phrase(title));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPadding(7);
		cell.setPaddingLeft(5);
		return cell;
	}
	public PdfPCell Right(String title) {
		PdfPCell cell = new PdfPCell(new Phrase(title));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPadding(7);
		cell.setPaddingRight(5);
		return cell;
	}


//	---------------------------------Bukti Transaksi Setor--------------------------------------
	public void ExportToPdfSetor(HttpServletResponse response) throws Exception {
		List<HistoryBank> data = historyBankRepo.findLastHistory();
		// Now create a new iText PDF document
		Document pdfDoc = new Document(PageSize.A6);
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
		pdfDoc.open();
		
		BaseColor color = new BaseColor(245, 128, 11);
	    Paragraph title = new Paragraph("BANK XYZ",
	            new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, color));
	    title.setAlignment(Element.ALIGN_CENTER);
	    pdfDoc.add(title);

		Paragraph notif = new Paragraph("Transaksi Berhasil", new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD));
		notif.setAlignment(Element.ALIGN_CENTER);
		pdfDoc.add(notif);
		// Add the generation date
		pdfDoc.add(new Paragraph(""));

		// Create a table
		PdfPTable pdfTable = new PdfPTable(2);
		pdfTable.getDefaultCell().setBorderWidth(1);
		pdfTable.setPaddingTop(100);

		pdfTable.setWidthPercentage(100);
		pdfTable.setSpacingBefore(10f);
		pdfTable.setSpacingAfter(10f);

		pdfTable.addCell("");
		pdfTable.addCell("");
		pdfTable.addCell(Left("Tanggal"));
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String formattedDate = "-";
		if (data.get(0).getTanggal() != null) {
			formattedDate = formatter.format(data.get(0).getTanggal());
		}
		pdfTable.addCell(Right(formattedDate));
		
		pdfTable.addCell(Left("Nomor Rekening"));
		pdfTable.addCell(Right(String.valueOf(data.get(0).getRekening().getNorek()) != null ? String.valueOf(data.get(0).getRekening().getNorek()) : "-"));
		pdfTable.addCell(Left("Nama Nasabah"));
		pdfTable.addCell(Right(String.valueOf(data.get(0).getNama() != null ? String.valueOf(data.get(0).getNama()) : "-")));
		pdfTable.addCell(Left("Jenis Transaksi"));
		pdfTable.addCell(Right("Setor Tunai"));
		pdfTable.addCell(Left("Nominal"));
		pdfTable.addCell(Right(String.valueOf(data.get(0).getUang() != null ? String.valueOf(data.get(0).getUang()) : "-")));

		// Add the table to the pdf document
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		pdfWriter.close();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}
	
//	---------------------------------Bukti Transaksi Tarik--------------------------------------
	public void ExportToPdfTarik(HttpServletResponse response) throws Exception {
		List<HistoryBank> data = historyBankRepo.findLastHistory();
		// Now create a new iText PDF document
		Document pdfDoc = new Document(PageSize.A6);
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
		pdfDoc.open();
		
		BaseColor color = new BaseColor(245, 128, 11);
	    Paragraph title = new Paragraph("BANK XYZ",
	            new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, color));
	    title.setAlignment(Element.ALIGN_CENTER);
	    pdfDoc.add(title);

		Paragraph notif = new Paragraph("Transaksi Berhasil", new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD));
		notif.setAlignment(Element.ALIGN_CENTER);
		pdfDoc.add(notif);
		// Add the generation date
		pdfDoc.add(new Paragraph(""));

		// Create a table
		PdfPTable pdfTable = new PdfPTable(2);
		pdfTable.getDefaultCell().setBorderWidth(1);
		pdfTable.setPaddingTop(100);

		pdfTable.setWidthPercentage(100);
		pdfTable.setSpacingBefore(10f);
		pdfTable.setSpacingAfter(10f);

		pdfTable.addCell("");
		pdfTable.addCell("");
		pdfTable.addCell(Left("Tanggal"));
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String formattedDate = "-";
		if (data.get(0).getTanggal() != null) {
			formattedDate = formatter.format(data.get(0).getTanggal());
		}
		pdfTable.addCell(Right(formattedDate));
		
		pdfTable.addCell(Left("Nomor Rekening"));
		pdfTable.addCell(Right(String.valueOf(data.get(0).getRekening().getNorek()) != null ? String.valueOf(data.get(0).getRekening().getNorek()) : "-"));
		pdfTable.addCell(Left("Nama Nasabah"));
		pdfTable.addCell(Right(String.valueOf(data.get(0).getNama() != null ? String.valueOf(data.get(0).getNama()) : "-")));
		pdfTable.addCell(Left("Jenis Transaksi"));
		pdfTable.addCell(Right("Tarik Tunai"));
		pdfTable.addCell(Left("Nominal"));
		pdfTable.addCell(Right(String.valueOf(data.get(0).getUang() != null ? String.valueOf(data.get(0).getUang()) : "-")));

		// Add the table to the pdf document
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		pdfWriter.close();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}
	
//	---------------------------------Bukti Transaksi Transfer--------------------------------------
	public void ExportToPdfTransfer(HttpServletResponse response) throws Exception {
		List<HistoryBank> data = historyBankRepo.findLastHistory();
		// Now create a new iText PDF document
		Document pdfDoc = new Document(PageSize.A6);
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
		pdfDoc.open();
		
		BaseColor color = new BaseColor(245, 128, 11);
	    Paragraph title = new Paragraph("BANK XYZ",
	            new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, color));
	    title.setAlignment(Element.ALIGN_CENTER);
	    pdfDoc.add(title);

		Paragraph notif = new Paragraph("Transaksi Berhasil", new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD));
		notif.setAlignment(Element.ALIGN_CENTER);
		pdfDoc.add(notif);
		// Add the generation date
		pdfDoc.add(new Paragraph(""));

		// Create a table
		PdfPTable pdfTable = new PdfPTable(2);
		pdfTable.getDefaultCell().setBorderWidth(1);
		pdfTable.setPaddingTop(100);

		pdfTable.setWidthPercentage(100);
		pdfTable.setSpacingBefore(10f);
		pdfTable.setSpacingAfter(10f);

		pdfTable.addCell("");
		pdfTable.addCell("");
		pdfTable.addCell(Left("Tanggal"));
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String formattedDate = "-";
		if (data.get(0).getTanggal() != null) {
			formattedDate = formatter.format(data.get(0).getTanggal());
		}
		pdfTable.addCell(Right(formattedDate));
		
		pdfTable.addCell(Left("Nomor Rekening Pengirim"));
		pdfTable.addCell(Right(String.valueOf(data.get(0).getRekening().getNorek()) != null ? String.valueOf(data.get(0).getRekening().getNorek()) : "-"));
		pdfTable.addCell(Left("Nama Nasabah Pengirim"));
		pdfTable.addCell(Right(String.valueOf(data.get(0).getNama() != null ? String.valueOf(data.get(0).getNama()) : "-")));
		pdfTable.addCell(Left("Jenis Transaksi"));
		pdfTable.addCell(Right("Transfer"));
		pdfTable.addCell(Left("Nomor Rekening Tujuan"));
		pdfTable.addCell(Right(String.valueOf(data.get(0).getNoRekTujuan() != null ? String.valueOf(data.get(0).getNoRekTujuan()) : "-")));
		pdfTable.addCell(Left("Nama Nasabah Tujuan"));
		pdfTable.addCell(Right(String.valueOf(data.get(0).getNamaTujuan() != null ? String.valueOf(data.get(0).getNamaTujuan()) : "-")));
		pdfTable.addCell(Left("Nominal"));
		pdfTable.addCell(Right(String.valueOf(data.get(0).getUang() != null ? String.valueOf(data.get(0).getUang()) : "-")));

		// Add the table to the pdf document
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		pdfWriter.close();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}
	
//	---------------------------------Bukti Transaksi Bayar Telepon--------------------------------------
	public void ExportToPdfBayarTelepon(HttpServletResponse response) throws Exception {
		List<HistoryTelkom> dataTelepon = historyTelkomRepo.findLastHistoryTelkom();
		List<HistoryBank> dataNasabah = historyBankRepo.findLastHistory();
		// Now create a new iText PDF document
		Document pdfDoc = new Document(PageSize.A6);
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
		pdfDoc.open();
		
		BaseColor color = new BaseColor(245, 128, 11);
	    Paragraph title = new Paragraph("BANK XYZ",
	            new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, color));
	    title.setAlignment(Element.ALIGN_CENTER);
	    pdfDoc.add(title);

		Paragraph notif = new Paragraph("Transaksi Berhasil", new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD));
		notif.setAlignment(Element.ALIGN_CENTER);
		pdfDoc.add(notif);
		// Add the generation date
		pdfDoc.add(new Paragraph(""));

		// Create a table
		PdfPTable pdfTable = new PdfPTable(2);
		pdfTable.getDefaultCell().setBorderWidth(1);
		pdfTable.setPaddingTop(100);

		pdfTable.setWidthPercentage(100);
		pdfTable.setSpacingBefore(10f);
		pdfTable.setSpacingAfter(10f);

		pdfTable.addCell("");
		pdfTable.addCell("");
		pdfTable.addCell(Left("Tanggal"));
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String formattedDate = "-";
		if (dataNasabah.get(0).getTanggal() != null) {
			formattedDate = formatter.format(dataNasabah.get(0).getTanggal());
		}
		pdfTable.addCell(Right(formattedDate));
		
		pdfTable.addCell(Left("Nomor Rekening"));
		pdfTable.addCell(Right(String.valueOf(dataNasabah.get(0).getRekening().getNorek()) != null ? String.valueOf(dataNasabah.get(0).getRekening().getNorek()) : "-"));
		pdfTable.addCell(Left("Nama Nasabah"));
		pdfTable.addCell(Right(String.valueOf(dataNasabah.get(0).getNama() != null ? String.valueOf(dataNasabah.get(0).getNama()) : "-")));
		pdfTable.addCell(Left("Jenis Transaksi"));
		pdfTable.addCell(Right("Bayar Telepon"));
		pdfTable.addCell(Left("Nomor Telepon"));
		pdfTable.addCell(Right(String.valueOf(dataTelepon.get(0).getIdPelanggan().getNoTelp()) != null ? String.valueOf(dataTelepon.get(0).getIdPelanggan().getNoTelp()) : "-"));
		pdfTable.addCell(Left("Nama Pelanggan Telepon"));
		pdfTable.addCell(Right(String.valueOf(dataTelepon.get(0).getIdPelanggan().getNama() != null ? String.valueOf(dataTelepon.get(0).getIdPelanggan().getNama()) : "-")));
		pdfTable.addCell(Left("Nominal"));
		pdfTable.addCell(Right(String.valueOf(dataNasabah.get(0).getUang() != null ? String.valueOf(dataNasabah.get(0).getUang()) : "-")));

		// Add the table to the pdf document
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		pdfWriter.close();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}

}