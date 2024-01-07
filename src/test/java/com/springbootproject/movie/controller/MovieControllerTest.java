package com.springbootproject.movie.Controller;

import com.springbootproject.movie.Model.CrewMember;
import com.springbootproject.movie.Model.Movie;
import com.springbootproject.movie.MovieApplication;
import com.springbootproject.movie.Service.MovieService;
import com.springbootproject.movie.Service.MovieServiceImpl;
//import com.springbootproject.movie.TestSecurityConfiguration;
//import com.springbootproject.movie.config.ApplicationConfig;
//import com.springbootproject.movie.config.JwtService;
//import com.springbootproject.movie.config.SecurityConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = MovieApplication.class)
@AutoConfigureMockMvc
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
        movie1.setId(2);
        movie1.setName("another movie");
        movie1.setDescription("another description");
        movie1.setStatus(true);

        List<Movie> movieList = Arrays.asList(movie, movie1);

        when(movieService.findAllMovies()).thenReturn(movieList);

        ResultActions result = mockMvc.perform(get("/api/v1/admin/movies")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(movieList.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(movieList.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(movieList.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(movieList.get(0).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(movieList.get(1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(movieList.get(1).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(movieList.get(1).getDescription()));
    }

    @Test
    public void movieController_CreateMovie_ReturnCreatedMovie() throws Exception {

        Movie movieToCreate = new Movie();
        movieToCreate.setName("New Movie");
        movieToCreate.setDescription("New Movie Description");
        movieToCreate.setStatus(true);

        Movie createdMovie = new Movie();
        createdMovie.setId(1);
        createdMovie.setName("New Movie");
        createdMovie.setDescription("New Movie Description");
        createdMovie.setStatus(true);

        ResponseEntity<Movie> createdResponseEntity = ResponseEntity.ok(createdMovie);

        Mockito.when(movieService.createMovie(Mockito.any(Movie.class))).thenReturn(createdResponseEntity);


        ResultActions result = mockMvc.perform(post("/api/v1/admin/movies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieToCreate)));


        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdMovie.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(createdMovie.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(createdMovie.getDescription()));


    }

    @Test
    public void movieController_CreateMovieWithCrewDetails_ReturnCreatedMovie() throws Exception {
        Movie movieToCreate = new Movie();
        movieToCreate.setName("New Movie");
        movieToCreate.setDescription("New Movie Description");
        movieToCreate.setStatus(true);

        Movie createdMovie = new Movie();
        createdMovie.setId(1);
        createdMovie.setName("New Movie");
        createdMovie.setDescription("New Movie Description");
        createdMovie.setStatus(true);

        ResponseEntity<Movie> createdResponseEntity = ResponseEntity.ok(createdMovie);

        Mockito.when(movieService.createMovieWithCrewDetails(Mockito.any(Movie.class))).thenReturn(createdResponseEntity);

        ResultActions result = mockMvc.perform(post("/api/v1/admin/movies/createWithCrewDetails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieToCreate)));

        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdMovie.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(createdMovie.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(createdMovie.getDescription()));
    }

    @Test
    public void movieController_RetrieveMovie_ReturnsMovie() throws Exception {
        Movie movie = new Movie();
        movie.setId(1);
        movie.setName("Test Movie");
        movie.setDescription("Test Movie Description");
        movie.setStatus(true);

        Mockito.when(movieService.retriveMovie(Mockito.anyInt())).thenReturn(movie);

        ResultActions result = mockMvc.perform(get("/api/v1/admin/movies/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(movie.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(movie.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(movie.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(movie.isStatus()));
    }

    @Test
    public void movieController_FindAllMoviesWithCrewDetails_ReturnListOfMovies() throws Exception {
        Movie movie = new Movie();
        movie.setId(1);
        movie.setName("test movie");
        movie.setDescription("test description");
        movie.setStatus(true);

        CrewMember crewMember1 = new CrewMember();
        crewMember1.setId(101);
        crewMember1.setName("John Doe");
        crewMember1.setRole("Director");

        CrewMember crewMember2 = new CrewMember();
        crewMember2.setId(102);
        crewMember2.setName("Jane Smith");
        crewMember2.setRole("Actor");

        movie.setCrewMemberList(Arrays.asList(crewMember1, crewMember2));

        Movie movie1 = new Movie();
        movie1.setId(2);
        movie1.setName("another movie");
        movie1.setDescription("another description");
        movie1.setStatus(true);

        CrewMember crewMember3 = new CrewMember();
        crewMember3.setId(103);
        crewMember3.setName("Alice Johnson");
        crewMember3.setRole("Producer");

        movie1.setCrewMemberList(Collections.singletonList(crewMember3));

        List<Movie> movieList = Arrays.asList(movie, movie1);

        when(movieService.findAllMoviesWithCrewDetails()).thenReturn(movieList);

        ResultActions result = mockMvc.perform(get("/api/v1/admin/movies/crewDetails")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void movieController_UpdateMovieDetails_ReturnUpdatedMovie() throws Exception {
        int movieId = 1;
        Movie movieToUpdate = new Movie();
        movieToUpdate.setName("Updated Movie");
        movieToUpdate.setDescription("Updated Movie Description");
        movieToUpdate.setStatus(true);

        Movie updatedMovie = new Movie();
        updatedMovie.setId(movieId);
        updatedMovie.setName("Updated Movie");
        updatedMovie.setDescription("Updated Movie Description");
        updatedMovie.setStatus(true);

        ResponseEntity<Movie> updatedResponseEntity = ResponseEntity.ok(updatedMovie);

        Mockito.when(movieService.updateMovieDetailsWithId(movieId, movieToUpdate)).thenReturn(updatedResponseEntity);

        ResultActions result = mockMvc.perform(put("/api/v1/admin/movies/updateDetails/{id}", movieId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieToUpdate)));

        result.andExpect(status().isOk());
    }

    @Test
    public void movieController_DeleteMovieById_ReturnNoContent() throws Exception {
        int movieId = 1;

        ResponseEntity<Void> deleteResponseEntity = ResponseEntity.noContent().build();

        Mockito.when(movieService.deleteMovieById(movieId)).thenReturn(deleteResponseEntity);

        ResultActions result = mockMvc.perform(delete("/api/v1/admin/movies/delete/{id}", movieId));

        result.andExpect(status().isNoContent());
    }



}