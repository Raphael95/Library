package com.example.myapp.mapper;

import com.example.myapp.domain.DangerouCargo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface DangerousCargoMapper {
    @Select("SELECT * FROM dangerouscargo")
    List<DangerouCargo> getAll();

    @Select("SELECT * FROM dangerouscargo WHERE dangerousId = #{id}")
    DangerouCargo getOne(Integer id);

    @Insert("INSERT INTO dangerouscargo(dangerousId,cargoName) VALUES(#{dangerousId},#{cargoName})")
    void insertCargo(DangerouCargo cargo);

    @Update("UPDATE dangerouscargo SET cargoName = #{cargoName},safeCardPath= #{safeCardPath} WHERE dangerousId = #{dangerousId}")
    void updateCargo(DangerouCargo cargo);

    @Delete("DELETE FROM dangerouscargo WHERE dangerousId=#{dangerousId}")
    void deleteCargo(Integer id);
}
