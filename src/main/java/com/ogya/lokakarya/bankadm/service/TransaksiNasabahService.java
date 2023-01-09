package com.ogya.lokakarya.bankadm.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	// -------------------------------------------cek
	// saldo----------------------------------------
	public MasterBankWrapper cekSaldo(Long rekening) {
		if (masterBankRepo.findById(rekening).isPresent()) {
			MasterBank nasabah = masterBankRepo.getReferenceById(rekening);
			MasterBankWrapper wrapper = new MasterBankWrapper();
			wrapper.setNorek(nasabah.getNorek());
			wrapper.setNama(nasabah.getNama());
			wrapper.setSaldo(nasabah.getSaldo());
			return wrapper;
		} else {
			throw new BusinessException("Rekening tidak terdaftar");
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
			} else {
				throw new BusinessException("No telepon tidak terdaftar");
			}
		} else {
			throw new BusinessException("Rekening tidak terdaftar");
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
				throw new BusinessException("Nominal transaksi minimal 10000");
			}
		} else {
			throw new BusinessException("Rekening tidak terdaftar");
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
				throw new BusinessException("Nominal transaksi minimal 10000");
			}
		} else {
			throw new BusinessException("Rekening tidak terdaftar");
		}
	}

	// -------------------------------------Transfer-------------------------------------------------
	public TransferWrapper transfer(Long rekTujuan, Long rekAsal, Long nominal) {

		if (masterBankRepo.findById(rekAsal).isPresent() && masterBankRepo.findById(rekTujuan).isPresent()) {
			
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
					throw new BusinessException("Nominal transaksi minimal 10000");
				}
			} else {
				throw new BusinessException("Nomor rekening tidak boleh sama");
			}
		} else {
			throw new BusinessException("Rekening tidak terdaftar");
		}
	}

	// --------------------------------------BayarTelpon----------------------------------------------
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

				} else {
					throw new BusinessException("Saldo Anda tidak cukup");
				}
			} else {
				throw new BusinessException("No telepon tidak terdaftar");
			}
		} else {
			throw new BusinessException("Rekening tidak terdaftar");
		}

		return wrapperList;
	}
}