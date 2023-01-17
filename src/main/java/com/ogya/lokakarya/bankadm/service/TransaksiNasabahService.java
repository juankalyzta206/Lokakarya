package com.ogya.lokakarya.bankadm.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
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
import com.ogya.lokakarya.exercise.feign.nasabah.request.TarikFeignRequest;
import com.ogya.lokakarya.exercise.feign.nasabah.response.NasabahFeignResponse;
import com.ogya.lokakarya.exercise.feign.nasabah.services.NasabahFeignService;
import com.ogya.lokakarya.exercise.feign.transfer.request.TransferFeignRequest;
import com.ogya.lokakarya.exercise.feign.transfer.response.TransferFeignResponse;
import com.ogya.lokakarya.exercise.feign.transfer.response.ValidateRekeningFeignResponse;
import com.ogya.lokakarya.exercise.feign.transfer.services.TransferFeignService;
import com.ogya.lokakarya.telepon.entity.HistoryTelkom;
import com.ogya.lokakarya.telepon.entity.MasterPelanggan;
import com.ogya.lokakarya.telepon.entity.TransaksiTelkom;
import com.ogya.lokakarya.telepon.repository.HistoryTelkomRepository;
import com.ogya.lokakarya.telepon.repository.MasterPelangganRepository;
import com.ogya.lokakarya.telepon.repository.TransaksiTelkomRepository;
import com.ogya.lokakarya.telepon.wrapper.BayarTeleponWrapper;
import com.ogya.lokakarya.usermanagement.entity.Users;
import com.ogya.lokakarya.usermanagement.repository.UsersRepository;
import com.ogya.lokakarya.util.DataResponseFeign;

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
	@Autowired
	NasabahFeignService nasabahFeignService;
	@Autowired
	UsersRepository usersRepository;
	@Autowired
	TransferFeignService transferService;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private TemplateEngine templateEngine;
    private Logger logger = LoggerFactory.getLogger(TransaksiNasabahService.class);

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
				wrapper.setIdTransaksi(historyBank.getIdHistoryBank());
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

	public DataResponseFeign<SetorAmbilWrapper> tarik(Long rekening, Long nominal) {
		DataResponseFeign<SetorAmbilWrapper> dataResponse = new DataResponseFeign<SetorAmbilWrapper>();
		if (masterBankRepo.findById(rekening).isPresent()) {
		MasterBank nasabah = masterBankRepo.getReferenceById(rekening);
		TransaksiNasabah transaksi = new TransaksiNasabah();
		HistoryBank historyBank = new HistoryBank();
        if (nominal >= 10000) {
            if (nasabah.getSaldo() - nominal >= 50000) {
                TarikFeignRequest tarikRequest = new TarikFeignRequest();
                tarikRequest.setNoRekening(rekening.toString());
                tarikRequest.setTarikan(nominal);

                try {
                    NasabahFeignResponse response = nasabahFeignService.callTarik(tarikRequest);
                    if (response.getSuccess()) {
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
                        wrapper.setIdTransaksi(historyBank.getIdHistoryBank());
                        wrapper.setNamaNasabah(nasabah.getNama());
                        wrapper.setNominal(nominal);
                        wrapper.setNomorRekening(rekening);
                        wrapper.setSaldo(nasabah.getSaldo());
                        wrapper.setTanggal(transaksi.getTanggal());
                        dataResponse.setData(wrapper);
                        
                        dataResponse.setSuccess(true);
        	            dataResponse.setReferenceNumber(response.getReferenceNumber());
                        return dataResponse;
                        } else {
                        dataResponse.setSuccess(false);
                        dataResponse.setMessage("Failed to withdraw. Please contact customer service.");
                        return dataResponse;
                        }
                        } catch (Exception e) {
                        logger.error("Error while withdrawing", e);
                        dataResponse.setSuccess(false);
                        dataResponse.setMessage("Failed to withdraw. Please contact customer service.");
                        return dataResponse;
                        }
                        } else {
                        dataResponse.setSuccess(false);
                        dataResponse.setMessage("Saldo Anda tidak cukup");
                        return dataResponse;
                        }
                        } else {
                        dataResponse.setSuccess(false);
                        dataResponse.setMessage("Nominal transaksi minimal Rp10.000,00.");
                        return dataResponse;
                        }
                        } else {
                        dataResponse.setSuccess(false);
                        dataResponse.setMessage("Nomor rekening tidak terdaftar");
                        return dataResponse;
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
							transfer.setIdTransaksi(historyBank.getIdHistoryBank());
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

//	public TransferWrapper transferValidate(HttpServletResponse response, Long rekTujuan, Long rekAsal, Long nominal)
//			throws MessagingException, IOException, DocumentException {
//		ValidateRekeningFeignResponse rekValidatePengirim = transferService.callValidateRekening(rekAsal.toString());
//		ValidateRekeningFeignResponse rekValidatePenerima = transferService.callValidateRekening(rekTujuan.toString());
//
//		TransferFeignRequest transferRequest = new TransferFeignRequest();
//		transferRequest.setJumlahTranfer(nominal);
//		transferRequest.setNoRekeningPengirim(rekAsal.toString());
//		transferRequest.setNoRekeningPenerima(rekTujuan.toString());
//
//		if (rekValidatePengirim.getRegistered() == true) {
//			if (rekValidatePenerima.getRegistered() == true) {
//				TransferFeignResponse transferResponse = transferService.callTransfer(transferRequest);
//				System.out.println("Reference Number : " + transferResponse.getReferenceNumber());
//				System.out.println("Success : " + transferResponse.getSuccess());
//
//				transfer(rekTujuan, rekAsal, nominal);
//				ExportToPdfTransferParam(response, transfer(rekTujuan, rekAsal, nominal).getIdTransaksi());
//				MimeMessage mailMessage = javaMailSender.createMimeMessage();
//				MasterBank masterBank = masterBankRepo.getReferenceById(rekAsal);
//				List<Users> users = usersRepository.findByUserId(masterBank.getUserId());
//				try {
//
//					MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
//
//					ByteArrayOutputStream outputStream = ExportToPdfTransferParam(response,
//							transfer(rekTujuan, rekAsal, nominal).getIdTransaksi());
//
////					helper.setFrom("admin@xyzbank.com");
//					helper.setTo(users.get(0).getEmail().toString());
//					helper.setSubject("Laporan Transaksi Transfer Bank");
//					helper.addAttachment("BuktiTransfer.pdf", new ByteArrayResource(outputStream.toByteArray()));
//
//					Context ctx = new Context(LocaleContextHolder.getLocale());
//					ctx.setVariable("name", users.get(0).getNama());
//					ctx.setVariable("rekTujuan", rekTujuan.toString());
//					ctx.setVariable("nomorReference", transferResponse.getReferenceNumber());
//					ctx.setVariable("tanggal", transfer(rekTujuan, rekAsal, nominal).getTanggal().toString());
//					ctx.setVariable("nominal", transfer(rekTujuan, rekAsal, nominal).getNominal().toString());
//
//					String body = templateEngine.process("TransferPengirim", ctx);
//					helper.setText(body, true);
//
//					javaMailSender.send(mailMessage);
//
//					System.out.println("Email: " + users.get(0).getEmail());
//					System.out.println("name: " + users.get(0).getNama());
//					System.out.println("rekTujuan: " + rekTujuan.toString());
//					System.out.println("tanggal: " + transfer(rekTujuan, rekAsal, nominal).getTanggal());
//					System.out.println("nominal: " + transfer(rekTujuan, rekAsal, nominal).getNominal());
//					System.out.println("");
//					System.out.println("Email send");
//
//				} catch (MessagingException e) {
//					System.err.println("Failed send email");
//					e.printStackTrace();
//				}
//				return transfer(rekTujuan, rekAsal, nominal);
//
//			} else {
//				throw new BusinessException("Rekening pengirim tidak terdaftar");
//			}
//		} else {
//			throw new BusinessException("Rekening tujuan tidak terdaftar");
//		}
//
//	}
	
	public TransferWrapper transferValidate(HttpServletResponse response, Long rekTujuan, Long rekAsal, Long nominal)
			throws MessagingException, IOException, DocumentException {
		ValidateRekeningFeignResponse rekValidatePengirim = transferService.callValidateRekening(rekAsal.toString());
		ValidateRekeningFeignResponse rekValidatePenerima = transferService.callValidateRekening(rekTujuan.toString());

		TransferFeignRequest transferRequest = new TransferFeignRequest();
		transferRequest.setJumlahTranfer(nominal);
		transferRequest.setNoRekeningPengirim(rekAsal.toString());
		transferRequest.setNoRekeningPenerima(rekTujuan.toString());

		if (rekValidatePengirim.getRegistered() == true) {
			if (rekValidatePenerima.getRegistered() == true) {
				TransferFeignResponse transferResponse = transferService.callTransfer(transferRequest);
				System.out.println("Reference Number : " + transferResponse.getReferenceNumber());
				System.out.println("Success : " + transferResponse.getSuccess());

				transfer(rekTujuan, rekAsal, nominal);
//				ExportToPdfTransferParam(response, transfer(rekTujuan, rekAsal, nominal).getIdTransaksi());
				MasterBank pengirim = masterBankRepo.getReferenceById(rekAsal);
				List<Users> userPengirim = usersRepository.findByUserId(pengirim.getUserId());
				MasterBank tujuan = masterBankRepo.getReferenceById(rekTujuan);
				List<Users> userTujuan = usersRepository.findByUserId(tujuan.getUserId());
//				
				Context ctxPengirim = new Context();
				ctxPengirim.setVariable("name", userPengirim.get(0).getNama());
				ctxPengirim.setVariable("rekTujuan", rekTujuan.toString());
				ctxPengirim.setVariable("nomorReference", transferResponse.getReferenceNumber());
				ctxPengirim.setVariable("tanggal", transfer(rekTujuan, rekAsal, nominal).getTanggal().toString());
				ctxPengirim.setVariable("nominal", transfer(rekTujuan, rekAsal, nominal).getNominal().toString());
				
				ByteArrayOutputStream pdf = ExportToPdfTransferParam(response,
						transfer(rekTujuan, rekAsal, nominal).getIdTransaksi());
				
				sendEmailTransfer(userPengirim.get(0).getEmail().toString(), "TransferPengirim", ctxPengirim, pdf);
				
				Context ctxTujuan = new Context();
				ctxTujuan.setVariable("name", userTujuan.get(0).getNama());
				ctxTujuan.setVariable("rekAsal", rekAsal.toString());
				ctxTujuan.setVariable("nomorReference", transferResponse.getReferenceNumber());
				ctxTujuan.setVariable("tanggal", transfer(rekTujuan, rekAsal, nominal).getTanggal().toString());
				ctxTujuan.setVariable("nominal", transfer(rekTujuan, rekAsal, nominal).getNominal().toString());
				sendEmailTransfer(userTujuan.get(0).getEmail().toString(), "TransferPenerima", ctxTujuan, pdf);
				return transfer(rekTujuan, rekAsal, nominal);

			} else {
				throw new BusinessException("Rekening pengirim tidak terdaftar");
			}
		} else {
			throw new BusinessException("Rekening tujuan tidak terdaftar");
		}

	}

//	-----------------------------------------KirimEmail-----------------------------------------------------
	public void sendEmailTransfer(String tujuan, String templateName, Context context, ByteArrayOutputStream pdf)
			throws MessagingException {
		MimeMessage mailMessage = javaMailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);

		helper.setTo(tujuan);
		helper.setSubject("Laporan Transaksi Transfer Bank");
		String html = templateEngine.process(templateName, context);
		helper.setText(html, true);
		helper.addAttachment("file.pdf", new ByteArrayResource(pdf.toByteArray()));
		javaMailSender.send(mailMessage);
		System.out.println("Email send");

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
					List<HistoryTelkom> dataHistory = historyTelkomRepo.dataTeleponById(masterPelanggan);
					BayarTeleponWrapper wrapper = new BayarTeleponWrapper();
					wrapper.setIdTransaksiBank(historyBank.getIdHistoryBank());
					wrapper.setIdTransaksiTelp(dataHistory.get(0).getIdHistory());
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
								wrapper.setIdTransaksiTelp(historyTelkom.getIdHistory());
								wrapper.setIdTransaksiBank(historyBank.getIdHistoryBank());
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

	public Font BlueFont() {
		BaseColor color = new BaseColor(15, 122, 252);
		Font font1 = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, color);
		return font1;
	}

	public Font OrangeFont() {
		BaseColor color = new BaseColor(245, 128, 11);
		Font font2 = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, color);
		return font2;
	}

//	---------------------------------Bukti Transaksi Setor--------------------------------------
	public void ExportToPdfSetorParam(HttpServletResponse response, Long idHistory) throws Exception {
		HistoryBank data = historyBankRepo.getReferenceById(idHistory);
		// Now create a new iText PDF document
		Document pdfDoc = new Document(PageSize.A6);
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
		pdfDoc.open();

		Paragraph title = new Paragraph();
		title.add(new Chunk("BANK ", BlueFont()));
		title.add(new Chunk("XYZ", OrangeFont()));
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
		if (data.getTanggal() != null) {
			formattedDate = formatter.format(data.getTanggal());
		}
		pdfTable.addCell(Right(formattedDate));

		pdfTable.addCell(Left("Nomor Rekening"));
		pdfTable.addCell(Right(
				String.valueOf(data.getRekening().getNorek()) != null ? String.valueOf(data.getRekening().getNorek())
						: "-"));
		pdfTable.addCell(Left("Nama Nasabah"));
		pdfTable.addCell(Right(String.valueOf(data.getNama() != null ? String.valueOf(data.getNama()) : "-")));
		pdfTable.addCell(Left("Jenis Transaksi"));
		pdfTable.addCell(Right("Setor Tunai"));
		pdfTable.addCell(Left("Nominal Transaksi"));
		pdfTable.addCell(Right(String.valueOf(data.getUang() != null ? String.valueOf(data.getUang()) : "-")));
		pdfTable.addCell(Left("Saldo Nasabah"));
		pdfTable.addCell(Right(String
				.valueOf(data.getRekening().getSaldo() != null ? String.valueOf(data.getRekening().getSaldo()) : "-")));

		// Add the table to the pdf document
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		pdfWriter.close();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}

//	---------------------------------Bukti Transaksi Tarik--------------------------------------
	public void ExportToPdfTarikParam(HttpServletResponse response, Long idHistory) throws Exception {
		HistoryBank data = historyBankRepo.getReferenceById(idHistory);
		// Now create a new iText PDF document
		Document pdfDoc = new Document(PageSize.A6);
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
		pdfDoc.open();

		Paragraph title = new Paragraph();
		title.add(new Chunk("BANK ", BlueFont()));
		title.add(new Chunk("XYZ", OrangeFont()));
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
		if (data.getTanggal() != null) {
			formattedDate = formatter.format(data.getTanggal());
		}
		pdfTable.addCell(Right(formattedDate));

		pdfTable.addCell(Left("Nomor Rekening"));
		pdfTable.addCell(Right(
				String.valueOf(data.getRekening().getNorek()) != null ? String.valueOf(data.getRekening().getNorek())
						: "-"));
		pdfTable.addCell(Left("Nama Nasabah"));
		pdfTable.addCell(Right(String.valueOf(data.getNama() != null ? String.valueOf(data.getNama()) : "-")));
		pdfTable.addCell(Left("Jenis Transaksi"));
		pdfTable.addCell(Right("Tarik Tunai"));
		pdfTable.addCell(Left("Nominal Transaksi"));
		pdfTable.addCell(Right(String.valueOf(data.getUang() != null ? String.valueOf(data.getUang()) : "-")));
		pdfTable.addCell(Left("Saldo Nasabah"));
		pdfTable.addCell(Right(String
				.valueOf(data.getRekening().getSaldo() != null ? String.valueOf(data.getRekening().getSaldo()) : "-")));

		// Add the table to the pdf document
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		pdfWriter.close();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}

//	---------------------------------Bukti Transaksi Transfer--------------------------------------
	public ByteArrayOutputStream ExportToPdfTransferParam(HttpServletResponse response, Long idHistory)
			throws DocumentException, IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		HistoryBank data = historyBankRepo.getReferenceById(idHistory);
		// Now create a new iText PDF document
		Document pdfDoc = new Document(PageSize.A6);
		PdfWriter.getInstance(pdfDoc, outputStream);
		pdfDoc.open();

		Paragraph title = new Paragraph();
		title.add(new Chunk("BANK ", BlueFont()));
		title.add(new Chunk("XYZ", OrangeFont()));
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
		if (data.getTanggal() != null) {
			formattedDate = formatter.format(data.getTanggal());
		}
		pdfTable.addCell(Right(formattedDate));

		pdfTable.addCell(Left("Nomor Rekening Pengirim"));
		pdfTable.addCell(Right(
				String.valueOf(data.getRekening().getNorek()) != null ? String.valueOf(data.getRekening().getNorek())
						: "-"));
		pdfTable.addCell(Left("Nama Nasabah Pengirim"));
		pdfTable.addCell(Right(String.valueOf(data.getNama() != null ? String.valueOf(data.getNama()) : "-")));
		pdfTable.addCell(Left("Jenis Transaksi"));
		pdfTable.addCell(Right("Transfer"));
		pdfTable.addCell(Left("Nomor Rekening Tujuan"));
		pdfTable.addCell(
				Right(String.valueOf(data.getNoRekTujuan() != null ? String.valueOf(data.getNoRekTujuan()) : "-")));
		pdfTable.addCell(Left("Nama Nasabah Tujuan"));
		pdfTable.addCell(
				Right(String.valueOf(data.getNamaTujuan() != null ? String.valueOf(data.getNamaTujuan()) : "-")));
		pdfTable.addCell(Left("Nominal Transaksi"));
		pdfTable.addCell(Right(String.valueOf(data.getUang() != null ? String.valueOf(data.getUang()) : "-")));
		pdfTable.addCell(Left("Saldo Pengirim"));
		pdfTable.addCell(Right(String
				.valueOf(data.getRekening().getSaldo() != null ? String.valueOf(data.getRekening().getSaldo()) : "-")));

		// Add the table to the pdf document
		pdfDoc.add(pdfTable);

		pdfDoc.close();
//		pdfWriter.close();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=BuktiTransfer.pdf");
		return outputStream;
	}

//	---------------------------------Bukti Transaksi Bayar Telepon--------------------------------------
	public void ExportToPdfBayarTeleponParam(HttpServletResponse response, Long idHistoryBank, Long idHistoryTelp)
			throws Exception {
		List<HistoryTelkom> dataTelepon = historyTelkomRepo.findByIdHistory(idHistoryTelp);
		HistoryBank dataNasabah = historyBankRepo.getReferenceById(idHistoryBank);

		// Now create a new iText PDF document
		Document pdfDoc = new Document(PageSize.A6);
		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, response.getOutputStream());
		pdfDoc.open();

		Paragraph title = new Paragraph();
		title.add(new Chunk("BANK ", BlueFont()));
		title.add(new Chunk("XYZ", OrangeFont()));
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
		if (dataNasabah.getTanggal() != null) {
			formattedDate = formatter.format(dataNasabah.getTanggal());
		}
		pdfTable.addCell(Right(formattedDate));

		pdfTable.addCell(Left("Nomor Rekening"));
		pdfTable.addCell(Right(String.valueOf(dataNasabah.getRekening().getNorek()) != null
				? String.valueOf(dataNasabah.getRekening().getNorek())
				: "-"));
		pdfTable.addCell(Left("Nama Nasabah"));
		pdfTable.addCell(
				Right(String.valueOf(dataNasabah.getNama() != null ? String.valueOf(dataNasabah.getNama()) : "-")));
		pdfTable.addCell(Left("Jenis Transaksi"));
		pdfTable.addCell(Right("Bayar Telepon"));
		pdfTable.addCell(Left("Nomor Telepon"));
		pdfTable.addCell(Right(String.valueOf(dataTelepon.get(0).getIdPelanggan().getNoTelp()) != null
				? String.valueOf(dataTelepon.get(0).getIdPelanggan().getNoTelp())
				: "-"));
		pdfTable.addCell(Left("Nama Pelanggan Telepon"));
		pdfTable.addCell(Right(String.valueOf(dataTelepon.get(0).getIdPelanggan().getNama() != null
				? String.valueOf(dataTelepon.get(0).getIdPelanggan().getNama())
				: "-")));
		pdfTable.addCell(Left("Nominal Transaksi"));
		pdfTable.addCell(
				Right(String.valueOf(dataNasabah.getUang() != null ? String.valueOf(dataNasabah.getUang()) : "-")));
		pdfTable.addCell(Left("Saldo Nasabah"));
		pdfTable.addCell(Right(String.valueOf(
				dataNasabah.getRekening().getSaldo() != null ? String.valueOf(dataNasabah.getRekening().getSaldo())
						: "-")));

		// Add the table to the pdf document
		pdfDoc.add(pdfTable);

		pdfDoc.close();
		pdfWriter.close();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=exportedPdf.pdf");
	}
}