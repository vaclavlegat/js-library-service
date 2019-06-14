package com.etnetera.hr.data;

import javax.persistence.*;

@Entity
public class JavaScriptFrameworkVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "JavaScriptFrameworkVersion{" +
                "id=" + id +
                ", version='" + version + '\'' +
                '}';
    }
}
