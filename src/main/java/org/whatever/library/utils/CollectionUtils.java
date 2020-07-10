package org.whatever.library.utils;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CollectionUtils<E> {


    public List<E> getCommonObjects(List<List<E>> lists) {
        //If there is only one list return it
        if (lists.size() == 1) return lists.get(0);

        //If any of the lists is empty return an empty list
        for (List<E> list : lists) {
            if (list == null) continue;
            if (list.size() == 0) return new ArrayList<>();
        }

        List<List<E>> listsCondensed = new ArrayList<>();

        for (int i = 0; i < lists.size(); i += 2) {
            try {
                listsCondensed.add(getCommonOfTwoLists(lists.get(i), lists.get(i + 1)));
            } catch (IndexOutOfBoundsException ex) {
                try {
                    listsCondensed.add(lists.get(i));
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        }
        return getCommonObjects(listsCondensed);
    }

    public List<E> getCommonObjects(List<E>... lists) {
        List<List<E>> asList = Arrays.stream(lists).collect(Collectors.toList());
        return getCommonObjects(asList);
    }

    private List<E> getCommonOfTwoLists(List<E> list1, List<E> list2) {
        if (list1 == null) return list2;
        if (list2 == null) return list1;
        List<E> result = new ArrayList<>();
        List<E> sum = getSumOfObjects(list1, list2);
        for (int i = 0; i < sum.size(); i++) {
            E object = sum.get(i);
            if (list1.contains(object) && list2.contains(object)) result.add(object);
        }
        return result;
    }


    public List<E> getSumOfObjects(List<E>... lists) {
        Set<E> result = new HashSet<>();

        for (int i = 0; i < lists.length; i++) {
            result.addAll(lists[i]);
        }
        return new ArrayList<>(result);
    }
}


