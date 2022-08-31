package com.knubisoft;

import com.knubisoft.anno.Data;
import com.knubisoft.anno.Lookup;
import com.knubisoft.entity.Table;
import com.knubisoft.parsingStrategy.ParsingStrategy;
import com.knubisoft.rwsource.impl.FileReadWriteSource;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFParsingStrategy implements ParsingStrategy<FileReadWriteSource> {

    private Class<?> cls;

    public PDFParsingStrategy(Class<?> cls) {
        this.cls = cls;
    }

    @Override
    @SneakyThrows
    public Table parseToTable(FileReadWriteSource content) {
        String buffer = content.getContent();
        if (cls.isAnnotationPresent(Data.class)) {
            List<Object> result = new ArrayList<>();
            Map<String, String> regex = findFieldNameRegex();
            String commonRegex = String.join(" ", regex.values());
            Pattern pattern = Pattern.compile(commonRegex);
            Matcher matcher = pattern.matcher(buffer);
            while (matcher.find())
                result.add(getEntity(regex, matcher));
        }
//            isHeaderMatchFields(buffer, cls);
//            List<Matcher> matchers = new ArrayList<>();
//            for (String regex : getRegexList()) {
//                matchers.add(Pattern.compile(regex).matcher(buffer));
//            }
//            Field[] fields = cls.getDeclaredFields();
//
//            Map<Integer, String> map = new LinkedHashMap<>();
//            for (int i = 0; i < fields.length; i++) {
//                map.put(i, fields[i].getName());
//            }
//
//            Map<Integer, Map<String, String>> result = new LinkedHashMap<>();
//            while (isAvailableMatchings(matchers)) {
//                for (int i = 0; i < fields.length; i++) {
//                    Map<String, String > hashMap = new LinkedHashMap<>();
//                    hashMap.put(fields[i].getName(), matchers.get(i).group());
//                    result.put(i, hashMap);
//                }
//            }
//
//            Table t = new Table(result);


//            List<Type> types = new ArrayList<>();
//            for (Field field : fields) {
//                types.add(field.getType());
//            }
//            Type[] t = new Type[types.size()];


//            for (Field declaredField : declaredFields) {
//                if (declaredField.isAnnotationPresent(Lookup.class)) {
//                    List<String> column = extractByLookup(buffer, declaredField);
//                }
//            }

        return null;
    }

    private Map<String, String> findFieldNameRegex() {
        Map<String, String> res = new LinkedHashMap<>();
        for (Field field : cls.getDeclaredFields()) {
            if(field.isAnnotationPresent(Lookup.class)){
                res.put(field.getName(), field.getAnnotation(Lookup.class).regex());
            }
        }
        return res;
    }

    @SneakyThrows
    private Object getEntity(Map<String, String> regex, Matcher matcher) {
        Object instance = cls.getConstructor().newInstance();
        for (String s : regex.keySet()) {
            Field field = cls.getDeclaredField(s);
            field.setAccessible(true);
            field.set(instance, matcher.group(s).replaceAll("\\h", " "));
        }
        return instance;
    }

    private boolean isAvailableMatchings(List<Matcher> matchers) {
        boolean isFound = true;
        for (Matcher matcher : matchers) {
            if (!matcher.find()) {
                isFound = false;
                break;
            }
        }
        return isFound;
    }

    private List<String> getRegexList() {
        List<String> regexes = new ArrayList<>();
        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(Lookup.class))
                regexes.add(field.getAnnotation(Lookup.class).regex());
        }
        return regexes;
    }

    private boolean isHeaderMatchFields(String buffer, Class<?> cls) {
        Field[] declaredFields = cls.getDeclaredFields();
        StringBuilder regexHeader = new StringBuilder();
        for (int i = 0; i < declaredFields.length; i++) {
            regexHeader.append(declaredFields[i]);
            if (i < declaredFields.length - 1)
                regexHeader.append("\s");
        }
        Pattern pattern = Pattern.compile(regexHeader.toString(), Pattern.CASE_INSENSITIVE);
        return pattern.matcher(buffer).find();
    }

    private List<String> extractByLookup(String buffer, Field declaredField) {
        List<String> matchingStrings = new ArrayList<>();
        String regex = declaredField.getAnnotation(Lookup.class).regex();
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(buffer);
        while (match.find()) {
            matchingStrings.add(match.group());
        }
        return matchingStrings;
    }
}
