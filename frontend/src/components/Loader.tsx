function Loader({ label = 'Loading...' }: { label?: string }) {
  return (
    <div className="loader">
      <div className="loader__spinner" role="status" aria-label={label} />
      <p className="loader__label">{label}</p>
    </div>
  );
}

export default Loader;
