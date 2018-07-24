package org.codebeneath.lyrics.service.songs.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Testing User API : RandomAPI
 * https://randomapi.com/api/6de6abfedb24f889e0b5f675edc50deb?fmt=xml&sole
 */
@Getter
@Setter
@ToString
@XmlRootElement(name = "results")
@XmlAccessorType(XmlAccessType.FIELD)
public class Results {

    private String first;
    private String last;
    private String email;
    private String address;
    private String created;
    private String balance;
}
