package com.ogya.lokakarya.bankadm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ogya.lokakarya.bankadm.service.TransaksiNasabahService;
import com.ogya.lokakarya.bankadm.wrapper.MasterBankWrapper;
import com.ogya.lokakarya.bankadm.wrapper.SetorAmbilWrapper;
import com.ogya.lokakarya.bankadm.wrapper.TransferWrapper;
import com.ogya.lokakarya.telepon.wrapper.BayarTeleponWrapper;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponseList;

@RestController
@RequestMapping(value = "/transaksiNasabah")
@CrossOrigin
public class TransaksiNasabahController {
	@Autowired
	TransaksiNasabahService transaksiNasabahService;

	@PostMapping(path = "/transfer")
	public DataResponse<TransferWrapper> transfer(@RequestParam("Nomor Rekening Asal") Long rekAsal,
												  @RequestParam("Nomor Rekening Tujuan") Long rekTujuan, @RequestParam("Nominal") Long nominal) {
		return new DataResponse<TransferWrapper>(transaksiNasabahService.transfer(rekTujuan, rekAsal, nominal));
	}

	@PostMapping(path = "/bayarTelpon")
	public DataResponseList<BayarTeleponWrapper> bayarTelpon(@RequestParam("Nomor Rekening") Long rekAsal, @RequestParam("No Telepon") Long noTelpon) {
		return new DataResponseList<BayarTeleponWrapper>(transaksiNasabahService.bayarTelpon(rekAsal, noTelpon));
	}
	
	@PostMapping(path = "/bayarTelponPerbulan")
	public DataResponseList<BayarTeleponWrapper> bayarTelponPerbulan(@RequestParam("Nomor Rekening") Long rekAsal, @RequestParam("No Telepon") Long noTelpon,
			@RequestParam("Bulan Tagihan") Byte bulanTagihan) {
		return new DataResponseList<BayarTeleponWrapper>(transaksiNasabahService.bayarTelponPerbulan(rekAsal, noTelpon, bulanTagihan));
	}

	@GetMapping(path = "/cekSaldo")
	public DataResponse<MasterBankWrapper> cekSaldo(@RequestParam("Nomor Rekening") Long norek) {
		return new DataResponse<MasterBankWrapper>(transaksiNasabahService.cekSaldo(norek));
	}

	@PostMapping(path = "/setor")
	public DataResponse<SetorAmbilWrapper> setor(@RequestParam("Nomor Rekening") Long norek, @RequestParam("Nominal") Long nominal) {
		return new DataResponse<SetorAmbilWrapper>(transaksiNasabahService.setor(norek, nominal));
	}

	@PostMapping(path = "/tarik")
	public DataResponse<SetorAmbilWrapper> tarik(@RequestParam("Nomor Rekening") Long norek, @RequestParam("Nominal") Long nominal) {
		return new DataResponse<SetorAmbilWrapper>(transaksiNasabahService.tarik(norek, nominal));
	}

	@GetMapping(path = "/findByNoRekAndNoTelp")
	public DataResponseList<BayarTeleponWrapper> findByNoTelp(@RequestParam("Nomor Rekening") Long rekAsal, @RequestParam("No Telepon") Long noTelp) {
		return new DataResponseList<BayarTeleponWrapper>(transaksiNasabahService.findByNoTelpon(rekAsal, noTelp));
	}
}
