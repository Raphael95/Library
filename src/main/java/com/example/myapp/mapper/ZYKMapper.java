package com.example.myapp.mapper;

import com.example.myapp.domain.ZYK;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ZYKMapper {

    @Select("SELECT * FROM zyk")
    List<ZYK> getAllBooks();
}
