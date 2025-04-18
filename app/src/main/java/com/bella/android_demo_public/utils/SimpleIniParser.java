package com.bella.android_demo_public.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class SimpleIniParser {
    public Map<String, Map<String, String>> parse(InputStream inputStream) throws IOException {
        Map<String, Map<String, String>> ini = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String currentSection = "";

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith(";") || line.isEmpty()) {
                continue; // 跳过注释和空行
            }
            if (line.startsWith("[") && line.endsWith("]")) {
                currentSection = line.substring(1, line.length() - 1);
                ini.put(currentSection, new HashMap<>());
            } else {
                int equalsIndex = line.indexOf('=');
                if (equalsIndex > 0) {
                    String key = line.substring(0, equalsIndex).trim();
                    String value = line.substring(equalsIndex + 1).trim();
                    if (!currentSection.isEmpty()) {
                        ini.get(currentSection).put(key, value);
                    }
                }
            }
        }
        return ini;
    }
}
