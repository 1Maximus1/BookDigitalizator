package libraryDigitalizer.controllers;

import jakarta.validation.Valid;
import libraryDigitalizer.model.Book;
import libraryDigitalizer.model.Person;
import libraryDigitalizer.services.BooksService;
import libraryDigitalizer.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {
    private final PeopleService peopleService;
    private final BooksService booksService;

    @Autowired
    public BookController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }


    @GetMapping()
    public String index(@RequestParam(value = "page",required = false) Integer pageN, @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,@RequestParam(value = "sort_by_year", required = false) boolean sortByYear, Model model){
        if(pageN == null || booksPerPage == null){
            model.addAttribute("books", booksService.findAll(sortByYear));
        }else {
            model.addAttribute("books", booksService.findWithPagination(pageN,booksPerPage, sortByYear));
        }

        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book")Book book){
        return "books/new";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person){
        model.addAttribute("book", booksService.findOne(id));

        Person bookOwner = booksService.getBookOwner(id);
        if (bookOwner != null){
            model.addAttribute("owner", bookOwner);
        }else {
            model.addAttribute("people", peopleService.findAll());
        }
        return "books/show";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "books/new";
        }

        booksService.save(book);

        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, @PathVariable("id") int id, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "books/edit";
        }

        booksService.update(id, book);
        return "redirect:/books";

    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        booksService.delete(id);
        return "redirect:/books";

    }

    @PatchMapping("/add/{id}")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson){
        booksService.appointing(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/free/{id}")
    public String release(@PathVariable("id") int id){
        booksService.release(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchPage(){
        return "books/search";
    }

    @PostMapping("/search")
    public String searchReply(@RequestParam("request") String request, Model model){
        model.addAttribute("books", booksService.searchByTitle(request));
        return "books/search";
    }
}
