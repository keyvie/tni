package gg.wzrds.tools.config;

import java.util.ArrayList;
import java.util.List;

public class PermissionGroup {
    public List<String> uuids = new ArrayList<>();
    public List<String> allow = new ArrayList<>();
    public List<String> deny = new ArrayList<>();
    public int priority = 0;
}