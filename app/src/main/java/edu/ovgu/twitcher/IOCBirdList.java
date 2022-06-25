package edu.ovgu.twitcher;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class IOCBirdList extends AppCompatActivity {
    String countryList[] = {"India", "China", "australia", "Portugle", "America", "NewZealand"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ioc_bird_list);
        ListView IOCList = findViewById(R.id.IOCListView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.ioc_list_content, R.id.iocBirdName, countryList);
        IOCList.setAdapter(arrayAdapter);
        try {

            URL url = new URL("https://www.worldbirdnames.org/master_ioc-names_xml.xml");

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory
                    .newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(url
                    .openStream()));
/*
            nodlist = document.getElementsByTagName("Youritem");

            for (int i = 0; i < nodlist.getLength(); i++) {
                Element element = (Element) nodlist.item(i);

                NodeList nodlistid = element.getElementsByTagName("Id");
                Element id = (Element) nodlistid.item(0);


                NodeList nodlistBaslik = element.getElementsByTagName("tagname1");
                Element baslik = (Element) nodlistBaslik.item(0);

                NodeList nodlistDetay = element.getElementsByTagName("tagname2");
                Element detay = (Element) nodlistDetay.item(0);

                NodeList nodlistKaynak = element.getElementsByTagName("tagname3");
                Element lat = (Element) nodlistKaynak.item(0);

                NodeList nodelistMedia = element.getElementsByTagName("tagname4");
                Element longi = (Element) nodelistMedia.item(0);

                NodeList nodelistTur = element.getElementsByTagName("tagname5");
                Element tur = (Element) nodelistTur.item(0);
                // String resimURL =
                // resim.getAttributes().getNamedItem("url").getNodeValue();

                NodeList nodelistMedia1 = element.getElementsByTagName("enclosure");
                Element picture= (Element) nodelistMedia1.item(0);
                String pictureURL = resim.getAttributes().getNamedItem("picturetagname").getNodeValue();

                xmltagname1.add(tagname1.getChildNodes().item(0).getNodeValue());
                xmltagname2.add(tagname2.getChildNodes().item(0).getNodeValue());
                xmltagname3.add(tagname3.getChildNodes().item(0).getNodeValue());
                xmltagname4.add(tagname4.getChildNodes().item(0).getNodeValue());
                xmltagname5.add(tagname5.getChildNodes().item(0).getNodeValue());


            }
*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
