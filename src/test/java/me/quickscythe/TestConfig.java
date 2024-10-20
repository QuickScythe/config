package me.quickscythe;

import org.json.JSONObject;

@Config.File(name = "test2", ext = "json")
public class TestConfig extends Config.Content {

    @Config.Value
    public String test = "test1";
    @Config.Value
    public int test2 = 2;
    @Config.Value
    public boolean test3 = true;

    @Config.Value
    public JSONObject test4 = new JSONObject().put("jsonTest", 2).put("jsonTest2", "test").put("jsonTest3", new JSONObject().put("jsonTest4", "test").put("jsonTest5", 2));
}
