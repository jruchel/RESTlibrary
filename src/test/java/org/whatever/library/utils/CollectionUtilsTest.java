package org.whatever.library.utils;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollectionUtilsTest {


    CollectionUtils<Integer> sut = new CollectionUtils<>();

    @Test
    void getCommonObjects() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> list2 = Arrays.asList(4, 3);
        List<Integer> list3 = Arrays.asList(4, 3, 2);

        List<Integer> actual = sut.getCommonObjects(list1, list2, list3);
        List<Integer> expected = Arrays.asList(4, 3);

        assertArrayEquals(actual.toArray(), expected.toArray());
    }

    @Test
    void getSumOfObjects() {
    }
}