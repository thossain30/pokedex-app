import React from 'react';
import './App.css';
import PokemonDisplayContainer from './components/PokemonDisplayContainer/PokemonDisplayContainer';

function App() {
  const template = require('./assets/pokedex_entry_template_2.jpg');
  return (
    <div className="relative w-full h-screen max-w-3xl mx-auto">
      <img
        src={template}
        alt="Background"
        className="w-full h-full z-0"
      />

      <div className="displayContainer">
        <PokemonDisplayContainer />
      </div>
    </div>
  );
}

export default App;
