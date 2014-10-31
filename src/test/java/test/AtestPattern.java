package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AtestPattern {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String log="HU2313";
//		String regString="\\w+\\d+";
		String regString="\\w{6}";
		Pattern p = Pattern.compile(regString);
		Matcher m = p.matcher(log);
		System.out.println(m.matches());
	}

}
