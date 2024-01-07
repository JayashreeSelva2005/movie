package com.springbootproject.movie.Validator;


import com.springbootproject.movie.Model.Movie;
import com.springbootproject.movie.Model.Ratings;
import com.springbootproject.movie.Model.User;
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
public class RatingsValidatorTest {

    @Autowired
    private LocalValidatorFactoryBean validatorFactoryBean;

    @Test
    void testValidRating() {
        Movie movie = new Movie();
        movie.setName("Inception");
        movie.setDescription("A mind-bending movie");
        movie.setStatus(true);

        User user = new User();
        user.setEmail("john@gmail.com");
        user.setPassword("password");


        Ratings rating = new Ratings(4, "Great movie!", movie, user, true);

        Set<ConstraintViolation<Ratings>> violations = validatorFactoryBean.validate(rating);

        assertEquals(0, violations.size());
    }

    @Test
    void testScoreValidation() {
        Movie movie = new Movie();
        movie.setName("Inception");
        movie.setDescription("A mind-bending movie");
        movie.setStatus(true);

        User user = new User();
        user.setEmail("john@gmail.com");
        user.setPassword("password");

        Ratings rating = new Ratings(6, "Awesome movie!", movie, user, true);

        Set<ConstraintViolation<Ratings>> violations = validatorFactoryBean.validate(rating);

        assertEquals(1, violations.size());
        ConstraintViolation<Ratings> violation = violations.iterator().next();
        assertEquals("Score must be at most 5", violation.getMessage());
    }

    @Test
    void testDescriptionValidation() {
        Movie movie = new Movie();
        movie.setName("Inception");
        movie.setDescription("A mind-bending movie");
        movie.setStatus(true);

        User user = new User();
        user.setEmail("john@gmail.com");
        user.setPassword("password");

        Ratings rating = new Ratings(4, "", movie, user, true);

        Set<ConstraintViolation<Ratings>> violations = validatorFactoryBean.validate(rating);

        assertEquals(1, violations.size());
        ConstraintViolation<Ratings> violation = violations.iterator().next();
        assertEquals("Description cannot be blank", violation.getMessage());
    }

    @Test
    void testStatusNullValidation() {
        Movie movie = new Movie();
        movie.setName("Inception");
        movie.setDescription("A mind-bending movie");
        movie.setStatus(true);

        User user = new User();
        user.setEmail("john@gmail.com");
        user.setPassword("password");

        Ratings rating = new Ratings();
        rating.setMovie(movie);
        rating.setDescription("good movie");
        rating.setId(23);
        rating.setUser(user);
        rating.setScore(4);

        Set<ConstraintViolation<Ratings>> violations = validatorFactoryBean.validate(rating);

        assertEquals(0, violations.size());

    }
}

