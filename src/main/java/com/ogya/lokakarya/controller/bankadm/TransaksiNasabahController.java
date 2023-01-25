package com.ogya.lokakarya.controller.bankadm;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.ogya.lokakarya.exercise.feign.services.nasabah.NasabahFeignService;
import com.ogya.lokakarya.notification.telepon.LaporanPenunggakanNotification;
import com.ogya.lokakarya.service.bankadm.TransaksiNasabahService;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponseList;
import com.ogya.lokakarya.wrapper.bankadm.MasterBankWrapper;
import com.ogya.lokakarya.wrapper.bankadm.SetorAmbilWrapper;
import com.ogya.lokakarya.wrapper.bankadm.TransferWrapper;
import com.ogya.lokakarya.wrapper.telepon.BayarTeleponWrapper;

@RestController
@RequestMapping(value = "/transaksiNasabah")
@CrossOrigin(origins = "*")
public class TransaksiNasabahController {
	@Autowired
	TransaksiNasabahService transaksiNasabahService;
	@Autowired
	NasabahFeignService nasabahFeignService;
	@Autowired
	LaporanPenunggakanNotification laporanPenunggakanNotification;

//	===========================================Transaksi==========================================================
	@PostMapping(path = "/transfer")
	public DataResponse<TransferWrapper> transfer(@RequestParam("Nomor Rekening Asal") Long rekAsal,
			@RequestParam("Nomor Rekening Tujuan") Long rekTujuan, @RequestParam("Nominal") Long nominal) {
		return new DataResponse<TransferWrapper>(transaksiNasabahService.transfer(rekTujuan, rekAsal, nominal));
	}

	@PostMapping(path = "/bayarTelpon")
	public DataResponseList<BayarTeleponWrapper> bayarTelpon(@RequestParam("Nomor Rekening") Long rekAsal,
			@RequestParam("No Telepon") Long noTelpon) {
		return new DataResponseList<BayarTeleponWrapper>(transaksiNasabahService.bayarTelpon(rekAsal, noTelpon));
	}

	@PostMapping(path = "/bayarTelponPerbulan")
	public DataResponseList<BayarTeleponWrapper> bayarTelponPerbulan(@RequestParam("Nomor Rekening") Long rekAsal,
			@RequestParam("No Telepon") Long noTelpon, @RequestParam("Bulan Tagihan") Byte bulanTagihan) {
		return new DataResponseList<BayarTeleponWrapper>(
				transaksiNasabahService.bayarTelponPerbulan(rekAsal, noTelpon, bulanTagihan));
	}

	@GetMapping(path = "/cekSaldo")
	public DataResponse<MasterBankWrapper> cekSaldo(@RequestParam("Nomor Rekening") Long norek) {
		return new DataResponse<MasterBankWrapper>(transaksiNasabahService.cekSaldo(norek));
	}

	@PostMapping(path = "/setor")
	public DataResponse<SetorAmbilWrapper> setor(@RequestParam("Nomor Rekening") Long norek,
			@RequestParam("Nominal") Long nominal) {
		return new DataResponse<SetorAmbilWrapper>(transaksiNasabahService.setor(norek, nominal));
	}

	@PostMapping(path = "/tarik")
	public DataResponse<SetorAmbilWrapper> tarik(@RequestParam("Nomor Rekening") Long norek,
			@RequestParam("Nominal") Long nominal) {
		return new DataResponse<SetorAmbilWrapper>(transaksiNasabahService.tarik(norek, nominal));
	}
	
//	=========================================GetDataTransaksi===================================================

	@GetMapping(path = "/findByNoRekAndNoTelp")
	public DataResponseList<BayarTeleponWrapper> findByNoTelp(@RequestParam("Nomor Rekening") Long rekAsal,
			@RequestParam("No Telepon") Long noTelp) {
		return new DataResponseList<BayarTeleponWrapper>(transaksiNasabahService.findByNoTelpon(rekAsal, noTelp));
	}

	@GetMapping(path = "/findTotalTagihan")
	public DataResponseList<BayarTeleponWrapper> findTotalTagihan(@RequestParam("Nomor Rekening") Long rekAsal,
			@RequestParam("No Telepon") Long noTelp) {
		return new DataResponseList<BayarTeleponWrapper>(transaksiNasabahService.findTotalTagihan(rekAsal, noTelp));
	}
	
//	=======================================ExportToPDF================================================================

	@GetMapping(path = "/exportToPdfSetorParam")
	public void exportToPdfSetor(HttpServletResponse response, @RequestParam("ID History") Long idHistory) throws Exception {
		transaksiNasabahService.ExportToPdfSetor(response, idHistory);
	}

	@GetMapping(path = "/exportToPdfTarikParam")
	public void exportToPdfTarikParam(HttpServletResponse response, @RequestParam("ID History") Long idHistory) throws Exception {
		transaksiNasabahService.ExportToPdfTarik(response, idHistory);
	}

	@GetMapping(path = "/exportToPdfTransferParam")
	public void exportToPdfTransferParam(HttpServletResponse response, @RequestParam("ID History") Long idHistory, @RequestParam("Saldo") Long saldo)
			throws Exception {
		transaksiNasabahService.ExportToPdfTransfer(response, idHistory, saldo);
	}

	@GetMapping(path = "/exportToPdfBayarTeleponParam")
	public void exportToPdfBayarTeleponParam(HttpServletResponse response, @RequestParam("ID History Bank") Long idHistoryBank,
			@RequestParam("ID History Telepon") Long idHistoryTelp) throws Exception {
		transaksiNasabahService.ExportToPdfBayarTelepon(response, idHistoryBank, idHistoryTelp);
	}

	
//	======================================TransaksiValidate=========================================================
	
	@PostMapping(path = "/transferValidate")
	public DataResponse<TransferWrapper> transferValidate(@RequestParam("Nomor Rekening Asal") Long rekAsal,
			@RequestParam("Nomor Rekening Tujuan") Long rekTujuan, @RequestParam("Nominal") Long nominal)
			throws Exception {
		return new DataResponse<TransferWrapper>(transaksiNasabahService.transferValidate(rekTujuan, rekAsal, nominal));
	}
	
	@PostMapping(path = "/tarikValidate")
	public DataResponse<SetorAmbilWrapper> tarikValidate(@RequestParam("Nomor Rekening") Long norek,
			@RequestParam("Nominal") Long nominal) throws Exception {
		return new DataResponse<SetorAmbilWrapper>(transaksiNasabahService.tarikValidate(norek, nominal));
	}

	@PostMapping(path = "/bayarTeleponPerbulanValidate")
	public DataResponseList<BayarTeleponWrapper> bayarTeleponValidate(@RequestParam("Nomor Rekening") Long rekAsal,
			@RequestParam("No Telepon") Long noTelpon, @RequestParam("Bulan Tagihan") Byte bulanTagihan)
			throws Exception {
		return new DataResponseList<BayarTeleponWrapper>(
				transaksiNasabahService.bayarTelponValidate(rekAsal, noTelpon, bulanTagihan));
	}

	@PostMapping(path = "/bayarTeleponTotalValidate")
	public DataResponseList<BayarTeleponWrapper> bayarTeleponTotalValidate(@RequestParam("Nomor Rekening") Long rekAsal,
			@RequestParam("No Telepon") Long noTelpon) throws Exception {
		return new DataResponseList<BayarTeleponWrapper>(
				transaksiNasabahService.bayarTelponTotalValidate(rekAsal, noTelpon));
	}

	@PostMapping(path = "/setorValidate")
	public DataResponse<SetorAmbilWrapper> setorValidate(@RequestParam("Nomor Rekening") Long norek,
			@RequestParam("Nominal") Long nominal)
			throws MessagingException, IOException, DocumentException, Exception {
		return new DataResponse<SetorAmbilWrapper>(transaksiNasabahService.sendBuktiSetor(norek, nominal));
	}

}
