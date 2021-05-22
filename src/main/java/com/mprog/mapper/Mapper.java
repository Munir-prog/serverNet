package com.mprog.mapper;

public interface Mapper<T, F> {

    F mapFrom(T object);
    T mapTo(F object);
}
