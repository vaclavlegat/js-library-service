package com.etnetera.hr;

import com.etnetera.hr.controller.JavaScriptFrameworkController;
import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkVersion;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(JavaScriptFrameworkController.class)
public class JavaScriptFrameworkControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JavaScriptFrameworkRepository repository;

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    private List<JavaScriptFramework> testFrameworks;

    private JavaScriptFramework testFramework;

    @Before
    public void setUpTest(){
        JavaScriptFramework angular = new JavaScriptFramework();
        angular.setName("Angular");
        angular.setHypeLevel(6);
        angular.setDeprecationDate(new Date());

        JavaScriptFramework react = new JavaScriptFramework();
        react.setName("React");
        react.setHypeLevel(8);
        react.setDeprecationDate(new Date());

        JavaScriptFrameworkVersion version1 = new JavaScriptFrameworkVersion();
        version1.setVersion("1");
        JavaScriptFrameworkVersion version2 = new JavaScriptFrameworkVersion();
        version2.setVersion("2");
        List<JavaScriptFrameworkVersion> versions = new ArrayList<>();
        versions.add(version1);
        versions.add(version2);
        angular.setVersions(versions);
        react.setVersions(versions);

        testFramework = angular;
        testFrameworks = new ArrayList<>();
        testFrameworks.add(angular);
        testFrameworks.add(react);
    }


    @Test
    public void testGetFrameworksSuccess() throws Exception {

        when(repository.findAll()).thenReturn(testFrameworks);

        mockMvc.perform(get("/frameworks").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testFindFrameworkSuccess() throws Exception {

        when(repository.findById(anyLong())).thenReturn(Optional.of(testFramework));

        mockMvc.perform(get("/frameworks/{id}", 1L).accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name", is("Angular")))
                .andExpect(jsonPath("$.hypeLevel", is(6)));
    }

    @Test
    public void testFindFrameworkNotFound() throws Exception {

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/frameworks/{id}", 1L).accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindFrameworkBadRequest() throws Exception {

        mockMvc.perform(get("/frameworks/abc").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateFrameworkSuccess() throws Exception {

        when(repository.save(any(JavaScriptFramework.class))).thenReturn(testFramework);

        mockMvc.perform(post("/frameworks")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(testFramework))
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name", is("Angular")))
                .andExpect(jsonPath("$.hypeLevel", is(6)))
                .andExpect(jsonPath("$.versions[0].version", is("1")));

    }

    @Test
    public void testUpdateFrameworkSuccess() throws Exception {

        JavaScriptFramework angular = new JavaScriptFramework();
        angular.setId(1L);
        angular.setName("Angular");
        angular.setHypeLevel(6);
        angular.setDeprecationDate(new Date());

        JavaScriptFramework updatedAngular = new JavaScriptFramework();
        updatedAngular.setId(1L);
        updatedAngular.setName("Angular");
        updatedAngular.setHypeLevel(4);
        updatedAngular.setDeprecationDate(new Date());

        when(repository.findById(anyLong())).thenReturn(Optional.of(angular));
        when(repository.save(any(JavaScriptFramework.class))).thenReturn(updatedAngular);

        mockMvc.perform(put("/frameworks/{id}", angular.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(angular))
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.hypeLevel", is(4)));

    }

    @Test
    public void testDeleteFrameworkSuccess() throws Exception {

        mockMvc.perform(delete("/frameworks/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent());

    }


    @Test
    public void testSearchSuccess() throws Exception {

        when(repository.findByNameIgnoreCaseContaining(anyString())).thenReturn(Optional.of(testFrameworks));

        mockMvc.perform(get("/frameworks/search?query=a")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

    }
}