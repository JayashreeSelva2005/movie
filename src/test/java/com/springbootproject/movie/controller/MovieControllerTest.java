package com.springbootproject.movie.controller;

import com.springbootproject.movie.Model.Movie;
import com.springbootproject.movie.Service.MovieServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
@ExtendWith(MockitoExtension.class)
public class MovieControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieServiceImpl movieService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void movieController_FindAllMovies_ReturnListOfMovies() throws Exception {

        Movie movie = new Movie();
        movie.setId(1);
        movie.setName("test movie");
        movie.setDescription("test description");
        movie.setStatus(true);

        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setName("test movie");
        movie1.setDescription("test description");
        movie1.setStatus(true);

        List<Movie> movieList = Arrays.asList( movie1 , movie
        );

        when(movieService.findAllMovies()).thenReturn(movieList);

        ResultActions result = mockMvc.perform(get("/api/v1/admin/movies")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(movieList.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", CoreMatchers.is(movieList.get(0).getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", CoreMatchers.is(movieList.get(0).getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", CoreMatchers.is(movieList.get(0).getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", CoreMatchers.is(movieList.get(1).getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", CoreMatchers.is(movieList.get(1).getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description", CoreMatchers.is(movieList.get(1).getDescription())));
    }
}
