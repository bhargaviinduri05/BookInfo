import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import BookListPage from './pages/BookListPage';
import AddBookPage from './pages/AddBookPage';
import EditBookPage from './pages/EditBookPage';
import SearchByIsbnPage from './pages/SearchByIsbnPage';
import './styles/App.css';

function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <main className="container">
        <Routes>
          <Route path="/" element={<BookListPage />} />
          <Route path="/add" element={<AddBookPage />} />
          <Route path="/edit/:id" element={<EditBookPage />} />
          <Route path="/search" element={<SearchByIsbnPage />} />
        </Routes>
      </main>
    </BrowserRouter>
  );
}

export default App;
