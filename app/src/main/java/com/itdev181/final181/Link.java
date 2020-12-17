package com.itdev181.final181;

import java.net.URL;

public class Link {
    private URL url;
    private String name;

    public Link(URL url, String name) {
        this.url = url;
        this.name = name;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
