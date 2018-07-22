package com.sthompson.chisel.parsing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InlineParserTests {

	private TwoPassGroupingParser p;
	
	@Before
	public void setUp() throws Exception {
		p = new TwoPassGroupingParser();
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
	public void test_multi_lines_separated_by_spaces_wrapped_in_separate_paragraphs() {
		List<String> lines = new ArrayList<>();
		
		lines.add("This is the first line of the paragraph.");
		lines.add(" \t");
		lines.add("This is the second line of the paragraph.");
		lines.add("This is the third line of the paragraph.");
		
		List<String> output = p.parseLines(lines);
		assertNotNull(output);
		assertEquals(2, output.size());
		
		assertEquals("<p>This is the first line of the paragraph.</p>", output.get(0));
		assertEquals("<p>This is the second line of the paragraph. This is the third line of the paragraph.</p>", output.get(1));
	}

	@Test
	public void test_star_around_text_surrounds_with_em() {
		List<String> lines = new ArrayList<>();
		lines.add("This is *styled styled* text");
		
		List<String> output = p.parseLines(lines);
		assertNotNull(output);
		
		assertEquals(1, output.size());
		assertEquals("<p>This is <em>styled styled</em> text</p>", output.get(0));
	}
	
	@Test
	public void test_starstar_around_text_surrounds_with_strong() {
		List<String> lines = new ArrayList<>();
		lines.add("This is **styled styled** text");
		
		List<String> output = p.parseLines(lines);
		assertNotNull(output);
		
		assertEquals(1, output.size());
		assertEquals("<p>This is <strong>styled styled</strong> text</p>", output.get(0));
	}
	
	@Test
	public void test_starstar_with_star_around_text_surrounds_with_strong_and_em() {
		List<String> lines = new ArrayList<>();
		lines.add("This is **styled and whatever *empha emph*  and styled** text");
		
		List<String> output = p.parseLines(lines);
		assertNotNull(output);
		
		assertEquals(1, output.size());
		assertEquals("<p>This is <strong>styled and whatever <em>empha emph</em>  and styled</strong> text</p>", output.get(0));
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
	public void test_bulleted_list_returns_unordered_list() {
		List<String> lines = new ArrayList<>();
		
		lines.add("* item 1");
		lines.add("* item 2");
		lines.add("* item 3");
		
		List<String> output = p.parseLines(lines);
		
		assertEquals(1, output.size());
		assertEquals("<ul><li>item 1</li><li>item 2</li><li>item 3</li></ul>", output.get(0));
	}
	
	@Test
	public void test_bulleted_lists_returns_unordered_lists() {
		List<String> lines = new ArrayList<>();
		
		lines.add("* item 1");
		lines.add("* item 2");
		lines.add("* item 3");
		lines.add("");
		lines.add("* list 2");
		
		List<String> output = p.parseLines(lines);
		
		assertEquals(2, output.size());
		assertEquals("<ul><li>item 1</li><li>item 2</li><li>item 3</li></ul>", output.get(0));
		assertEquals("<ul><li>list 2</li></ul>", output.get(1));
	}
	
	@Test
	public void test_bulleted_list_returns_ordered_list() {
		List<String> lines = new ArrayList<>();
		
		lines.add("1. item 1");
		lines.add("3. item 2");
		lines.add("9. item 3");
		
		List<String> output = p.parseLines(lines);
		
		assertEquals(1, output.size());
		assertEquals("<ol><li>item 1</li><li>item 2</li><li>item 3</li></ol>", output.get(0));
	}
	
	@Test
	public void test_bulleted_lists_returns_ordered_lists() {
		List<String> lines = new ArrayList<>();
		
		lines.add("1. item 1");
		lines.add("1. item 2");
		lines.add("1. item 3");
		lines.add("");
		lines.add("0. list 2");
		
		List<String> output = p.parseLines(lines);
		
		assertEquals(2, output.size());
		assertEquals("<ol><li>item 1</li><li>item 2</li><li>item 3</li></ol>", output.get(0));
		assertEquals("<ol><li>list 2</li></ol>", output.get(1));
	}
	
	@Test
	public void test_bulleted_list_returns_ordered_list_with_styling() {
		
		List<String> lines = new ArrayList<>();
		lines.add("1. **item 1**");
		lines.add("1. **item *2* here**");
		lines.add("1. item 3");
		lines.add("");
		lines.add("0. list 2");
		
		List<String> output = p.parseLines(lines);
		
		assertEquals(2, output.size());
		assertEquals("<ol><li><strong>item 1</strong></li><li><strong>item <em>2</em> here</strong></li><li>item 3</li></ol>", output.get(0));
		assertEquals("<ol><li>list 2</li></ol>", output.get(1));
		
	}
}
