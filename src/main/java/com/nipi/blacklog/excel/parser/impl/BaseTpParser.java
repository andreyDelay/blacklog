package com.nipi.blacklog.excel.parser.impl;

import com.nipi.blacklog.excel.parser.ExcelParser;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component("BASE_TP")
public class BaseTpParser implements ExcelParser {

	@Override
	public void parse(InputStream inputStream) {
		System.out.println("This is an excel parsing stub.");
		System.out.println("Implementation will be presented later.");
	}
}
