package com.sthompson.chisel.io;

import java.io.InputStream;
import java.util.List;

public interface MarkdownDataSource {
	List<String> readData();
}
