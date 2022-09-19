package com.nipi.blacklog.excel.parser.impl;

import com.nipi.blacklog.excel.model.WorkbookType;
import com.nipi.blacklog.excel.parser.ExcelParser;
import com.nipi.blacklog.excel.parser.ParserResolver;
import com.nipi.blacklog.excel.exception.NotSupportedParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class ParserResolverImpl implements ParserResolver {

	@Autowired
	Map<String, ExcelParser> excelParserMap;

	@Override
	public ExcelParser resolveParser(WorkbookType workbookType) {
		ExcelParser excelParser = excelParserMap.get(workbookType.getType());
		if (Objects.isNull(excelParser)) {
			throw new NotSupportedParserException(
					String.format("Parser for workbook type - %s, not found. Request cannot be processed.",
							workbookType.getType()));
		}
		return excelParser;
	}
}
