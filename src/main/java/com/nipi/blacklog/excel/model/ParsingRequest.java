package com.nipi.blacklog.excel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsingRequest {
	private String filename;
	private String filepath;
	private WorkbookType workbookType;
}
