package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

import java.time.LocalDate;

@Getter
@Setter
public class HeadDto implements Flattenable{

    private String fio;
    private String post;
    private Boolean headMain;
    private String docName;
    private String docNum;
    private LocalDate docDate;

}
