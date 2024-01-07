package com.springbootproject.movie.Validator;

import com.springbootproject.movie.Model.Movie;
import com.springbootproject.movie.TestConfig;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
public class MovieValidatorTest {

    @Autowired
    private LocalValidatorFactoryBean validatorFactoryBean;

    @Test
    void testValidMovie() {
        Movie movie = new Movie();
        movie.setName("Valid Name");
        movie.setDescription("Valid Description");
        movie.setStatus(true);

        Set<ConstraintViolation<Movie>> violations = validatorFactoryBean.validate(movie);

        assertEquals(0, violations.size());
    }

    @Test
    void testNameValidation() {
        Movie movie = new Movie();
        movie.setName("a");
        movie.setDescription("A mind-bending movie");
        movie.setStatus(true);
        Set<ConstraintViolation<Movie>> violations = validatorFactoryBean.validate(movie);

        assertEquals(1, violations.size());
        ConstraintViolation<Movie> violation = violations.iterator().next();
        assertEquals("Name must be between 1 and 255 characters", violation.getMessage());
    }

    @Test
    void testDescriptionValidation() {
        Movie movie = new Movie();
        movie.setDescription("a");
        movie.setName("Inception-3");
        movie.setStatus(true);

        Set<ConstraintViolation<Movie>> violations = validatorFactoryBean.validate(movie);

        assertEquals(1, violations.size());
        ConstraintViolation<Movie> violation = violations.iterator().next();
        assertEquals("Description must be between 1 and 1000 characters", violation.getMessage());
    }

    @Test
    void testNameNullValidation() {
        Movie movie = new Movie();
        movie.setName("");
        movie.setDescription("A mind-bending movie");
        movie.setStatus(true);
        Set<ConstraintViolation<Movie>> violations = validatorFactoryBean.validate(movie);

        assertEquals(2, violations.size());

    }

    @Test
    void testDescriptionNullValidation() {
        Movie movie = new Movie();

        movie.setName("Inception-3");
        movie.setStatus(true);

        Set<ConstraintViolation<Movie>> violations = validatorFactoryBean.validate(movie);

        assertEquals(2, violations.size());
    }

    @Test
    void testStatusNullValidation() {
        Movie movie = new Movie();

        movie.setName("Inception-3");
        movie.setDescription("A mind-bending movie");

        Set<ConstraintViolation<Movie>> violations = validatorFactoryBean.validate(movie);

        assertEquals(1, violations.size());
        ConstraintViolation<Movie> violation = violations.iterator().next();
        assertEquals("Status cannot be null", violation.getMessage());
    }

}
