package com.grayraccoon.sample.batchpractice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class People {
    private long id;
    private String first_name;
    private String last_name;
    private String email;
}
