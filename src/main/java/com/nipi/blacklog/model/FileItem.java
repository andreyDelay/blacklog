package com.nipi.blacklog.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Builder
@Table(name = "files")
public class FileItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "files_sequence")
	private Long id;

	@Column(name = "title")
	private String filename;

	@Column(name = "path")
	private String filepath;

	@Column(name = "size")
	private long size;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private FileStatus fileStatus;
}