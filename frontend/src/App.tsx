import React from 'react';
import './App.css';
import SearchBar from './components/SearchBar';

function App() {
  const template = require('./assets/pokedex_entry_template_2.jpg');
  return (
    <div className="App">
      <header className="App-header">
        <div className="yellow-bg searchbar">
          <SearchBar />
        </div>
        <img
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
