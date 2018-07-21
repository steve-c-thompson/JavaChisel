package com.sthompson.chisel.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextChunkParser implements ChiselParser {

	@Override
	public List<String> parseLines(List<String> input) {
		List<String> lines = new ArrayList<>();
		
		List<String> chunks = groupLinesInChunks(input);
		
		// Look for # with space, and capture it and all words afterward as two groups
		Pattern pattern = Pattern.compile("([#]+)\\s(.*)");
		Matcher matcher = pattern.matcher("");
		for(String line : chunks) {
			matcher.reset(line);
			String tag;
			if(matcher.find()) {
				String h = matcher.group(1);
				tag = "h";
				tag += h.length();
				// TODO: this should not count past 6
				line = matcher.group(2);
			}
			else {
				tag = "p";
			}
			StringBuilder sb = new StringBuilder("<");
			sb.append(tag).append(">");
			sb.append(line);
			sb.append("</").append(tag).append(">");
			lines.add(sb.toString());
		}
		
		return lines;
	}
	
	private List<String> groupLinesInChunks(List<String> lines){
		List<String> chunks = new ArrayList<>();
		
		StringBuilder lineBuff = new StringBuilder();
		boolean newChunk = true;
		
		// if line starts with # it is a chunk
		for (int i=0; i < lines.size(); i++) {
			String line = lines.get(i);
			if(line != null && line.startsWith("#")) {
				chunks.add(line);
				newChunk = true;
			}
			else if(line != null && !line.matches("\\s+") && !"".equals(line)) {
				if(!newChunk) {
					lineBuff.append(" ");
				}
				lineBuff.append(line);
				newChunk = false;
			}
			else {
				if(lineBuff.length() > 0) {
					chunks.add(lineBuff.toString());
					// reset
					lineBuff.delete(0, lineBuff.length());
				}
				newChunk = true;
			}
		}
		
		// get the last line
		if(lineBuff.length() > 0) {
			chunks.add(lineBuff.toString());
		}
		
		
		return chunks;
	}

}
