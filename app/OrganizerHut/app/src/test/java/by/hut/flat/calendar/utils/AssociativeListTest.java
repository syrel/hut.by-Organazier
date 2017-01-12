package by.hut.flat.calendar.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class AssociativeListTest {
    @Test
    public void testShouldAdd(){
        AssociativeList<String> alist = new AssociativeList<String>();
        alist.add("Bob", "bob_name");
        alist.add("Rob", "rob_name");
        alist.add("Ann", 1);
        alist.add("Maria", 2);
        System.out.println(alist);
        assertEquals(4,alist.getSize());
        assertEquals("Bob",alist.get("bob_name"));
        assertEquals("Rob",alist.get("rob_name"));
        assertEquals("Ann",alist.get(1));
        assertEquals("Maria",alist.get(2));
    }
}