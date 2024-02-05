package com.mobile_computing;

import java.io.Serializable;

/**
 * Created by Nicholas Cioli on 1/12/2017.
 */

public class Datum implements Serializable {
    private int    m_id;
    private String m_title;
    private String m_date;
    private String m_text;
    private String m_imageUrl;

    // Constructor
    Datum(int id, String title, String date, String text, String imageUrl) {
        m_id       = id;
        m_title    = title;
        m_date     = date;
        m_text     = text;
        m_imageUrl = imageUrl;
    }

    public Datum() {

    }

    // Get operations
    public int id () { return m_id; }
    public String title() { return m_title; }
    public String date() { return m_date; }
    public String text() { return m_text; }
    public String imageUrl() { return m_imageUrl; }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public void setM_title(String m_title) {
        this.m_title = m_title;
    }

    public void setM_date(String m_date) {
        this.m_date = m_date;
    }

    public void setM_text(String m_text) {
        this.m_text = m_text;
    }

    public void setM_imageUrl(String m_imageUrl) {
        this.m_imageUrl = m_imageUrl;
    }
}
