package com.rabelo.libook_info.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Language {
    EN("english"),
    ES("spanish"),
    FR("french"),
    IT("italian"),
    PT("portuguese"),
    OTHER("other language");

    private String language;

    public static void printLanguages() {
        for (Language language : Language.values()) {
            System.out.println(language + " -> " + language.getLanguage());
        }
    }
}
