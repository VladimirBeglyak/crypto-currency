package com.begliak.cryptocurrency.mapper;

public interface Mapper <F,T>{
    T mapFrom(F entity);
}
