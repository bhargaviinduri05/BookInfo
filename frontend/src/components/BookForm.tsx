import { useState } from 'react';
import type { ChangeEvent, FormEvent } from 'react';
import type { BookFormData, ValidationErrors } from '../types/book';

interface BookFormProps {
  initialData: BookFormData;
  initialImageUrl?: string | null;
  submitLabel: string;
  onSubmit: (form: BookFormData, imageFile: File | null) => Promise<void>;
  fieldErrors?: ValidationErrors;
}

function BookForm({ initialData, initialImageUrl, submitLabel, onSubmit, fieldErrors }: BookFormProps) {
  const [form, setForm] = useState<BookFormData>(initialData);
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string | null>(initialImageUrl ?? null);
  const [submitting, setSubmitting] = useState(false);

  function handleChange(e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  function handleImageChange(e: ChangeEvent<HTMLInputElement>) {
    const file = e.target.files?.[0] ?? null;
    setImageFile(file);
    if (file) {
      setPreviewUrl(URL.createObjectURL(file));
    }
  }

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setSubmitting(true);
    try {
      await onSubmit(form, imageFile);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form className="book-form" onSubmit={handleSubmit}>
      <div className="form-grid">
        <div className="form-group">
          <label htmlFor="title">Title *</label>
          <input id="title" name="title" type="text" value={form.title} onChange={handleChange} required />
          {fieldErrors?.title && <span className="form-error">{fieldErrors.title}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="author">Author *</label>
          <input id="author" name="author" type="text" value={form.author} onChange={handleChange} required />
          {fieldErrors?.author && <span className="form-error">{fieldErrors.author}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="isbn">ISBN *</label>
          <input
            id="isbn"
            name="isbn"
            type="text"
            placeholder="e.g. 9780134685991"
            value={form.isbn}
            onChange={handleChange}
            required
          />
          {fieldErrors?.isbn && <span className="form-error">{fieldErrors.isbn}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="publisher">Publisher</label>
          <input id="publisher" name="publisher" type="text" value={form.publisher} onChange={handleChange} />
        </div>

        <div className="form-group">
          <label htmlFor="genre">Genre</label>
          <input id="genre" name="genre" type="text" value={form.genre} onChange={handleChange} />
        </div>

        <div className="form-group">
          <label htmlFor="price">Price ($) *</label>
          <input
            id="price"
            name="price"
            type="number"
            step="0.01"
            min="0"
            value={form.price}
            onChange={handleChange}
            required
          />
          {fieldErrors?.price && <span className="form-error">{fieldErrors.price}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="quantity">Quantity *</label>
          <input
            id="quantity"
            name="quantity"
            type="number"
            min="0"
            value={form.quantity}
            onChange={handleChange}
            required
          />
          {fieldErrors?.quantity && <span className="form-error">{fieldErrors.quantity}</span>}
        </div>

        <div className="form-group form-group--full">
          <label htmlFor="description">Description</label>
          <textarea id="description" name="description" rows={4} value={form.description} onChange={handleChange} />
        </div>

        <div className="form-group form-group--full">
          <label htmlFor="image">Cover Image</label>
          <input id="image" name="image" type="file" accept="image/jpeg,image/png,image/webp" onChange={handleImageChange} />
          {previewUrl && (
            <div className="image-preview">
              <img src={previewUrl} alt="Cover preview" />
            </div>
          )}
        </div>
      </div>

      <div className="form-actions">
        <button type="submit" className="btn btn--primary" disabled={submitting}>
          {submitting ? 'Saving...' : submitLabel}
        </button>
      </div>
    </form>
  );
}

export default BookForm;
