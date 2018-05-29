package util.campooproductions.es;

import java.util.ArrayList;
import java.util.List;

public class ArrayEmptyElementsRemover 
{
	public static String[] removeEmptyStrings(String[] array) 
	{
		List<String> list = new ArrayList<String>();

	    for(String s : array) {
	       if(s != null && s.length() > 0) {
	          list.add(s);
	       }
	    }

	    array = list.toArray(new String[list.size()]);
	    return array;
	}
}
