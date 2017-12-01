package com.domo4pi.utils.xml.properties;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class PropertiesAdaptor extends XmlAdapter<XmlProperties, Properties> {

    @Override
    public XmlProperties marshal(Properties properties) throws Exception {
        XmlProperties xmlProperties = new XmlProperties();

        List<String> keys = new ArrayList<String>();
        for (Object key : properties.keySet()) {
            keys.add(key.toString());
        }
        Collections.sort(keys);

        for (String key : keys) {
            xmlProperties.getProperties().add(new XmlProperty(key, properties.getProperty(key)));
        }
        return xmlProperties;
    }

    @Override
    public Properties unmarshal(XmlProperties xmlProperties) throws Exception {
        Properties properties = new Properties();

        for (XmlProperty property : xmlProperties.getProperties()) {
            properties.setProperty(property.getKey(), property.getValue());
        }

        return properties;
    }
}
