package com.sthompson.chisel.parsing;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InlineParser implements ChiselParser {
	
	private Map<String, String> regexMatchRulesAndCaptures;
	
	{
		// Using a linked hash map because order of keys matters in the case of 
		// strong and em
		regexMatchRulesAndCaptures = new LinkedHashMap<>();
		// lazy quantifier on the text to get as little as possible
		regexMatchRulesAndCaptures.put("(\\*\\*)(.*?)(\\*\\*)", "<strong>$2</strong>");	//<strong>
		regexMatchRulesAndCaptures.put("(\\*)(.*?)(\\*)", "<em>$2</em>");				//<em>
	}

	@Override
	public List<String> parseLines(List<String> input) {
		List<String> output = new ArrayList<>();
		
		// This goes over each rule for one line, but it should probably do something recursive
		Set<String> ruleKeys = regexMatchRulesAndCaptures.keySet();
		for(int i = 0; i < input.size(); i++) {
			String line = input.get(i);
			for (String rule : ruleKeys) {
				Pattern pattern = Pattern.compile(rule);
				Matcher m = pattern.matcher("");
				m.reset(line);
				// found a match in this line
				while(m.find()) {
					line = handleSubstitution(line, m, regexMatchRulesAndCaptures.get(rule)); 
				}
			}
			output.add(line);
		}
		
		
		return output;
	}
	
	private String handleSubstitution(String line, Matcher m, String rep) {
		line = m.replaceFirst(rep);
		return line;
	}

}
