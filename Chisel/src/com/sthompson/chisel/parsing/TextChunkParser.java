package com.sthompson.chisel.parsing;

import java.util.ArrayList;
import java.util.List;

public class TextChunkParser implements ChiselParser {

	@Override
	public List<String> parseLines(List<String> input) {
		List<String> lines = new ArrayList<>();
		
		List<String> chunks = groupLinesInChunks(input);
		
//		boolean newParagraph = true;
//		StringBuilder lineBuffer = new StringBuilder(64);
//		for(int i=0; i < input.size(); i++) {
//			// read a line and start a new paragraph
//			// until an empty line is found
//			if(newParagraph) {
//				lineBuffer.append("<p>");
//			}
//			lineBuffer.append(input.get(i));
//			if(i < lines.size() - 1) {
//				String nextLine = input.get(++i);
//				if(nextLine.matches("\\s+")) {
//					lineBuffer.append("</p>");
//					lines.add(lineBuffer.toString());
//					newParagraph = true;
//				}
//				else {
//					lineBuffer.append(nextLine);
//					newParagraph = false;
//				}
//			}
//			else {
//				
//			}
//		}
		
		for(String line : chunks) {
			StringBuilder sb = new StringBuilder("<p>");
			sb.append(line);
			sb.append("</p>");
			lines.add(sb.toString());
		}
		
		return lines;
	}
	
	private List<String> groupLinesInChunks(List<String> lines){
		List<String> chunks = new ArrayList<>();
		
		StringBuilder lineBuff = new StringBuilder();
		boolean newChunk = true;
		for (int i=0; i < lines.size(); i++) {
			String line = lines.get(i);
			if(line != null && !line.matches("\\s+") && !"".equals(line)) {
				if(!newChunk) {
					lineBuff.append(" ");
				}
				lineBuff.append(line);
				newChunk = false;
			}
			else {
				chunks.add(lineBuff.toString());
				// reset
				lineBuff.delete(0, lineBuff.length());
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
