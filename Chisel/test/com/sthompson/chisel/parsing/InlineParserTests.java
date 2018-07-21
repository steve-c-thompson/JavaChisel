package com.sthompson.chisel.parsing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InlineParserTests {

	private InlineParser p;
	
	@Before
	public void setUp() throws Exception {
		p = new InlineParser();
	}

	@After
	public void tearDown() throws Exception {
		p = null;
	}

	@Test
	public void test_star_around_text_surrounds_with_em() {
		List<String> lines = new ArrayList<>();
		lines.add("This is *styled styled* text");
		
		List<String> output = p.parseLines(lines);
		assertNotNull(output);
		
		assertEquals(1, output.size());
		assertEquals("This is <em>styled styled</em> text", output.get(0));
	}
	
	@Test
	public void test_starstar_around_text_surrounds_with_strong() {
		List<String> lines = new ArrayList<>();
		lines.add("This is **styled styled** text");
		
		List<String> output = p.parseLines(lines);
		assertNotNull(output);
		
		assertEquals(1, output.size());
		assertEquals("This is <strong>styled styled</strong> text", output.get(0));
	}
	
	@Test
	public void test_starstar_with_star_around_text_surrounds_with_strong_and_em() {
		List<String> lines = new ArrayList<>();
		lines.add("This is **styled and whatever *empha emph*  and styled** text");
		
		List<String> output = p.parseLines(lines);
		assertNotNull(output);
		
		assertEquals(1, output.size());
		assertEquals("This is <strong>styled and whatever <em>empha emph</em>  and styled</strong> text", output.get(0));
	}

}
