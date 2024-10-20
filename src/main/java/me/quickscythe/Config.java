package me.quickscythe;



import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;

public class Config {

    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());
    private final java.io.File FILE;
    private final Content CONTENT;

    public Config(JavaPlugin plugin, Class<? extends Config.Content> clazz) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        JSONObject fileData;
        if (clazz.isAnnotationPresent(File.class)) {
            File cf = clazz.getAnnotation(File.class);
            if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
            FILE = new java.io.File(plugin.getDataFolder(), cf.name() + "." + cf.ext());
            if (FILE.exists()) {
                fileData = load(FILE);
            } else fileData = new JSONObject();
            CONTENT = clazz.getConstructor().newInstance();
            for (Field f : CONTENT.getContentValues()) {
                if(fileData.has(f.getName())) f.set(CONTENT, fileData.get(f.getName()));
            }
        } else {
            throw new IllegalArgumentException("Class must have @Config.File annotation");
        }
    }

    private JSONObject load(java.io.File file) {
        try (Scanner scanner = new Scanner(file)) {
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
            return new JSONObject(builder.toString());
        } catch (IOException e) {
            LOGGER.severe("Failed to load config file");
            return new JSONObject();
        }
    }

    public Object get(Field field){
        try {
            return field.get(CONTENT);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Object get(String variable) {
        try {
            return CONTENT.getClass().getField(variable).get(CONTENT);
        } catch (NoSuchFieldException e) {
            return null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void set(String variable, Object value) {
        try {
            CONTENT.getClass().getField(variable).set(CONTENT, value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalArgumentException("Field " + variable + " does not exist. Skipping writing to file.");
        }
    }

    public void write() {
        try (FileWriter writer = new FileWriter(FILE)) {
            JSONObject data = new JSONObject();
            for (Field f : CONTENT.getContentValues()) {
                data.put(f.getName(), f.get(CONTENT));
            }
            writer.write(data.toString(2));
        } catch (IOException e) {
            LOGGER.severe("Failed to write config file");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return FILE.getName();
    }

    public Content getContent() {
        return CONTENT;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface File {
        String name();

        String ext() default "json";
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Value {
        boolean override() default false;
    }

    public static class Content {

        @Value(override = true)
        public Number version = 1;

        Field[] getContentValues() {
            return Arrays.stream(this.getClass().getFields()).filter(f -> f.isAnnotationPresent(Value.class)).toArray(Field[]::new);
        }
    }




}
