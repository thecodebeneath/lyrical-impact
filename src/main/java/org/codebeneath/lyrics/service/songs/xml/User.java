package org.codebeneath.lyrics.service.songs.xml;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    @XmlElement(name = "results")
    private List<Results> results;

}
