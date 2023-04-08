package com.developmentjunit.junitrestapi.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.developmentjunit.junitrestapi.entity.Book;
import com.developmentjunit.junitrestapi.repository.BookRepository;

@RestController
@RequestMapping(value = "/book")
public class BookController {

	@Autowired
	BookRepository bookRepository;
	
	@GetMapping
	public List<Book> getAllBookRecords(){
		return bookRepository.findAll();
	}
	
	@GetMapping(value = "{bookId}")
	public Book getBookById(@PathVariable(value = "bookId") Long bookId) {
		return bookRepository.findById(bookId).get();
	}
	
	@PostMapping
	public Book createBookRecord(@RequestBody Book bookRecord) {
		return bookRepository.save(bookRecord);
	}
	
	@PutMapping
	public Book updateBookRecord(@RequestBody Book book)throws NotFoundException{
		if(book == null || book.getBookId() == null) {
			return null; //à revoir
		}
		Optional<Book> optionalBook = bookRepository.findById(book.getBookId());
		if(!optionalBook.isPresent()) {
			throw new NotFoundException();
		}
		
		Book existingBookRecord = optionalBook.get();
		existingBookRecord.setName(book.getName());
		existingBookRecord.setSummary(book.getSummary());
		existingBookRecord.setRating(book.getRating());
		return bookRepository.save(existingBookRecord);
	}
	
	@DeleteMapping(value = "{bookId}")
	public void deleteBookById(@PathVariable(value = "bookId") Long bookId) throws NotFoundException {
	    if (bookRepository.findById(bookId).isEmpty()) {
	        throw new NotFoundException();
	    }
	    bookRepository.deleteById(bookId);
	}

	//TODO: write /delete endpoint using TDD method
}
