package com.sthompson.chisel.parsing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TextChunkParserTests {

	private TextChunkParser p;
	
	@Before
	public void setUp() throws Exception {
		p = new TextChunkParser();
	}

	@After
	public void tearDown() throws Exception {
		p = null;
	}

	@Test
	public void test_single_line_wrapped_in_paragraph() {
		List<String> lines = new ArrayList<>();
		
		lines.add("This is the first line of the paragraph.");
		
		List<String> output = p.parseLines(lines);
		assertNotNull(output);
		assertEquals(1, output.size());
		
		assertEquals("<p>This is the first line of the paragraph.</p>", output.get(0));
	}
	
	@Test
	public void test_multi_lines_together_wrapped_in_paragraph() {
		List<String> lines = new ArrayList<>();
		
		lines.add("This is the first line of the paragraph.");
		lines.add("This is the second line of the paragraph.");
		
		List<String> output = p.parseLines(lines);
		assertNotNull(output);
		assertEquals(1, output.size());
		
		assertEquals("<p>This is the first line of the paragraph. This is the second line of the paragraph.</p>", output.get(0));
	}
	
	@Test
	public void test_multi_lines_separated_by_empty_String_wrapped_in_separate_paragraphs() {
		List<String> lines = new ArrayList<>();
		
		lines.add("This is the first line of the paragraph.");
		lines.add("");
		lines.add("This is the second line of the paragraph.");
		
		List<String> output = p.parseLines(lines);
		assertNotNull(output);
		assertEquals(2, output.size());
		
		assertEquals("<p>This is the first line of the paragraph.</p>", output.get(0));
		assertEquals("<p>This is the second line of the paragraph.</p>", output.get(1));
	}
	
	@Test
	public void test_multi_lines_separated_by_null_wrapped_in_separate_paragraphs() {
		List<String> lines = new ArrayList<>();
		
		lines.add("This is the first line of the paragraph.");
		lines.add(null);
		lines.add("This is the second line of the paragraph.");
		lines.add("This is the third line of the paragraph.");
		
		List<String> output = p.parseLines(lines);
		assertNotNull(output);
		assertEquals(2, output.size());
		
		assertEquals("<p>This is the first line of the paragraph.</p>", output.get(0));
		assertEquals("<p>This is the second line of the paragraph. This is the third line of the paragraph.</p>", output.get(1));
	}

}