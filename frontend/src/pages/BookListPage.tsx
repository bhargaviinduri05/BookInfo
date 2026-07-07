import { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import BookCard from '../components/BookCard';
import Loader from '../components/Loader';
import Alert from '../components/Alert';
import ConfirmDialog from '../components/ConfirmDialog';
import bookService from '../services/bookService';
import { extractErrorMessage } from '../services/errorHandler';
import type { Book } from '../types/book';

function BookListPage() {
  const location = useLocation();
  const navigate = useNavigate();

  const [books, setBooks] = useState<Book[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState(
    (location.state as { message?: string } | null)?.message ?? ''
  );
  const [bookToDelete, setBookToDelete] = useState<{ id: number; title: string } | null>(null);

  useEffect(() => {
    // Clear the navigation state so the success message doesn't reappear on refresh/back
    if (location.state) {
      navigate(location.pathname, { replace: true, state: null });
    }
    loadBooks();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  async function loadBooks() {
    setLoading(true);
    setError('');
    try {
      const data = await bookService.getAllBooks();
      setBooks(data);
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
      setBooks((prev) => prev.filter((b) => b.id !== bookToDelete.id));
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
        <h1>All Books</h1>
        <Link to="/add" className="btn btn--primary">
          + Add New Book
        </Link>
      </div>

      <Alert type="error" message={error} onClose={() => setError('')} />
      <Alert type="success" message={successMessage} onClose={() => setSuccessMessage('')} />

      {loading ? (
        <Loader label="Loading books..." />
      ) : books.length === 0 ? (
        <div className="empty-state">
          <p>No books in the library yet.</p>
          <Link to="/add" className="btn btn--primary">
            Add your first book
          </Link>
        </div>
      ) : (
        <div className="book-grid">
          {books.map((book) => (
            <BookCard key={book.id} book={book} onDelete={requestDelete} />
          ))}
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

export default BookListPage;
