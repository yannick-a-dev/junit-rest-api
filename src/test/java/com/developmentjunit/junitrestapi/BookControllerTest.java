package com.developmentjunit.junitrestapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.developmentjunit.junitrestapi.entity.Book;
import com.developmentjunit.junitrestapi.repository.BookRepository;
import com.developmentjunit.junitrestapi.web.BookController;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookController.class)
public class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private BookRepository bookRepository;

//	@InjectMocks
//	private BookController bookController;

	Book RECORD_1 = new Book(1L, "Essola", "Paris", 5);
	Book RECORD_2 = new Book(2L, "Nouma", "Yaounde", 6);
	Book RECORD_3 = new Book(3L, "Azegue", "Nvog Ndi", 7);
//
//	@Before
//	public void setUp() {
//		MockitoAnnotations.openMocks(this);
//		this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
//	}

	@Test
	public void getAllRecords_success() throws Exception {
		List<Book> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));

		Mockito.when(bookRepository.findAll()).thenReturn(records);

		mockMvc.perform(MockMvcRequestBuilders.get("/book").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Essola")));

	}

	@Test
	public void getBookById_success() throws Exception {
		Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(java.util.Optional.of(RECORD_1));

		mockMvc.perform(MockMvcRequestBuilders.get("/book/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("Essola")));
	}

	@Test
	public void createBook_success() throws Exception {
		Book book1 = Book.builder().name("Essola").summary("Paris").rating(15).build();

		Mockito.when(bookRepository.save(book1)).thenReturn(book1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(book1));

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("Essola")));
	}

	@Test
	public void updateBookRecord_success() throws Exception {
		Book bookUpdate = Book.builder()
				.name("Essola")
				.summary("Paris")
				.rating(15)
				.build();

		Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(Optional.of(RECORD_1));
		Mockito.when(bookRepository.save(bookUpdate)).thenReturn(bookUpdate);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(bookUpdate));

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("Essola")));
	}
	

//	@Test
//	public void updateBook_NotFound() throws Exception {
//	    Book updatedBook = Book.builder()
//	    		.bookId(8l)
//	    		.name("Abessola")
//				.summary("Par")
//				.rating(15)
//				.build();
//	    
//	    Mockito.when(bookRepository.findById(updatedBook.getBookId())).thenReturn(null);
//
//	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book")
//	            .contentType(MediaType.APPLICATION_JSON)
//	            .accept(MediaType.APPLICATION_JSON)
//	            .content(this.mapper.writeValueAsString(updatedBook));
//
//	    mockMvc.perform(mockRequest)
//	            .andExpect(status().isBadRequest())
//	            .andExpect(result ->
//	                assertTrue(result.getResolvedException() instanceof NotFoundException))
//	    .andExpect(result ->
//	        assertEquals(" with ID 8 does not exist.", result.getResolvedException().getMessage()));
//	}
	
	@Test
	public void deleteBookById_success() throws Exception {
	    Mockito.when(bookRepository.findById(RECORD_2.getBookId())).thenReturn(Optional.of(RECORD_2));

	    mockMvc.perform(MockMvcRequestBuilders
	            .delete("/book/2")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk());
	}

//	@Test
//	public void deleteBookById_notFound() throws Exception {
//	    Mockito.when(bookRepository.findById(10l)).thenReturn(null);
//
//	    mockMvc.perform(MockMvcRequestBuilders
//	            .delete("/book/2")
//	            .contentType(MediaType.APPLICATION_JSON))
//	    .andExpect(status().isBadRequest())
//	            .andExpect(result ->
//	                    assertTrue(result.getResolvedException() instanceof NotFoundException))
//	    .andExpect(result ->
//	            assertEquals("Book with ID 10 does not exist.", result.getResolvedException().getMessage()));
//	}
}
