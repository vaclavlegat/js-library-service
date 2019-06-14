package com.etnetera.hr;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkVersion;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import org.assertj.core.util.DateUtil;
import org.h2.util.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Class used for Spring Boot/MVC based tests.
 * 
 * @author Etnetera
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JavaScriptFrameworkTests {

    @Autowired
    private JavaScriptFrameworkRepository repository;

    @Before
    public void init(){
        JavaScriptFramework angular = new JavaScriptFramework();
        angular.setName("Angular");
        angular.setHypeLevel(6);
        angular.setDeprecationDate(new Date());

        JavaScriptFramework react = new JavaScriptFramework();
        react.setName("React");
        react.setHypeLevel(8);
        react.setDeprecationDate(new Date());

        repository.save(angular);
        repository.save(react);

    }

    @Test
    public void testGetFrameworks() {
        Iterable<JavaScriptFramework> frameworks = repository.findAll();

        assertNotNull(frameworks);

    }

    @Test
    public void testCreateFramework() {
        JavaScriptFramework framework = new JavaScriptFramework();
        framework.setName("Angular");
        framework.setHypeLevel(5);
        framework.setDeprecationDate(new Date());

        JavaScriptFrameworkVersion version1 = new JavaScriptFrameworkVersion();
        version1.setVersion("1");
        JavaScriptFrameworkVersion version2 = new JavaScriptFrameworkVersion();
        version2.setVersion("2");
        List<JavaScriptFrameworkVersion> versions = new ArrayList<>();
        versions.add(version1);
        versions.add(version2);
        framework.setVersions(versions);

        framework = repository.save(framework);

        JavaScriptFramework saved = repository.findById(framework.getId()).orElse(null);

        assertNotNull(saved);

        assertEquals(framework.getName(), saved.getName());
        assertEquals(framework.getHypeLevel(), saved.getHypeLevel());
        assertEquals(framework.getVersions().size(), saved.getVersions().size());

    }

    @Test
    public void testUpdateFramework() {

        Long id = 1L;
        Integer hypeLevel = 5;
        JavaScriptFramework framework = repository.findById(id).orElse(null);
        assertNotNull(framework);

        framework.setHypeLevel(hypeLevel);

        repository.save(framework);

        JavaScriptFramework updatedFramework = repository.findById(id).orElse(null);
        assertNotNull(updatedFramework);

        assertEquals(hypeLevel, updatedFramework.getHypeLevel());


    }

    @Test
    public void testDeleteFramework() {

        JavaScriptFramework framework = new JavaScriptFramework();
        framework.setName("Angular");
        framework.setHypeLevel(5);
        framework.setDeprecationDate(new Date());

        JavaScriptFrameworkVersion version1 = new JavaScriptFrameworkVersion();
        version1.setVersion("1");
        JavaScriptFrameworkVersion version2 = new JavaScriptFrameworkVersion();
        version2.setVersion("2");
        List<JavaScriptFrameworkVersion> versions = new ArrayList<>();
        versions.add(version1);
        versions.add(version2);
        framework.setVersions(versions);


        JavaScriptFramework savedFramework = repository.save(framework);
        savedFramework = repository.findById(savedFramework.getId()).orElse(null);
        assertNotNull(savedFramework);

        repository.deleteById(savedFramework.getId());

        savedFramework = repository.findById(savedFramework.getId()).orElse(null);
        assertNull(savedFramework);


    }

    @Test
    public void testSearchFramework() {

        List<JavaScriptFramework> foundFrameworks = repository.findByNameIgnoreCaseContaining("angular").orElse(new ArrayList<>());
        assertFalse(foundFrameworks.isEmpty());

        List<JavaScriptFramework> notFoundFrameworks = repository.findByNameIgnoreCaseContaining("vue").orElse(new ArrayList<>());
        assertTrue(notFoundFrameworks.isEmpty());

    }
	
}
