package com.ogya.lokakarya.configuration.telepon;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "column")
@PropertySource("classpath:column/columnLaporanPenunggakan.properties")
public class LaporanPenunggakanConfigurationProperties {
	@Value("#{'${column.column}'.split(',')}")
	private List<String> column;
	public List<String> getColumn() {
		return column;
	}
	public void setColumn(List<String> column) {
		this.column = column;
	}
}
