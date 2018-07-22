package com.sthompson.chisel.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import com.sthompson.chisel.io.FileMarkdownDataSource;
import com.sthompson.chisel.io.MarkdownDataSource;
import com.sthompson.chisel.parsing.ChiselParser;
import com.sthompson.chisel.parsing.TwoPassGroupingParser;

public class ChiselFileReaderApp {

	private MarkdownDataSource dataSource;
	private ChiselParser parser;
	
	public ChiselFileReaderApp(MarkdownDataSource markdownDataSource, ChiselParser parser) {
		this.dataSource = markdownDataSource;
		this.parser = parser;
	}
	
	private List<String> parse() {
		List<String> lines = parser.parseLines(dataSource.readData());
		return lines;
	}
	
	private void outputLines(List<String> lines) {
		for (String string : lines) {
			System.out.println(string);
		}
	}
	public static void main(String[] args) {
		String fileName = "markdown.txt";
		
		try {
			FileInputStream fis = new FileInputStream(fileName);
			FileMarkdownDataSource fmds = new FileMarkdownDataSource(fis);
			ChiselParser parser = new TwoPassGroupingParser();
			ChiselFileReaderApp app = new ChiselFileReaderApp(fmds, parser);
			
			app.outputLines(app.parse());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
