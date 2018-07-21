package com.sthompson.chisel.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileMarkdownDataSource extends MarkdownDataSourceImpl {

	private List<String> data;
	
	public FileMarkdownDataSource(InputStream inputStream) {
		super(inputStream);
		
	}

	@Override
	public void initialize(InputStream inputStream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
		data = new ArrayList<>();
		
		String line = null;
		
		try {
			while( (line = reader.readLine()) != null) {
				data.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public List<String> readData() {
		// defensive copy
		List<String> output = new ArrayList<>(data);
		return output;
	}

}
