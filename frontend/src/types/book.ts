// Shape returned by the backend for a single book (BookResponseDTO)
export interface Book {
  id: number;
  title: string;
  author: string;
  isbn: string;
  publisher?: string;
  genre?: string;
  price: number;
  quantity: number;
  description?: string;
  imageUrl?: string | null;
  createdAt?: string;
  updatedAt?: string;
}

// Shape sent to the backend when creating/updating a book (BookRequestDTO)
export interface BookFormData {
  title: string;
  author: string;
  isbn: string;
  publisher: string;
  genre: string;
  price: string; // kept as string in the form, parsed to number before sending
  quantity: string;
  description: string;
}

// Generic API envelope returned by every backend endpoint (ApiResponse<T>)
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp?: string;
}

// Shape of field-level validation errors returned by the backend (400 responses)
export type ValidationErrors = Record<string, string>;

export const emptyBookForm: BookFormData = {
  title: '',
  author: '',
  isbn: '',
  publisher: '',
  genre: '',
  price: '',
  quantity: '',
  description: '',
};
