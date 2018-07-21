package com.sthompson.chisel.io;

import java.io.InputStream;
import java.util.List;

public abstract class MarkdownDataSourceImpl implements MarkdownDataSource {

	
	public MarkdownDataSourceImpl(InputStream inputStream) {
		initialize(inputStream);
	}
	
	public abstract void initialize(InputStream inputStream);

}
