package com.ogya.lokakarya.configuration.nasabah;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "")
@PropertySource("classpath:column/columnSetor.properties")
public class LaporanSetorConfigurationProperties {
	@Value("#{'${column}'.split(',')}")
	private List<String> column;
	public List<String> getColumn() {
		return column;
	}
	public void setColumn(List<String> column) {
		this.column = column;
	}
}
