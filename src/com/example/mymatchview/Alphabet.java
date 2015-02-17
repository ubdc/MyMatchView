package com.example.mymatchview;

import java.util.ArrayList;
import java.util.List;

import com.example.mymatchview.MatchView.Line;

public class Alphabet {
	
	public static List<Line> match(char c) {
		List<Line> lines = new ArrayList<Line>();
		switch (c) {
		case 'W':
			lines.add(new Line(0, 0, 0, 45));
			lines.add(new Line(0, 45, 15, 15));
			lines.add(new Line(15, 15, 30, 45));
			lines.add(new Line(30, 45, 30, 0));
			break;
		case 'T':
			lines.add(new Line(0, 0, 30, 0));
			lines.add(new Line(15, 0, 15, 45));
			break;
		case 'U':
			lines.add(new Line(0, 0, 0, 45));
			lines.add(new Line(0, 45, 30, 45));
			lines.add(new Line(30, 45, 30, 0));
			break;
		case 'J':
			lines.add(new Line(30, 0, 30, 45));
			lines.add(new Line(30, 45, 15, 45));
			lines.add(new Line(15, 45, 0, 25));
			break;
		case 'I':
			lines.add(new Line(0, 0, 30, 0));
			lines.add(new Line(15, 0, 15, 45));
			lines.add(new Line(0, 45, 30, 45));
			break;
		case 'A':
			lines.add(new Line(0, 45, 15, 0));
			lines.add(new Line(15, 0, 30, 45));
			lines.add(new Line(4, 26, 26, 26));
			break;
		case '.':
			lines.add(new Line(14, 45, 16, 45));
			lines.add(new Line(14, 44, 16, 44));
			break;
		case 'C':
			lines.add(new Line(0, 0, 30, 0));
			lines.add(new Line(0, 0, 0, 45));
			lines.add(new Line(0, 45, 30, 45));
			break;
		case 'O':
			lines.add(new Line(0, 0, 30, 0));
			lines.add(new Line(0, 0, 0, 45));
			lines.add(new Line(0, 45, 30, 45));
			lines.add(new Line(30, 0, 30, 45));
			break;
		case 'M':
			lines.add(new Line(0, 0, 0, 45));
			lines.add(new Line(0, 0, 15, 30));
			lines.add(new Line(15, 30, 30, 0));
			lines.add(new Line(30, 0, 30, 45));
			break;
		case 'L':
			lines.add(new Line(0, 0, 0, 45));
			lines.add(new Line(0, 45, 30, 45));
			break;
		case 'D':
			lines.add(new Line(0, 0, 20, 0));
			lines.add(new Line(0, 0, 0, 45));
			lines.add(new Line(0, 45, 20, 45));
			lines.add(new Line(20, 45, 30, 30));
			lines.add(new Line(30, 30, 30, 15));
			lines.add(new Line(30, 15, 20, 0));
			break;
		case 'N':
			lines.add(new Line(0, 0, 0, 45));
			lines.add(new Line(0, 0, 30, 45));
			lines.add(new Line(30, 0, 30, 45));
			break;
		case 'G':
			lines.add(new Line(30, 0, 30, 10));
			lines.add(new Line(0, 0, 30, 0));
			lines.add(new Line(0, 0, 0, 45));
			lines.add(new Line(0, 45, 30, 45));
			lines.add(new Line(30, 45, 30, 35));
			lines.add(new Line(30, 35, 20, 35));
			break;
		case 'E':
			lines.add(new Line(0, 0, 0, 45));
			lines.add(new Line(0, 0, 30, 0));
			lines.add(new Line(0, 22, 30, 22));
			lines.add(new Line(0, 45, 30, 45));
			break;
		}
		return lines;
	}
	
	public static List<Line> match(String s) {
		List<Line> lines = null;
		for (int i = 0; i < s.length(); i++) {
			if (lines == null) {
				lines = match(s.charAt(i));
			} else {
				List<Line> match = match(s.charAt(i));
				for (Line line : match) {
					line.offset(40 * i, 0);
				}
				lines.addAll(match);
			}
		}
		return lines;
	}
}
