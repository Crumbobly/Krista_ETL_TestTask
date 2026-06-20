package ru.lab.dto.inner;

import lombok.Getter;
import lombok.Setter;
import ru.lab.dto.FlattenerDto;

@Getter
@Setter
public class ContactDto extends FlattenerDto {

    private String phone;
    private String site;
    private String mail;


}
