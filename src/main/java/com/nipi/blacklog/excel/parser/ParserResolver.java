package com.nipi.blacklog.excel.parser;

import com.nipi.blacklog.excel.model.WorkbookType;

public interface ParserResolver {
	ExcelParser resolveParser(WorkbookType workbookType) ;
}
