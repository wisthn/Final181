package com.itdev181.final181;

import java.net.URL;

public class RSSLink {
    private URL link;
    private String title;

    public RSSLink(URL link, String title) {
        this.link = link;
        this.title = title;
    }

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
