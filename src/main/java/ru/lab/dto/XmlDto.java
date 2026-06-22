package ru.lab.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import ru.lab.dto.response.EBudgetResponseDto;

import java.util.List;

@JacksonXmlRootElement(localName = "response")
public class XmlDto {

    public Meta meta;

    @JacksonXmlElementWrapper(localName = "data")
    @JacksonXmlProperty(localName = "object")
    public List<EBudgetResponseDto> data;

    public static class Meta {
        public int page;
        public String from;
        public String to;
    }
}
