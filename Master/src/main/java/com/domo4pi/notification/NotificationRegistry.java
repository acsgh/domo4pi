package com.domo4pi.notification;

import com.domo4pi.application.Application;
import com.domo4pi.utils.ExceptionUtils;
import com.domo4pi.utils.xml.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.File;
import java.util.*;

@XmlRootElement(name = "Notifications")
@XmlAccessorType(XmlAccessType.FIELD)
public class NotificationRegistry {

    public static final File configurationFile = new File(Application.getInstance().getBaseDir(), "Notifications.xml");

    @XmlTransient
    private final Logger log = LoggerFactory.getLogger(getClass());

	@XmlJavaTypeAdapter(GroupsMapAdaptor.class)
	@XmlElement(name = "NotificationGroups")
	public Map<String, Set<String>> addressByGroup = new HashMap<String, Set<String>>();


    public void store() {
        try {
            XmlUtils.saveToFile(configurationFile, this, Application.DEFAULT_ENCODING);
        } catch (Exception e) {
            log.info("Unable to store notifications registry", e);
            ExceptionUtils.throwRuntimeException(e);
        }
    }

	public static class GroupsMapAdaptor extends XmlAdapter<XmlPersitenceResourceBundle, Map<String, Set<String>>> {

		@Override
		public XmlPersitenceResourceBundle marshal(Map<String, Set<String>> localeResourceBundleFiles) throws Exception {
			XmlPersitenceResourceBundle xmlLocaleResourceBundle = new XmlPersitenceResourceBundle();

			List<String> keys = new ArrayList<String>(localeResourceBundleFiles.keySet());
			Collections.sort(keys);

			for (String key : keys) {
				List<String> files = new ArrayList<String>(localeResourceBundleFiles.get(key));
				Collections.sort(files);
                XmlPersistenceEntry entry = new XmlPersistenceEntry();
                entry.name = key;
                entry.addresses = files;
				xmlLocaleResourceBundle.groups.add(entry);
			}
			return xmlLocaleResourceBundle;
		}

		@Override
		public Map<String, Set<String>> unmarshal(XmlPersitenceResourceBundle xmlPersistenceResourceBundle) throws Exception {
			Map<String, Set<String>> persistenceResourceBundleFiles = new HashMap<String, Set<String>>();

			for (XmlPersistenceEntry persistenceEntry : xmlPersistenceResourceBundle.groups) {
				persistenceResourceBundleFiles.put(persistenceEntry.name, new HashSet<String>(persistenceEntry.addresses));
			}

			return persistenceResourceBundleFiles;
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class XmlPersitenceResourceBundle {
		@XmlElement(name = "NotificationGroup", required = true)
		public final List<XmlPersistenceEntry> groups = new ArrayList<XmlPersistenceEntry>();
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class XmlPersistenceEntry {

		@XmlAttribute(name = "name", required = true)
		public String name;

		@XmlElement(name = "Address")
        public List<String> addresses;
	}
}
