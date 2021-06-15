package com.webmidtermhw.controller;

import com.webmidtermhw.dataAccessObjects.*;
import com.webmidtermhw.model.Entities.*;
import com.webmidtermhw.model.dataTransferObjects.BookCommentsModel;
import com.webmidtermhw.model.dataTransferObjects.BookModel;
import com.webmidtermhw.model.dataTransferObjects.CommentModel;
import com.webmidtermhw.model.dataTransferObjects.CommentReplyModel;
import com.webmidtermhw.services.CommentReplyService;
import com.webmidtermhw.services.CommentService;
import com.webmidtermhw.services.MongoNextSequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class LibraryWebService {
    @Autowired
    RepositoryForBookCategory repositoryForBookCategory;

    @Autowired
    RepositoryForBook repositoryForBook;

    @Autowired
    RepositoryForPickedBook repositoryForPickedBook;

    @Autowired
    RepositoryForUser repositoryForUser;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentReplyService commentReplyService;

    @Autowired
    MongoNextSequenceService nextSequenceService;

    //list all book categories
    @GetMapping("/categories")
    public ResponseEntity<?> listBookCategories(){
        return ResponseEntity.ok(repositoryForBookCategory.findAll());
    }

    //add book category
    @PostMapping("/create/category")
    public ResponseEntity<?> createCategory(@RequestBody Category category){
        return ResponseEntity.ok(repositoryForBookCategory.save(category));
    }

    //list all books
    @GetMapping("/books")
    public ResponseEntity<?> listAllBooks(){
        return ResponseEntity.ok(repositoryForBook.findAll());
    }

    //create book
    @PostMapping("/create/book")
    public ResponseEntity<?> createBook(@RequestBody BookModel book){
        Book newBook = new Book(book);
        Optional<Category> category = repositoryForBookCategory.findById(book.getCategoryId());
        if (category.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No category provided id");
        newBook.setCategory(category.get());
        newBook.setAvailable(true);
        return ResponseEntity.ok(repositoryForBook.save(newBook));
    }

    //logged in user pick up a book by id
    @GetMapping("/pickup/{bookId}")
    public ResponseEntity<?> pickupBook(@PathVariable("bookId") long bookId){
        //get this book from db
        Optional<Book> book = repositoryForBook.findById(bookId);
        //if there's no such book return
        if (book.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No book with the provided id");

        //check if book is available for pick
        if (!book.get().getAvailable())
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body("This book is not available for pick up");

        //get logged in user
        User user = getLoggedInUser();
        //check if user is null
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No logged in user");

        //check if this book was picked up by this user and has not been dropped off
        PickedBook pickedBook = repositoryForPickedBook.findByBookAndUserAndDroppedOff(book.get(), user, false);
        if (pickedBook != null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You have picked up this book and you have not dropped it");

        //create new pickedBook
        PickedBook newPickedBook = new PickedBook(book.get(), user);

        //set pick date
        newPickedBook.setPickDate(new Date());

        //save the picked book details
        repositoryForPickedBook.save(newPickedBook);

        //set this book as unavailable for pick
        book.get().setAvailable(false);

        //save the book
        repositoryForBook.save(book.get());

        return ResponseEntity.ok("Book picked");
    }

    //drop off a book by its id
    @GetMapping("/dropoff/{bookId}")
    public ResponseEntity<?> dropOffBook(@PathVariable("bookId") long bookId){
        //get this book from db
        Optional<Book> book = repositoryForBook.findById(bookId);
        //if there's no such book return
        if (book.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No book with the provided id");

        //get logged in user
        User user = getLoggedInUser();
        //check if user is null
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No logged in user");

        //check if this book was picked up by this user and has not been dropped off
        PickedBook pickedBook = repositoryForPickedBook.findByBookAndUserAndDroppedOff(book.get(), user, false);
        if (pickedBook == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You have not picked up this book");

        //drop off this book
        pickedBook.setDroppedOff(true);
        //save it
        repositoryForPickedBook.save(pickedBook);

        //set this book as available
        book.get().setAvailable(true);
        //save the book
        repositoryForBook.save(book.get());

        return ResponseEntity.ok("This book has been dropped off");
    }

    //get a list of current picked up books by user
    @GetMapping("/list/currentpicks")
    public ResponseEntity<?> listOfCurrentPickedUpBooks(){
        //get logged in user
        User user = getLoggedInUser();
        //check if user is null
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No logged in user");

        return ResponseEntity.ok(repositoryForPickedBook.findByUserAndDroppedOff(user, false));
    }

    //show logged in user history
    @GetMapping("/my-history")
    public ResponseEntity<?> userHistory(){
        //get logged in user
        User user = getLoggedInUser();
        //check if user is null
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No logged in user");

        return ResponseEntity.ok(repositoryForPickedBook.findByUser(user));
    }

    //search for a book by book name and category id
    @GetMapping("/search/{bookName}/{categoryId}")
    public ResponseEntity<?> findByBookNameAndCategoryId(@PathVariable("bookName") String bookName, @PathVariable("categoryId") long categoryId ){
        Optional<Category> category = repositoryForBookCategory.findById(categoryId);
        if(category.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such book category");

        return ResponseEntity.ok(repositoryForBook.findFirstByNameAndCategory(bookName, category.get()));

    }

    //search for a book by book name and category id and author
    @GetMapping("/search/{bookName}/{categoryId}/{author}")
    public ResponseEntity<?> findByBookNameAndCategoryIdAndAuthor(@PathVariable("bookName") String bookName, @PathVariable("categoryId") long categoryId , @PathVariable("author") String author){
        Optional<Category> category = repositoryForBookCategory.findById(categoryId);
        if(category.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such book category");

        return ResponseEntity.ok(repositoryForBook.findFirstByNameAndCategoryAndAuthor(bookName, category.get(), author));

    }

    //method to post book comment
    @PostMapping("/book/comment")
    public ResponseEntity<?> postBookComment(@RequestBody CommentModel commentModel){
        //get this book from db
        Optional<Book> book = repositoryForBook.findById(commentModel.getBookId());
        //if there's no such book return
        if (book.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No book with the provided id");

        //get logged in user
        User user = getLoggedInUser();
        //check if user is null
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No logged in user");

        //create book comment
        Comment comment = new Comment(commentModel.getText(), book.get(), user);
        int id = nextSequenceService.getNextSequence("customSequences");
        comment.setId((long) id);

        //save the comment and return it
        return ResponseEntity.ok(commentService.addComment(comment));
    }

    //method to post book comment
    @PostMapping("/book/commentReply")
    public ResponseEntity<?> postBookCommentReply(@RequestBody CommentReplyModel commentReplyModel){
        //get this comment from db
        Comment comment = commentService.getById(commentReplyModel.getCommentId());
        //if there's no such book return
        if (comment == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No comment with the provided id");

        //get logged in user
        User user = getLoggedInUser();
        //check if user is null
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No logged in user");

        //create book comment
        CommentReply commentReply = new CommentReply(commentReplyModel.getText(), comment, user);
        int id = nextSequenceService.getNextSequence("customSequences");
        commentReply.setId((long) id);

        //save the comment and return it
        return ResponseEntity.ok(commentReplyService.addCommentReply(commentReply));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable("bookId") long bookId){
        Optional<Book> book = repositoryForBook.findById(bookId);
        //if there's no such book return
        if (book.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No book with the provided id");

        //get book comments
        List<Comment> comments = commentService.findByBookId(bookId);
        //fetch and set comment replies
        comments.forEach(comment -> comment.setReplies(commentReplyService.findByComment(comment)));

        BookCommentsModel bookCommentsModel = new BookCommentsModel(comments, book.get());

        return ResponseEntity.ok(bookCommentsModel);
    }

    //method to get logged in user
    public User getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ( authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        UserDetails loggedInUser;
        try {
            loggedInUser = (UserDetails) principal;
        }
        catch (Exception e){
            return null;
        }

        return repositoryForUser.findByEmail(loggedInUser.getUsername());
    }
}
