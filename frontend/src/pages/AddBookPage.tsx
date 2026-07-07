import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import BookForm from '../components/BookForm';
import Alert from '../components/Alert';
import bookService from '../services/bookService';
import { extractErrorMessage } from '../services/errorHandler';
import { emptyBookForm } from '../types/book';
import type { BookFormData, ValidationErrors } from '../types/book';
import { isAxiosError } from 'axios';

function AddBookPage() {
  const navigate = useNavigate();
  const [error, setError] = useState('');
  const [fieldErrors, setFieldErrors] = useState<ValidationErrors | undefined>(undefined);

  async function handleSubmit(form: BookFormData, imageFile: File | null) {
    setError('');
    setFieldErrors(undefined);
    try {
      await bookService.createBook(form, imageFile);
      navigate('/', { state: { message: `"${form.title}" was added successfully.` } });
    } catch (err) {
      if (isAxiosError(err) && err.response?.status === 400 && err.response.data?.data) {
        setFieldErrors(err.response.data.data as ValidationErrors);
      }
      setError(extractErrorMessage(err));
    }
  }

  return (
    <div className="page">
      <div className="page__header">
        <h1>Add New Book</h1>
      </div>
      <Alert type="error" message={error} onClose={() => setError('')} />
      <BookForm
        initialData={emptyBookForm}
        submitLabel="Add Book"
        onSubmit={handleSubmit}
        fieldErrors={fieldErrors}
      />
    </div>
  );
}

export default AddBookPage;
