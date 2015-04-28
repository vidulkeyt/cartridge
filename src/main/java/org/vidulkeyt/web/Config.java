package org.vidulkeyt.web;

public class Config {
    private Config() {
        super();
    }
    public static Config getInstance() {
        return new Config();
    }

    public String getClientId() {
        return "730d694f9478dc921d40";
    }
    public String getClientSecret() {
        return "765dd1c85ae46466f53423833f7e28854af8ac6d";
    }
    public String getRedirectURI() {
        return "http://helloworld2-vidulkeyt.rhcloud.com/github/auth";
    }
}
