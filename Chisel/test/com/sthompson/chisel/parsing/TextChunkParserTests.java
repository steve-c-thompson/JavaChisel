package com.sthompson.chisel.parsing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sthompson.chisel.parsing.TextChunkParser;

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
	
	@Test
	public void test_hX_line_becomes_html() {
		List<String> lines = new ArrayList<>();
		
		lines.add("# Header");
		
		List<String> output = p.parseLines(lines);
		assertNotNull(output);
		assertEquals(1, output.size());
		
		assertEquals("<h1>Header</h1>", output.get(0));
		
		lines.set(0, "## Header");
		
		output = p.parseLines(lines);
		assertNotNull(output);
		assertEquals(1, output.size());
		
		assertEquals("<h2>Header</h2>", output.get(0));
		
		lines.set(0, "### Header");
		
		output = p.parseLines(lines);
		assertNotNull(output);
		assertEquals(1, output.size());
		
		assertEquals("<h3>Header</h3>", output.get(0));

		lines.set(0, "#### Header");
		
		output = p.parseLines(lines);
		assertNotNull(output);
		assertEquals(1, output.size());
		
		assertEquals("<h4>Header</h4>", output.get(0));
		

		lines.set(0, "##### Header");
		
		output = p.parseLines(lines);
		assertNotNull(output);
		assertEquals(1, output.size());
		
		assertEquals("<h5>Header</h5>", output.get(0));
	}
	
	@Test
	public void test_multiple_h2_line_becomes_html() {
		List<String> lines = new ArrayList<>();
		
		lines.add("## Header 1");
		lines.add("## Header 2");
		
		List<String> output = p.parseLines(lines);
		assertNotNull(output);
		assertEquals(2, output.size());
		
		assertEquals("<h2>Header 1</h2>", output.get(0));
		assertEquals("<h2>Header 2</h2>", output.get(1));
	}
	
	@Test
	public void test_h_followed_by_text_lines_becomes_header_with_paragraph() {
		List<String> lines = new ArrayList<>();
		
		lines.add("## Header 1");
		lines.add("This is text 1.");
		lines.add("This is text 2.");
		
		List<String> output = p.parseLines(lines);
		assertNotNull(output);
		assertEquals(2, output.size());
		
		assertEquals("<h2>Header 1</h2>", output.get(0));
		assertEquals("<p>This is text 1. This is text 2.</p>", output.get(1));
	}
	
	@Test
	public void test_h_followed_by_spaces_with_text_line_becomes_header_with_paragraph() {
		List<String> lines = new ArrayList<>();
		
		lines.add("## Header 1");
		lines.add("");
		lines.add("   ");
		lines.add("This is text 1.");
		lines.add("This is text 2.");
		
		List<String> output = p.parseLines(lines);
		assertNotNull(output);
		assertEquals(2, output.size());
		
		assertEquals("<h2>Header 1</h2>", output.get(0));
		assertEquals("<p>This is text 1. This is text 2.</p>", output.get(1));
	}

}
