import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { isAxiosError } from 'axios';
import BookForm from '../components/BookForm';
import Alert from '../components/Alert';
import Loader from '../components/Loader';
import bookService from '../services/bookService';
import { extractErrorMessage } from '../services/errorHandler';
import type { Book, BookFormData, ValidationErrors } from '../types/book';

function bookToFormData(book: Book): BookFormData {
  return {
    title: book.title,
    author: book.author,
    isbn: book.isbn,
    publisher: book.publisher ?? '',
    genre: book.genre ?? '',
    price: String(book.price),
    quantity: String(book.quantity),
    description: book.description ?? '',
  };
}

function EditBookPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [book, setBook] = useState<Book | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [fieldErrors, setFieldErrors] = useState<ValidationErrors | undefined>(undefined);

  useEffect(() => {
    if (!id) return;
    loadBook(Number(id));
  }, [id]);

  async function loadBook(bookId: number) {
    setLoading(true);
    setError('');
    try {
      const data = await bookService.getBookById(bookId);
      setBook(data);
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setLoading(false);
    }
  }

  async function handleSubmit(form: BookFormData, imageFile: File | null) {
    if (!id) return;
    setError('');
    setFieldErrors(undefined);
    try {
      await bookService.updateBook(Number(id), form, imageFile);
      navigate('/', { state: { message: `"${form.title}" was updated successfully.` } });
    } catch (err) {
      if (isAxiosError(err) && err.response?.status === 400 && err.response.data?.data) {
        setFieldErrors(err.response.data.data as ValidationErrors);
      }
      setError(extractErrorMessage(err));
    }
  }

  if (loading) {
    return (
      <div className="page">
        <Loader label="Loading book details..." />
      </div>
    );
  }

  if (!book) {
    return (
      <div className="page">
        <Alert type="error" message={error || 'Book not found.'} />
      </div>
    );
  }

  return (
    <div className="page">
      <div className="page__header">
        <h1>Edit Book</h1>
      </div>
      <Alert type="error" message={error} onClose={() => setError('')} />
      <BookForm
        initialData={bookToFormData(book)}
        initialImageUrl={book.imageUrl}
        submitLabel="Save Changes"
        onSubmit={handleSubmit}
        fieldErrors={fieldErrors}
      />
    </div>
  );
}

export default EditBookPage;
