package com.sthompson.chisel.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileMarkdownDataSourceimplTests {

	private FileMarkdownDataSource mds;
	
	private InputStream is;
	@Before
	public void setUp() throws Exception {
		InputStream is = new FileInputStream("test.txt");
		mds = new FileMarkdownDataSource(is);
	}

	@After
	public void tearDown() throws Exception {
		if(is != null) {
			is.close();
		}
	}

	@Test
	public void test_readData_returns_lines_as_list() {
		List<String> data = mds.readData();
		assertNotNull(data);
		assertEquals(3, data.size());
		assertEquals("First line", data.get(0));
	}

}
