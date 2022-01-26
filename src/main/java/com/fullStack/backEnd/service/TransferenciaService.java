package com.fullStack.backEnd.service;

import org.springframework.stereotype.Service;

@Service
public class TransferenciaService {

	public double calculandoTransferenciaA(double valorTransferido){
		double resultado = valorTransferido + (3 * 5.44);
		return resultado;
	}
	
	public double calculandoTransferenciaB(double valorTransferido) {
		return valorTransferido + (12 * 5.44);
	}
	
	public double calculandoTransferenciaC(double valorTransferido, long dias) {
		if(dias > 10 && dias <= 20) {
			// 8.2%
			return valorTransferido * 1.082;
		} else if(dias > 20 && dias <= 30) {
			// 6.9%
			return valorTransferido * 1.069;	
		} else if(dias > 30 && dias <= 40) {
			// 4.7%
			return valorTransferido * 1.047;
		} else if(dias > 40) {
			// 1.7%
			return valorTransferido * 1.017;
		} else {
			return valorTransferido;
		}
	}
	
}
