import { NavLink } from 'react-router-dom';

function Navbar() {
  return (
    <header className="navbar">
      <div className="navbar__brand">
        <span className="navbar__icon" aria-hidden="true">📚</span>
        <span>Book Management System</span>
      </div>
      <nav className="navbar__links">
        <NavLink
          to="/"
          className={({ isActive }) => (isActive ? 'navbar__link navbar__link--active' : 'navbar__link')}
        >
          All Books
        </NavLink>
        <NavLink
          to="/add"
          className={({ isActive }) => (isActive ? 'navbar__link navbar__link--active' : 'navbar__link')}
        >
          Add Book
        </NavLink>
        <NavLink
          to="/search"
          className={({ isActive }) => (isActive ? 'navbar__link navbar__link--active' : 'navbar__link')}
        >
          Search by ISBN
        </NavLink>
      </nav>
    </header>
  );
}

export default Navbar;
