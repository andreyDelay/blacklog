package com.nipi.blacklog.excel;

public enum WorkbookType {
	BASE_TP("BASE_TP");

	public final String workbookType;

	private WorkbookType(String workbookType) {
		this.workbookType = workbookType;
	}

	public String getWorkbookType() {
		return this.workbookType;
	}
}
