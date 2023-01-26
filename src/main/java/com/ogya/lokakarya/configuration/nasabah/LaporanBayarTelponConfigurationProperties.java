package com.ogya.lokakarya.configuration.nasabah;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "telpon")
@PropertySource("classpath:column/columnBayarTelpon.properties")
public class LaporanBayarTelponConfigurationProperties {

	@Value("#{'${telpon.telpon}'.split(',')}")
	private List<String> telpon;

	public List<String> getTelpon() {
		return telpon;
	}

	public void setTelpon(List<String> telpon) {
		this.telpon = telpon;
	}
	
}
