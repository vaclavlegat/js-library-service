package com.etnetera.hr.data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Simple data entity describing basic properties of every JavaScript framework.
 *
 * @author Etnetera
 *
 */
@Entity
public class JavaScriptFramework {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, length = 30)
	private String name;

	@Temporal(TemporalType.DATE)
    @Column(name = "deprecation_date")
    private Date deprecationDate;

    @Column(name = "hype_level", nullable = false)
    private Integer hypeLevel;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "framework_id")
	private List<JavaScriptFrameworkVersion> versions = new ArrayList<>();

	public JavaScriptFramework() {
	}

	public JavaScriptFramework(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public Date getDeprecationDate() {
        return deprecationDate;
    }

    public void setDeprecationDate(Date deprecationDate) {
        this.deprecationDate = deprecationDate;
    }

    public Integer getHypeLevel() {
        return hypeLevel;
    }

    public void setHypeLevel(Integer hypeLevel) {
        this.hypeLevel = hypeLevel;
    }

    public List<JavaScriptFrameworkVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<JavaScriptFrameworkVersion> versions) {
        this.versions = versions;
    }

    @Override
    public String toString() {
        return "JavaScriptFramework{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deprecationDate=" + deprecationDate +
                ", hypeLevel=" + hypeLevel +
                ", versions=" + versions +
                '}';
    }
}
