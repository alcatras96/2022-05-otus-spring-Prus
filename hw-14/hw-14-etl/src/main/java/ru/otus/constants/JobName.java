package ru.otus.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobName {

    RELATIONAL_TO_NON_RELATIONAL_JOB("relationalToNonRelationalDatabaseEtlJob");

    private final String name;
}
