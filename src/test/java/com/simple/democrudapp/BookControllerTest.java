package com.simple.democrudapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    void shouldCreateBook() throws Exception {
        Book payload = new Book("Test Title", "Author", 2020);

        MvcResult result = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andReturn();

        Book created = objectMapper.readValue(result.getResponse().getContentAsString(), Book.class);
        assertThat(bookRepository.findById(created.getId())).isPresent();
    }

    @Test
    void shouldGetBookById() throws Exception {
        Book saved = bookRepository.save(new Book("Sample", "Someone", 2021));

        mockMvc.perform(get("/api/books/" + saved.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Sample"))
            .andExpect(jsonPath("$.author").value("Someone"))
            .andExpect(jsonPath("$.year").value(2021));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        Book saved = bookRepository.save(new Book("Old", "Author", 2010));
        Book updatePayload = new Book("New", "Author 2", 2022);

        mockMvc.perform(put("/api/books/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePayload)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("New"))
            .andExpect(jsonPath("$.author").value("Author 2"))
            .andExpect(jsonPath("$.year").value(2022));

        Book updated = bookRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo("New");
    }

    @Test
    void shouldDeleteBook() throws Exception {
        Book saved = bookRepository.save(new Book("Delete Me", "Anon", 2000));

        mockMvc.perform(delete("/api/books/" + saved.getId()))
            .andExpect(status().isNoContent());

        assertThat(bookRepository.existsById(saved.getId())).isFalse();
    }
}
