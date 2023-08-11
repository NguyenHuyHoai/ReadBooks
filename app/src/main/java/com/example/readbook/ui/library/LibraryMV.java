package com.example.readbook.ui.library;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.readbook.models.Book;

import java.util.List;

public class LibraryMV extends ViewModel {

    private LibraryRepository repository = new LibraryRepository();

    public LiveData<List<Book>> getLibraryBooks() {
        return repository.getLibraryBooks();
    }
}
