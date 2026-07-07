import apiClient from './apiClient';
import type { ApiResponse, Book, BookFormData } from '../types/book';

/**
 * Converts the form data (all strings) into the JSON payload shape
 * expected by the backend's BookRequestDTO.
 */
function buildBookPayload(form: BookFormData) {
  return {
    title: form.title.trim(),
    author: form.author.trim(),
    isbn: form.isbn.trim(),
    publisher: form.publisher.trim() || undefined,
    genre: form.genre.trim() || undefined,
    price: form.price === '' ? undefined : Number(form.price),
    quantity: form.quantity === '' ? undefined : Number(form.quantity),
    description: form.description.trim() || undefined,
  };
}

/**
 * Builds a multipart/form-data payload containing the book JSON
 * (as a "book" part) and an optional image file (as an "image" part) —
 * matching the @RequestPart names expected by BookController.
 */
function buildMultipartForm(form: BookFormData, imageFile: File | null): FormData {
  const payload = buildBookPayload(form);
  const formData = new FormData();
  formData.append('book', JSON.stringify(payload));
  if (imageFile) {
    formData.append('image', imageFile);
  }
  return formData;
}

export const bookService = {
  async getAllBooks(): Promise<Book[]> {
    const response = await apiClient.get<ApiResponse<Book[]>>('/api/books');
    return response.data.data;
  },

  async getBookById(id: number): Promise<Book> {
    const response = await apiClient.get<ApiResponse<Book>>(`/api/books/${id}`);
    return response.data.data;
  },

  async getBookByIsbn(isbn: string): Promise<Book> {
    const response = await apiClient.get<ApiResponse<Book>>(`/api/books/isbn/${encodeURIComponent(isbn)}`);
    return response.data.data;
  },

  async createBook(form: BookFormData, imageFile: File | null): Promise<Book> {
    const formData = buildMultipartForm(form, imageFile);
    const response = await apiClient.post<ApiResponse<Book>>('/api/books', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return response.data.data;
  },

  async updateBook(id: number, form: BookFormData, imageFile: File | null): Promise<Book> {
    const formData = buildMultipartForm(form, imageFile);
    const response = await apiClient.put<ApiResponse<Book>>(`/api/books/${id}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return response.data.data;
  },

  async deleteBook(id: number): Promise<void> {
    await apiClient.delete<ApiResponse<void>>(`/api/books/${id}`);
  },

  async uploadBookImage(id: number, imageFile: File): Promise<Book> {
    const formData = new FormData();
    formData.append('image', imageFile);
    const response = await apiClient.post<ApiResponse<Book>>(`/api/books/${id}/image`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return response.data.data;
  },
};

export default bookService;
