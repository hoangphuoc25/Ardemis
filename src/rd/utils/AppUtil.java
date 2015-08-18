package rd.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppUtil {
	public static String getStatus(String source) {
		List<String> newQualifier = new ArrayList<String>(Arrays.asList("name card", "advertisement"));
		List<String> preQualifiedQualifier = new ArrayList<String>(Arrays.asList("conference", "trade show", "web", "personal contact"));
		String status = "";
		if (newQualifier.contains(source.toLowerCase())) {
			status = "New";
		} else if (preQualifiedQualifier.contains(source.toLowerCase())) {
			status = "Pre-qualified";
		}
		return status;
	}
}
