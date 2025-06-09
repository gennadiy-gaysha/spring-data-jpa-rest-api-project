package com.gaysha.spring_data_jpa_setup.mapper;

public interface Mapper<A, B> {
    A mapFrom(B b);
    B mapTo(A a);
}
