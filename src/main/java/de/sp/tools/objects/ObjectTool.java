package de.sp.tools.objects;

public class ObjectTool {
	public static boolean equalsOrBothNull(Object o1, Object o2){
		
		if(o1 == null){
			return o2 == null;
		}
		
		return o1.equals(o2);
	}
}
