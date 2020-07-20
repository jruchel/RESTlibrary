package org.whatever.library.utils;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CollectionUtils<E> {


    private List<E> getCommonObjects(List<List<E>> lists) {
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

    /**
     * A method that takes in a bunch of lists and finds common objects among them, if a list is null, it's treated as if it wasn't there
     *
     * @param lists Lists to process
     * @return a lists of common objects
     */
    public List<E> getCommonObjects(List<E>... lists) {
        List<List<E>> asList = Arrays.stream(lists).collect(Collectors.toList());
        return getCommonObjects(asList);
    }

    private List<E> getCommonOfTwoLists(List<E> list1, List<E> list2) {
        //Only considering non null lists
        if (list1 == null) return list2;
        if (list2 == null) return list1;
        List<E> result = new ArrayList<>();
        List<E> sum = getSumOfObjects(list1, list2);
        for (E object : sum) {
            if (list1.contains(object) && list2.contains(object)) result.add(object);
        }
        return result;
    }

    public List<E> getSumOfObjects(List<E>... lists) {
        Set<E> result = new HashSet<>();

        for (List<E> list : lists) {
            result.addAll(list);
        }
        return new ArrayList<>(result);
    }
}


