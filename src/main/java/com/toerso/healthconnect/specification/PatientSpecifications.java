package com.toerso.healthconnect.specification;

import com.toerso.healthconnect.entity.Patient;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class PatientSpecifications {
    public static Specification<Patient> hasNameLike(String name) {
        return (root, query, cb) -> name == null ? null :
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Patient> hasPhone(String phone) {
        return (root, query, cb) -> phone == null ? null :
                cb.equal(root.get("phone"), phone);
    }
}
