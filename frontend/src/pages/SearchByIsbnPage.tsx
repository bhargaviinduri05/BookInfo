import { useState } from 'react';
import type { FormEvent } from 'react';
import BookCard from '../components/BookCard';
import Alert from '../components/Alert';
import Loader from '../components/Loader';
import ConfirmDialog from '../components/ConfirmDialog';
import bookService from '../services/bookService';
import { extractErrorMessage } from '../services/errorHandler';
import type { Book } from '../types/book';

function SearchByIsbnPage() {
  const [isbn, setIsbn] = useState('');
  const [book, setBook] = useState<Book | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [searched, setSearched] = useState(false);
  const [bookToDelete, setBookToDelete] = useState<{ id: number; title: string } | null>(null);
  const [successMessage, setSuccessMessage] = useState('');

  async function handleSearch(e: FormEvent) {
    e.preventDefault();
    if (!isbn.trim()) return;

    setLoading(true);
    setError('');
    setBook(null);
    setSearched(true);

    try {
      const result = await bookService.getBookByIsbn(isbn.trim());
      setBook(result);
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setLoading(false);
    }
  }

  function requestDelete(id: number, title: string) {
    setBookToDelete({ id, title });
  }

  async function confirmDelete() {
    if (!bookToDelete) return;
    try {
      await bookService.deleteBook(bookToDelete.id);
      setBook(null);
      setSuccessMessage(`"${bookToDelete.title}" was deleted successfully.`);
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setBookToDelete(null);
    }
  }

  return (
    <div className="page">
      <div className="page__header">
        <h1>Search by ISBN</h1>
      </div>

      <form className="search-bar" onSubmit={handleSearch}>
        <input
          type="text"
          placeholder="Enter ISBN (e.g. 9780134685991)"
          value={isbn}
          onChange={(e) => setIsbn(e.target.value)}
        />
        <button type="submit" className="btn btn--primary">
          Search
        </button>
      </form>

      <Alert type="error" message={error} onClose={() => setError('')} />
      <Alert type="success" message={successMessage} onClose={() => setSuccessMessage('')} />

      {loading && <Loader label="Searching..." />}

      {!loading && book && (
        <div className="book-grid book-grid--single">
          <BookCard book={book} onDelete={requestDelete} />
        </div>
      )}

      {!loading && searched && !book && !error && (
        <div className="empty-state">
          <p>No book found with that ISBN.</p>
        </div>
      )}

      {bookToDelete && (
        <ConfirmDialog
          title="Delete Book"
          message={`Are you sure you want to delete "${bookToDelete.title}"? This action cannot be undone.`}
          onConfirm={confirmDelete}
          onCancel={() => setBookToDelete(null)}
        />
      )}
    </div>
  );
}

export default SearchByIsbnPage;
