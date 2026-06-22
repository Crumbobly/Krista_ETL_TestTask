package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

import java.time.LocalDateTime;

@Getter
@Setter
public class SuccessionDto implements Flattenable {

    private String parentName;
    private String parentCode;
    private String ogrn;
    private String docName;
    private String numberDoc;
    private LocalDateTime documentDate;
    private String dataSource;


}
