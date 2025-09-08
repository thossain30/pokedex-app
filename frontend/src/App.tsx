import React from 'react';
import './App.css';

function App() {
  const template = require('./assets/pokedex_entry_template.jpg');
  return (
    <div className="App">
      <header className="App-header">
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
