package com.sthompson.chisel.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sthompson.chisel.parsing.TwoPassGroupingParser;

public class TwoPassGroupingParserTests {
	
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
	public void test_star_wrapped_in_em_tags() {
		String input = "My *emphasized text* is awesome";
		
		List<String> lines = new ArrayList<>();
		lines.add(input);
		
		List<String> output = p.parseLines(lines);
		
		assertNotNull(output);
		assertEquals(1, output.size());
		
		assertEquals("My <em>emphasized text</em> is awesome", output.get(0));
		
	}
	
	@Test
	public void test_starstar_wrapped_in_strong_tags() {
		String input = "My **strong text** is awesome";
		
		List<String> lines = new ArrayList<>();
		lines.add(input);
		
		List<String> output = p.parseLines(lines);
		
		assertNotNull(output);
		assertEquals(1, output.size());
		
		assertEquals("My <strong>strong text</strong> is awesome", output.get(0));
		
	}
	
	@Test
	public void test_starstar_and_star_wrapped_in_strong_and_em_tags() {
		String input = "My **strong *emphasized* text** is awesome";
		
		List<String> lines = new ArrayList<>();
		lines.add(input);
		
		List<String> output = p.parseLines(lines);
		
		assertNotNull(output);
		assertEquals(1, output.size());
		
		assertEquals("My <strong>strong <em>emphasized</em> text</strong> is awesome", output.get(0));
		
	}

}
