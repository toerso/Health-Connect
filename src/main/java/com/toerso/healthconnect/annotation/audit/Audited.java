package com.toerso.healthconnect.annotation.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audited {

    // What operation is being performed
    String action() default "";

    // What type of entity is being affected — e.g. "Patient", "User"
    // This tells the audit log WHICH table/domain was touched
    String targetType() default "";

    // Which argument index holds the target entity's ID
    // e.g. if your method is deletePatient(Long id), the ID is at index 0
    // Default -1 means "no ID to extract"
    int targetIdArgIndex() default -1;
}
