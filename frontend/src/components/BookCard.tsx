import { Link } from 'react-router-dom';
import type { Book } from '../types/book';

interface BookCardProps {
  book: Book;
  onDelete: (id: number, title: string) => void;
}

function BookCard({ book, onDelete }: BookCardProps) {
  return (
    <div className="book-card">
      <div className="book-card__image-wrapper">
        {book.imageUrl ? (
          <img src={book.imageUrl} alt={`Cover of ${book.title}`} className="book-card__image" />
        ) : (
          <div className="book-card__image-placeholder" aria-hidden="true">
            No Cover
          </div>
        )}
      </div>

      <div className="book-card__body">
        <h3 className="book-card__title">{book.title}</h3>
        <p className="book-card__author">by {book.author}</p>

        <dl className="book-card__meta">
          <div className="book-card__meta-row">
            <dt>ISBN</dt>
            <dd>{book.isbn}</dd>
          </div>
          {book.genre && (
            <div className="book-card__meta-row">
              <dt>Genre</dt>
              <dd>{book.genre}</dd>
            </div>
          )}
          <div className="book-card__meta-row">
            <dt>Price</dt>
            <dd>${book.price.toFixed(2)}</dd>
          </div>
          <div className="book-card__meta-row">
            <dt>In stock</dt>
            <dd>{book.quantity}</dd>
          </div>
        </dl>

        <div className="book-card__actions">
          <Link to={`/edit/${book.id}`} className="btn btn--secondary">
            Edit
          </Link>
          <button className="btn btn--danger" onClick={() => onDelete(book.id, book.title)}>
            Delete
          </button>
        </div>
      </div>
    </div>
  );
}

export default BookCard;
