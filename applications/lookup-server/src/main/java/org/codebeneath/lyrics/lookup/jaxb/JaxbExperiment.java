package org.codebeneath.lyrics.lookup.jaxb;

import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.codebeneath.lyrics.lookup.songs.chartlyrics.ArrayOfSearchLyricResult;

/**
 *
 * @author black
 */
public class JaxbExperiment {

    public static void main(String[] args) {
        JaxbExperiment je = new JaxbExperiment();
        je.jaxbExperiment();
    }
    
    private void jaxbExperiment() {
        // works because no namespace in xml document root element
//        Response salesTax = restTemplate.getForObject("https://data.mo.gov/api/views/vpge-tj3s/rows.xml?accessType=DOWNLOAD", Response.class);
//        log.info("  ====== salesTax as Obj: {}", salesTax.getRow().getRows().get(0));

        // works with "namespace" removed from @XmlRootElement
        String xmlStringWorks = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<ArrayOfSearchLyricResult>"
                + "    <SearchLyricResult>"
                + "        <Artist>101</Artist>"
                + "        <Song>IT</Song>"
                + "    </SearchLyricResult>"
                + "</ArrayOfSearchLyricResult>";

        // fails with @XmlRootElement(..., namespace="http://api.chartlyrics.com/")
        String xmlStringFails = "<ArrayOfSearchLyricResult xmlns=\"http://api.chartlyrics.com/\">"
                + "    <SearchLyricResult>"
                + "        <Artist>101</Artist>"
                + "        <Song>IT</Song>"
                + "    </SearchLyricResult>"
                + "</ArrayOfSearchLyricResult>";

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(ArrayOfSearchLyricResult.class);

// this block strips the xmlns="" from the doc and then it works...
//            XMLInputFactory xif = XMLInputFactory.newFactory();
//            xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false); // this is the magic line
//            StreamSource source = new StreamSource(new StringReader(xmlStringFails));
//            XMLStreamReader xsr = xif.createXMLStreamReader(source);
//            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//            ArrayOfSearchLyricResult result = (ArrayOfSearchLyricResult) unmarshaller.unmarshal(xsr);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ArrayOfSearchLyricResult result = (ArrayOfSearchLyricResult) jaxbUnmarshaller.unmarshal(new StringReader(xmlStringWorks));
            System.out.println(result);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
