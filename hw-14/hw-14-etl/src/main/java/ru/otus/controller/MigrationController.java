package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static ru.otus.constants.JobName.RELATIONAL_TO_NON_RELATIONAL_JOB;


@RestController
@RequiredArgsConstructor
public class MigrationController {

    private final JobOperator jobOperator;

    @PostMapping("/migration")
    public void startMigrationJob() throws Exception {
        jobOperator.startNextInstance(RELATIONAL_TO_NON_RELATIONAL_JOB.getName());
    }
}
