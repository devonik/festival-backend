package nik.dev.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nik.dev.model.FestivalTicketPhase;

public class CompareSetList {
	
	public static
	<T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
	  
	  List<T> list = new ArrayList<T>(c);
	  java.util.Collections.sort(list);
	  return list;
	}

}
