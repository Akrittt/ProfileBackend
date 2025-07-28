package com.portfolio.entity;

import lombok.Data;
import jdk.jfr.DataAmount;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;


@Data
@Getter
@Setter
@Document(collection = "projects")
public class projectEntity {
    private String id;
    private String title;
    private String description;
    private String github;
    private String live;

    private List<String> tech;
    private List<Stat> stats;

    @Data
    @Getter
    @Setter
    public static class Stat {
        private String label;
        private int value;
        private String suffix;
    }
}
