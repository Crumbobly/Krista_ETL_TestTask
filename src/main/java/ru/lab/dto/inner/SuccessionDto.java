package ru.lab.dto.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class SuccessionDto extends FlattenerDto {

    private String parentName;
    private String parentCode;
    private String ogrn;
    private String docName;
    private String numberDoc;
    private LocalDateTime documentDate;
    private String dataSource;


}
