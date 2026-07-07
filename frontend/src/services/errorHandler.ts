import { isAxiosError } from 'axios';
import type { ApiResponse, ValidationErrors } from '../types/book';

/**
 * Extracts a human-readable error message from an Axios error thrown by
 * a call to the backend. Falls back to a generic message when the error
 * shape is unexpected (e.g. network failure, CORS issue, server down).
 */
export function extractErrorMessage(error: unknown): string {
  if (isAxiosError(error)) {
    const data = error.response?.data as ApiResponse<unknown> | undefined;

    if (data?.message) {
      // If validation errors were returned as a field->message map, append them
      if (data.data && typeof data.data === 'object' && !Array.isArray(data.data)) {
        const fieldErrors = data.data as ValidationErrors;
        const details = Object.entries(fieldErrors)
          .map(([field, msg]) => `${field}: ${msg}`)
          .join(', ');
        return details ? `${data.message} (${details})` : data.message;
      }
      return data.message;
    }

    if (error.code === 'ERR_NETWORK') {
      return 'Unable to reach the server. Please check that the backend is running.';
    }

    return error.message || 'An unexpected error occurred while contacting the server.';
  }

  if (error instanceof Error) {
    return error.message;
  }

  return 'An unexpected error occurred.';
}
