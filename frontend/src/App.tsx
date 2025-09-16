import React from 'react';
import './App.css';
import SearchBar from './components/SearchBar';

function App() {
  const template = require('./assets/pokedex_entry_template_2.jpg');
  return (
    <div className="App">
      <header className="App-header">
        <SearchBar />
        <img 
          className="App-link" 
          src={template}
          style={
            { width: "100vw", height: "100vh" }
          }
        >
        </img>
      </header>
    </div>
  );
}

export default App;
