package ru.lab.dto.response.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.service.flattener.Flattenable;

@Getter
@Setter
public class ContactDto implements Flattenable{

    private String phone;
    private String site;
    private String mail;


}
