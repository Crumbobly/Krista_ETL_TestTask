package ru.lab.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import ru.lab.service.flattener.Flattenable;
import ru.lab.service.flattener.FlattenerService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

public class FlattenerServiceTest {

    private final FlattenerService service = new FlattenerService();

    @AllArgsConstructor
    private static class SimpleObject{
        Integer id;
        String name;
    }

    @AllArgsConstructor
    private static class SimpleObjectExtendsFlattenerDto implements Flattenable {
        Integer id;
        String name;
    }

    @AllArgsConstructor
    private static class ObjectWithIterableField implements Flattenable {
        Iterable<Object> iterable;
    }

    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class BigObject {
        Integer id;
        String name;
        Boolean flag;
        LocalDateTime localDateTime;
        Object simpleObject;
    }

    @Test
    void shouldFlatSimpleObject() {
        final Integer id = 1;
        final String name = "test";
        final SimpleObject simpleObject = new SimpleObject(id, name);

        final Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("id", id);
        expected.put("name", name);

        final Map<String, Object> actual = service.flat(simpleObject);

        assertEquals(expected, actual);
    }


    @Test
    void shouldFlatSpecialTypes() {
        final LocalDateTime localDateTime = LocalDateTime.now();
        final Integer id = 1;
        final Boolean flag = true;

        final BigObject bigObject = new BigObject();
        bigObject.setLocalDateTime(localDateTime);
        bigObject.setId(id);
        bigObject.setFlag(flag);

        final Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("localDateTime", localDateTime);
        expected.put("id", id);
        expected.put("flag", flag);

        final Map<String, Object> actual = service.flat(bigObject);

        assertEquals(expected, actual);
        assertInstanceOf(LocalDateTime.class, actual.get("localDateTime"));
        assertInstanceOf(Integer.class, actual.get("id"));
        assertInstanceOf(Boolean.class, actual.get("flag"));
    }

    @Test
    void shouldFlatNullFields() {
        final BigObject bigObject = new BigObject();
        final Map<String, Object> expected = new HashMap<String, Object>();
        final Map<String, Object> actual = service.flat(bigObject);
        assertEquals(expected, actual);
    }

    @Test
    void shouldFlatRecursive_cantFlatWrongClasses() {
        final SimpleObject simpleObject = new SimpleObject(1, "name");

        final BigObject bigObject = new BigObject();
        bigObject.setSimpleObject(simpleObject);

        final Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("simpleObject", simpleObject);

        final Map<String, Object> actual = service.flat(bigObject);
        assertEquals(expected, actual);
    }

    @Test
    void shouldFlatRecursive() {
        final SimpleObjectExtendsFlattenerDto s = new SimpleObjectExtendsFlattenerDto(1, "name");

        final BigObject bigObject = new BigObject();
        bigObject.setSimpleObject(s);

        final Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("simpleObject_id", 1);
        expected.put("simpleObject_name", "name");

        final Map<String, Object> actual = service.flat(bigObject);
        assertEquals(expected, actual);
    }

    @Test
    void shouldFlatSampleCollection(){

        final List<Object> lst = new ArrayList<Object>();
        lst.add("field1");
        lst.add("field2");
        lst.add("field3");

        final ObjectWithIterableField o = new ObjectWithIterableField(lst);

        final Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("iterable", "field1#field2#field3");

        final Map<String, Object> actual = service.flat(o);
        assertEquals(expected, actual);
    }

    @Test
    void shouldFlatObjectCollection(){

        final List<Object> lst = new ArrayList<>();
        lst.add(new SimpleObjectExtendsFlattenerDto(1, "name1"));
        lst.add(new SimpleObjectExtendsFlattenerDto(2, "name2"));
        lst.add(new SimpleObjectExtendsFlattenerDto(3, "name3"));

        final ObjectWithIterableField o = new ObjectWithIterableField(lst);

        final Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("iterable_id", "1#2#3");
        expected.put("iterable_name", "name1#name2#name3");

        final Map<String, Object> actual = service.flat(o);
        assertEquals(expected, actual);
    }

    @Test
    void shouldFlatObjectCollectionWithNull(){

        final List<Object> lst = new ArrayList<>();
        lst.add(new SimpleObjectExtendsFlattenerDto(1, "name1"));
        lst.add(null);
        lst.add(new SimpleObjectExtendsFlattenerDto(3, null));
        lst.add(new SimpleObjectExtendsFlattenerDto(4, "name4"));

        final ObjectWithIterableField o = new ObjectWithIterableField(lst);

        final Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("iterable_id", "1#3#4");
        expected.put("iterable_name", "name1#name4");

        final Map<String, Object> actual = service.flat(o);
        assertEquals(expected, actual);
    }

}
