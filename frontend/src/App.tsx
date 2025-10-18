import React from 'react';
import './App.css';
import PokemonSearchOverlay from './components/PokemonSearchOverlay/PokemonSearchOverlay';
import PokemonDisplayContainer from './components/PokemonDisplayContainer/PokemonDisplayContainer';

function App() {
  const template = require('./assets/pokedex_entry_template_2.jpg');
  return (
    <div className="App">
      <header className="App-header relative w-screen h-screen">
        <img
          src={template}
          alt="Background"
          className="w-full h-full"
        />

        <div
          className="absolute z-10 yellow-bg p-1 rounded-md max-w-full w-fit sm:w-fit md:w-fit"
          style={{
            top: "15%",        // relative to container height
            left: "50%",       // relative to container width
          }}
        >
          <PokemonDisplayContainer />
        </div>
      </header>
    </div>
  );
}

export default App;
