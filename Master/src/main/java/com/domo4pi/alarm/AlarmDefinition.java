package com.domo4pi.alarm;

import com.domo4pi.alarm.area.Area;
import com.domo4pi.alarm.area.registry.AlarmRegistry;
import com.domo4pi.alarm.area.registry.AreaPreview;
import com.domo4pi.application.Application;
import com.domo4pi.alarm.nodes.MasterNode;
import com.domo4pi.alarm.nodes.Node;
import com.domo4pi.alarm.nodes.RemoteNode;
import com.domo4pi.utils.ExceptionUtils;
import com.domo4pi.utils.xml.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@XmlAccessorType(XmlAccessType.FIELD)
public class AlarmDefinition {

    public static final String CONFIGURATION_FILE = "Alarm.xml";
    private static final Logger log = LoggerFactory.getLogger(AlarmDefinition.class);

    @XmlElementWrapper(name = "Nodes")
    @XmlElements({@XmlElement(name = "Master", type = MasterNode.class), @XmlElement(name = "Remote", type = RemoteNode.class)})
    public List<Node> nodes = new ArrayList<>();

    @XmlElementWrapper(name = "Areas")
    @XmlElement(name = "Area")
    public List<Area> areas = new ArrayList<>();

    @XmlTransient
    private AtomicBoolean sabotageEnabled = new AtomicBoolean(true);

    @XmlTransient
    private AtomicBoolean sirenEnabled = new AtomicBoolean(true);

    public void load() {
        try {
            AlarmRegistry alarmRegistry = XmlUtils.readFromFile(new File(Application.getInstance().getBaseDir(), CONFIGURATION_FILE), AlarmRegistry.class, Application.DEFAULT_ENCODING);

            for (AreaPreview areaPreview : alarmRegistry.areas) {
                Area area = getArea(areaPreview.name);

                area.setStatus(areaPreview.status);
                area.changeStatusTime = areaPreview.changeStatusTime;
                area.activeSensors = areaPreview.activeSensors;
            }
        } catch (Exception e) {
            log.info("Unable to read alarm registry: {}", e.getMessage());
            save();
        }
    }

    public Area getArea(String name) {
        Area result = null;
        for (Area area : areas) {
            if (area.name.equals(name)) {
                result = area;
                break;
            }
        }

        if (result == null) {
            throw new IllegalArgumentException("Area " + name + " not found");
        }
        return result;
    }


    public Node getNode(String name) {
        Node result = null;
        for (Node node : nodes) {
            if (node.name.equals(name)) {
                result = node;
                break;
            }
        }

        if (result == null) {
            throw new IllegalArgumentException("Node " + name + " not found");
        }
        return result;
    }

    public void save() {
        AlarmRegistry alarmRegistry = new AlarmRegistry();
        try {
            for (Area area : areas) {
                AreaPreview preview = new AreaPreview();
                preview.name = area.name;
                preview.status = area.getStatus();
                preview.changeStatusTime = area.changeStatusTime;
                preview.activeSensors = area.activeSensors;

                alarmRegistry.areas.add(preview);
            }

            XmlUtils.saveToFile(new File(Application.getInstance().getBaseDir(), CONFIGURATION_FILE), alarmRegistry, Application.DEFAULT_ENCODING);
        } catch (Exception e) {
            log.info("Unable to store alarm registry", e);
            ExceptionUtils.throwRuntimeException(e);
        }
    }

    public boolean isSabotageEnabled() {
        return sabotageEnabled.get();
    }

    public AtomicBoolean getSabotageEnabled() {
        return sabotageEnabled;
    }

    public AtomicBoolean getSirenEnabled() {
        return sirenEnabled;
    }

    public String getStatus() {
        String result = "";

        for (Area area : areas) {
            if (!result.isEmpty()) {
                result += ", ";
            }
            result += area.name + ": " + area.getStatus();
        }

        return result;
    }
}
