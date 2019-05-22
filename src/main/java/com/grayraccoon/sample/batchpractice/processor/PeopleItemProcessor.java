package com.grayraccoon.sample.batchpractice.processor;

import com.grayraccoon.sample.batchpractice.domain.People;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class PeopleItemProcessor implements ItemProcessor<People, People> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeopleItemProcessor.class);

    @Override
    public People process(People people) throws Exception {

        final People result = people.toBuilder()
                .id(people.getId())
                .first_name(StringUtils.capitalize(people.getFirst_name()))
                .last_name(StringUtils.capitalize(people.getLast_name()))
                .email(people.getEmail())
                .build();

        LOGGER.info("processing from: {}", people);
        LOGGER.info("processing to: {}", result);

        return result;
    }

}
