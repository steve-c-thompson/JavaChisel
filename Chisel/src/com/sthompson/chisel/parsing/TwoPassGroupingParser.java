package com.sthompson.chisel.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwoPassGroupingParser implements ChiselParser {
	
	private Map<String, String> regexMatchRulesAndCaptures;
	List<List<String>> groupingRules;
	List<List<String>> inlineRules;
	
	private Matcher headerMatcher;
	private Matcher olMatcher;
	private Matcher ulMatcher;
	private Matcher lineBreakMatcher;
	
	{
		groupingRules = new ArrayList<>();
		inlineRules = new ArrayList<>();
		
		groupingRules.add(Arrays.asList("(#+)\\s(.*)", "$2"));								// headers
		groupingRules.add(Arrays.asList("^[0-9]\\.\\s(.*)", "$1"));							// ol
		groupingRules.add(Arrays.asList("^\\*\\s(.*)", "$1"));								// ul
		groupingRules.add(Arrays.asList("^\\s*$", "$1"));									// line break
		
		inlineRules.add(Arrays.asList("(\\*\\*)(.*?)(\\*\\*)", "<strong>$2</strong>"));	// strong					//headers
		inlineRules.add(Arrays.asList("(\\*)(.*?)(\\*)", "<em>$2</em>"));			// em					//headers
		
		// Using a linked hash map because order of keys matters in the case of 
		// strong and em
		regexMatchRulesAndCaptures = new LinkedHashMap<>();
		// lazy quantifier on the text to get as little as possible
		regexMatchRulesAndCaptures.put("(\\*\\*)(.*?)(\\*\\*)", "<strong>$2</strong>");	//<strong>
		regexMatchRulesAndCaptures.put("(\\*)(.*?)(\\*)", "<em>$2</em>");				//<em>
		
		String headerRule = groupingRules.get(0).get(0);
		Pattern headerPattern = Pattern.compile(headerRule);
		headerMatcher = headerPattern.matcher("");
		
		String olRule = groupingRules.get(1).get(0);
		Pattern olPattern = Pattern.compile(olRule);
		olMatcher = olPattern.matcher("");
		
		String ulRule = groupingRules.get(2).get(0);
		Pattern ulPattern = Pattern.compile(ulRule);
		ulMatcher = ulPattern.matcher("");
		
		String lineBreakRule = groupingRules.get(3).get(0);
		Pattern lineBreakPattern = Pattern.compile(lineBreakRule);
		lineBreakMatcher = lineBreakPattern.matcher("");
	}

	@Override
	public List<String> parseLines(List<String> input) {
		return parseFromList(input);
	}
		
	private List<String> parseFromList(List<String> input) {
		input = groupLines(input);
		
		List<String> output = new ArrayList<>();
		for(int i = 0; i < input.size(); i++) {
			String line = input.get(i);
			line = parseLine(line);
			output.add(line);
		
		}
		return output;
	}
	
	private List<String> groupLines(List<String> input) {
		List<String> lines = new ArrayList<>();
		
		StringBuilder group = new StringBuilder();
		for(int i=0; i < input.size(); i++) {
			String line = input.get(i);
			headerMatcher.reset(line);
			olMatcher.reset(line);
			ulMatcher.reset(line);
			lineBreakMatcher.reset(line);
			if(headerMatcher.matches()) {
				lines.add(line);
				continue;
			}
			else if(olMatcher.matches()) {
				// add this line plus \n
				group.append(line).append("\n");
			}
			else if(ulMatcher.matches()) {
				// add this line plus \n
				group.append(line).append("\n");
			}
			else if(lineBreakMatcher.matches()) {
				lines.add(group.toString());
				group.setLength(0);
				//lines.add(line);  leads to extra whitespace in paragraphs
			}
			else {
				if(group.length() > 0) {
					group.append("\n");
				}
				group.append(line);
			}
		}
		if(group.length() > 0) {
			lines.add(group.toString());
		}
		
		return lines;
	}
	
	private String parseLine(String line) {	
		headerMatcher.reset(line);
		olMatcher.reset(line);
		ulMatcher.reset(line);
//		olMatcherMulti.reset(line);
//		ulMatcherMulti.reset(line);
		lineBreakMatcher.reset(line);
		if(headerMatcher.find()) {
			line = header(line, headerMatcher, groupingRules.get(0).get(1));
			line = parseInline(line);
		}
		else {
			// handle groups by splitting on \n 
			String[] lineArr = line.split("\n");
			if(olMatcher.find()) {
				StringBuilder lineBuilder = new StringBuilder("<ol>");
				String rep = groupingRules.get(1).get(1);
				listItems(lineArr, lineBuilder, olMatcher, rep);
				lineBuilder.append("</ol>");
				line = lineBuilder.toString();
			}
			else if(ulMatcher.find()) {
				StringBuilder lineBuilder = new StringBuilder("<ul>");
				String rep = groupingRules.get(2).get(1);
				listItems(lineArr, lineBuilder, ulMatcher, rep);
				lineBuilder.append("</ul>");
				line = lineBuilder.toString();
			}
			else if(lineBreakMatcher.matches()) {
				line = lineBreaks(lineArr);
			}
			else {
				line = para(lineArr);
			}
		}
		return line;
	}
	
	private String parseInline(String line) {
		// other rules
		for(int j = 0; j < inlineRules.size(); j++) {
			String rule = inlineRules.get(j).get(0);
			String rep = inlineRules.get(j).get(1);
			Pattern pattern = Pattern.compile(rule);
			Matcher m = pattern.matcher(line);
			// found a match in this line
			while(m.find()) {
				line = handleSubstitution(line, m, rep); 
			}
		}
		return line;
	}
	
	
	private String header(String line, Matcher m, String rep) {
		String h = m.group(1);
		// TODO: this should not count past 6
		StringBuilder newLine = new StringBuilder("<h");
		newLine.append(h.length()).append(">");
		newLine.append(m.group(2));
		newLine.append("</h").append(h.length()).append(">");
		return newLine.toString();
	}
	
	private String para(String[] lineArr) {
		StringBuilder sb = new StringBuilder("<p>");
		
		for (int i = 0; i < lineArr.length; i++) {
			String line = lineArr[i];
			sb.append(parseInline(line));
			if(i != lineArr.length - 1) {
				sb.append(" ");
			}
			
		}
		sb.append("</p>");
		
		return sb.toString();
	}
	
	
	private void listItems(String[] lineArr, StringBuilder lineBuilder,
			Matcher m, String rep) {
		for (String line : lineArr) {
			m.reset(line);
			line = m.replaceFirst(rep);
			lineBuilder.append("<li>");
			lineBuilder.append(parseInline(line));
			lineBuilder.append("</li>");
		}
	}
	
	private String lineBreaks(String[] lineArr) {
		StringBuilder sb = new StringBuilder();
		for (String string : lineArr) {
			sb.append(string);
		}
		return sb.toString();
	}
	
	private String handleSubstitution(String line, Matcher m, String rep) {
		line = m.replaceFirst(rep);
		return line;
	}

}
