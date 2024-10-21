package me.quickscythe;

import java.io.File;

public class JavaMod {

    String name;
    String mainClass;
    String version;
    String description;
    String author;
    String[] links;

    File dataFolder;

    public JavaMod(String name, String mainClass, String version, String description, String author, String[] links){
        this.name = name;
        this.mainClass = mainClass;
        this.version = version;
        this.description = description;
        this.author = author;
        this.links = links;
        this.dataFolder = new File("config/" + name +"/");
        if(!dataFolder.exists()) dataFolder.mkdirs();
    }

    public String getName(){
        return name;
    }

    public File getDataFolder(){
        return dataFolder;
    }

    public String getMainClass(){
        return mainClass;
    }

    public String getVersion(){
        return version;
    }

    public String getDescription(){
        return description;
    }

    public String getAuthor(){
        return author;
    }

    public String[] getLinks(){
        return links;
    }


}
